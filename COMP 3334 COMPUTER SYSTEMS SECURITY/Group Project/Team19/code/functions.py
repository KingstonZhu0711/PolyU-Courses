# Download by calling "pip install cryptography" in cmd for using the cryptography library
import base64
import datetime
import http.client
import json
import logging
import os
import re
import secrets
import smtplib
import sqlite3
import string
import time
import urllib.parse
from email.mime.text import MIMEText
import hashlib
import bcrypt
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import padding as sym_padding
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import padding as asym_padding
from cryptography.hazmat.primitives.asymmetric import rsa


DATABASE_NAME = 'File_System.db'

def export_audit_logs():
    """Export audit logs; only available to admins."""
    try:
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        
        # Obtain logs
        cursor.execute('''
            SELECT timestamp, action_type, username, details, status 
            FROM audit_logs 
            ORDER BY timestamp DESC
        ''')
        
        timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"audit_logs_{timestamp}.csv"
        export_dir = "audit_exports"
        
        # Create export directory
        os.makedirs(export_dir, exist_ok=True)
        full_path = os.path.join(export_dir, filename)
        
        # Write to CSV file
        with open(full_path, 'w', encoding='utf-8') as f:
            f.write("Time stamp,Operation,User,Details,Status\n")
            for log in cursor.fetchall():
                details = log[3] if log[3] else ""
                f.write(f'"{log[0]}","{log[1]}","{log[2]}","{details}","{log[4]}"\n')
        
        print(f"Saved {cursor.rowcount} log file to {full_path}")
        return True
        
    except sqlite3.Error as e:
        print(f"Database error: {str(e)}")
        return False
    except Exception as e:
        print(f"Error saving: {str(e)}")
        return False
    finally:
        if 'conn' in locals():
            conn.close()

def get_user_role(username):
    """Retrieve the role of the user."""
    conn = sqlite3.connect(DATABASE_NAME)
    try:
        cursor = conn.cursor()
        cursor.execute(
            "SELECT role FROM users WHERE username = ?",
            (username,)
        )
        result = cursor.fetchone()
        return result[0] if result else None
    except sqlite3.Error as e:
        logging.error(f"Database error: {str(e)}")
        return None
    finally:
        conn.close()

def login():
    """
    Handle user login by hashing the password on the client side
    and sending it to the server for verification.
    """
    username = input("Please enter your username: ")
    password = input("Please enter your password: ")

    # Retrieve the user's salt from the server
    connection = http.client.HTTPConnection('localhost', 8000)
    connection.request('GET', f"/get_salt_password?username={urllib.parse.quote(username)}")
    response = connection.getresponse()

    if response.status != 200:
        return False, None

    salt = response.read().decode('utf-8')

    # Hash the password on the client side using the retrieved salt
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), salt.encode('utf-8')).decode('utf-8')

    # Send the hashed password and username to the server for verification
    login_path = f"/login?username={urllib.parse.quote(username)}&password={urllib.parse.quote(hashed_password)}"
    connection.request('GET', login_path)

    response = connection.getresponse()
    connection.close()

    if response.status == 200:
        log_audit("LOGIN", username, f"User {username} logged in")
        return True, username  # Login successful
    else:
        log_audit("LOGIN", username, f"User {username} login failed", status="FAILED")
        print("Login failed. Invalid username or password.")
        return False, None

def register():
    """
    Handle user registration by hashing the password on the client side
    and sending it to the server for storage.
    """
    username = input("\nPlease enter a username to register: ")
    password = input("Please enter a password: ")
    role = input("Please select a user role [user/admin]: ").strip().lower()

    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=1024
    )
    iv = os.urandom(16)

    encrypted_private_key = encrypt_key(password, iv, private_key)
    public_key = private_key.public_key()

    # Manage JSON file
    filename = "users.json"
    user_data = {}
    if os.path.isfile(filename):
        with open(filename, 'r') as f:
            user_data = json.load(f)

    user_data[username] = {
        "iv": base64.b64encode(iv).decode('utf-8'),
        "encrypted_private_key": base64.b64encode(encrypted_private_key).decode('utf-8')
    }

    with open(filename, 'w') as f:
        json.dump(user_data, f, indent=2)

    if role not in ('user', 'admin'):
        print("Invalid role, defaulting to 'user'.")
        role = 'user'

    # Generate a salt
    salt_password = bcrypt.gensalt().decode('utf-8')
    # Hash the password with the salt
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), salt_password.encode('utf-8')).decode('utf-8')

    # Send the hashed password and salt to the server for storage
    connection = http.client.HTTPConnection('localhost', 8000)
    params = urllib.parse.urlencode({
        'username': username,
        'password': hashed_password,
        'salt_password': salt_password,
        'salt_derivation': bcrypt.gensalt().decode('utf-8'),
        'public_key': serialize_public_key(public_key),
        'role': role
    })
    connection.request('GET', f"/register?{params}")

    response = connection.getresponse()
    connection.close()

    if response.status == 200:
        log_audit("REGISTER", username, f"User {username} registered successfully")
        print("Registration successful!")
        return True
    else:
        log_audit("REGISTER", username, f"User {username} registration failed", status="FAILED")
        print("Registration failed. Username may already exist.")
        return False

def derive_key(password, salt, iterations=10):
    """Derive a key from the password and salt using PBKDF2."""
    current_value = password.encode('utf-8')
    for _ in range(iterations):
        current_value = hashlib.pbkdf2_hmac('sha256', current_value, salt.encode('utf-8'), 1)
    return current_value

def encrypt_file(input_path, key, iv, output_path):
    """Encrypt the file using AES."""
    if len(key) not in {16, 24, 32}:
        raise ValueError("Key must be 16, 24, or 32 bytes long.")
    try:
        with open(input_path, 'rb') as f:
            data = f.read()

        # Pad the data
        padder = sym_padding.PKCS7(algorithms.AES.block_size).padder()
        padded_data = padder.update(data) + padder.finalize()

        # Encrypt the data
        cipher = Cipher(algorithms.AES(key), modes.CBC(iv))
        encryptor = cipher.encryptor()
        ciphertext = encryptor.update(padded_data) + encryptor.finalize()

        with open(output_path, 'wb') as f:
            f.write(ciphertext)
        print(f"\nFile encrypted successfully and saved to {output_path}")

    except Exception as e:
        print(f"An error occurred during encryption: {e}")

def serialize_private_key(key: rsa.RSAPrivateKey) -> bytes:
    """Serialize RSA private key to PEM format bytes."""
    return key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    )

def serialize_public_key(key: rsa.RSAPublicKey) -> bytes:
    """Serialize RSA public key to PEM format bytes."""
    return key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )

def deserialize_private_key(data: bytes) -> rsa.RSAPrivateKey:
    """Deserialize RSA private key from PEM format bytes."""
    return serialization.load_pem_private_key(
        data,
        password=None,  # Not encrypted
        backend=default_backend()
    )

def deserialize_public_key(data: bytes) -> rsa.RSAPublicKey:
    """Deserialize RSA public key from PEM format bytes."""
    return serialization.load_pem_public_key(
        data,
        backend=default_backend()
    )

def encrypt_key(aes_key: str, iv: bytes, rsa_key: rsa.RSAPrivateKey) -> bytes:
    """
    Encrypt the RSA private key.
    Parameters:
        aes_key (str): Password string used for encryption
        iv (bytes): 16-byte initialization vector
        rsa_key (RSAPrivateKey): RSA private key object to be encrypted
    Returns:
        bytes: Encrypted byte data
    """
    try:
        # Serialize the key to bytes
        key_bytes = serialize_private_key(rsa_key)

        # Generate AES key
        hashed_aes_key = hashlib.sha256(aes_key.encode()).digest()

        # Create a padder
        padder = sym_padding.PKCS7(algorithms.AES.block_size).padder()

        # Pad the data
        padded_data = padder.update(key_bytes) + padder.finalize()

        # Encrypt the data
        cipher = Cipher(
            algorithm=algorithms.AES(hashed_aes_key),
            mode=modes.CBC(iv)
        )
        encryptor = cipher.encryptor()
        return encryptor.update(padded_data) + encryptor.finalize()

    except Exception as e:
        raise ValueError(f"Encryption failed: {str(e)}") from e

def decrypt_key(aes_key: str, iv: bytes, encrypted_data: bytes) -> rsa.RSAPrivateKey:
    """
    Decrypt and reconstruct the RSA private key.
    Parameters:
        aes_key (str): Password used for encryption
        iv (bytes): Initialization vector used for encryption
        encrypted_data (bytes): Encrypted byte data
    Returns:
        RSAPrivateKey: Decrypted RSA private key object
    """
    try:
        # Generate AES key
        hashed_aes_key = hashlib.sha256(aes_key.encode()).digest()

        # Create a decryptor
        cipher = Cipher(
            algorithm=algorithms.AES(hashed_aes_key),
            mode=modes.CBC(iv)
        )
        decryptor = cipher.decryptor()

        # Decrypt the data
        decrypted_padded = decryptor.update(encrypted_data) + decryptor.finalize()

        # Unpad the data
        unpadder = sym_padding.PKCS7(algorithms.AES.block_size).unpadder()
        decrypted = unpadder.update(decrypted_padded) + unpadder.finalize()

        # Deserialize the key
        return serialization.load_pem_private_key(
            decrypted,
            password=None
        )
    except Exception as e:
        raise ValueError(f"Decryption failed: {str(e)}") from e

def decrypt_file(encrypted_data, key, iv, output_path):
    """Decrypt the file using AES."""
    try:
        cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
        decryptor = cipher.decryptor()

        # Decrypt the data
        padded_data = decryptor.update(encrypted_data) + decryptor.finalize()

        # Unpad the data
        unpadder = sym_padding.PKCS7(algorithms.AES.block_size).unpadder()
        plaintext = unpadder.update(padded_data) + unpadder.finalize()

        # Save the decrypted file
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, 'wb') as f:
            f.write(plaintext)

        print(f"\nFile decrypted successfully to {output_path}")
        return True

    except ValueError as e:
        print(f"\nPadding error: {e}")
        print("This usually means either:")
        print("1. Wrong password was used")
        print("2. File was corrupted")
        print("3. IV doesn't match the encryption IV")
        return False
    except Exception as e:
        print(f"\nDecryption error: {e}")
        return False
        
def list_files(username):

    connection = http.client.HTTPConnection('localhost', 8000)
    list_files_path = f"/files?username={urllib.parse.quote(username)}"
    connection.request('GET', list_files_path)

    response = connection.getresponse()
    if response.status == 200:
        files = json.loads(response.read().decode())
        print("\nFiles under your account:")
        for file in files:
            # Display only the ID, Filename, and Username (derived as 'owner')
            print(f"File ID: {file['id']}, Filename: {file['filename']}, Owner ID: {file['user_id']}")
    else:
        print("Failed to retrieve files:", response.read().decode())

def detect_legal_filename(filename):
    """Check if a filename is valid and does not contain errors."""

    # Step 1: Decode URL
    decoded = urllib.parse.unquote(filename)
    # Step 2: Unification
    decoded = decoded.replace("\\", "/")
    # Step 3: Extract pure name
    
    # Defense 1: Directory traversal attack
    if re.search(r'(\.\.|%2e%2e)', filename, re.IGNORECASE):
        print("Directory traversal attack detected!")
        return False
    
    # Defense 2: Retain device names (Windows)
    base_name = os.path.splitext(filename)[0].upper()
    reserved = {"CON", "PRN", "AUX", "NUL", "COM1", "LPT1"}
    if base_name in reserved:
        print("System retained characters detected!")
        return False
    
    # Defense 3: Limit length
    if len(filename) > 255:
        print("File name is too long!")
        return False
    
    # If all checks pass, the filename is valid
    return True


def upload_file(username):
    """Upload a file to the server."""
    connection = None
    try:
        file_path = input("\nPlease enter the path of the file to upload: ").strip('"')

        # Detect whether the input file name can be used to attack
        if not detect_legal_filename(file_path):
            print("Invalid file name")
            return 
        
        if not os.path.exists(file_path):
            print("File not found at the specified path.")
            return
        
        # Retrieve the user's salt and hashed password from the database
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT salt_password, salt_derivation, password FROM users WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()

        if result is None:
            print("User not found.")
            return

        salt_password = result[0]
        salt_derivation = result[1]
        stored_hashed_password = result[2]

        # Prompt for the user's plaintext password
        password = input("Please enter your password to continue to upload: ")

        # Hash the entered plaintext password using the retrieved salt
        hashed_password = bcrypt.hashpw(password.encode('utf-8'), salt_password.encode('utf-8')).decode('utf-8')

        # Verify if the hashed passwords match
        if hashed_password != stored_hashed_password:
            print("Password incorrect. Please try again.")
            return

        # Derive the AES key from the password
        key = derive_key(password, salt_derivation)

        # Generate a random IV
        iv = os.urandom(16)

        # Define the output path for the encrypted file
        encrypted_file_path = f"{file_path}.enc"

        # Encrypt the file
        encrypt_file(file_path, key, iv, encrypted_file_path)

        # Read the encrypted file data
        with open(encrypted_file_path, 'rb') as f:
            encrypted_data = f.read()

        # Establish a connection to the server
        connection = http.client.HTTPConnection('localhost', 8000)

        try:
            # Convert IV to hexadecimal string for transport
            iv_hex = iv.hex()

            # Create a JSON payload with the IV, username, and filename
            key_iv_payload = {
                'username': username,
                'filename': os.path.basename(file_path),  # Use only the filename
                'iv': iv_hex
            }

            # Send the IV in the request body
            print("Sending IV payload...")
            connection.request('POST', "/upload_iv",
                            json.dumps(key_iv_payload).encode('utf-8'),
                            {'Content-Type': 'application/json'})

            # Get the response to ensure the IV was uploaded successfully
            response = connection.getresponse()
            if response.status != 200:
                print(f"Failed to upload IV: {response.read().decode()}")
                return
            connection.close()

            connection = http.client.HTTPConnection('localhost', 8000)

            # Now send the encrypted file data
            print("Sending the encrypted file data...")
            headers = {
                'Content-Type': 'application/octet-stream',
                'Filename': os.path.basename(file_path),
                'User-ID': username  # Username identifies the user
            }
            connection.request('POST', "/upload_file", encrypted_data, headers)

            response = connection.getresponse()
            if response.status == 200:
                print("File uploaded successfully!")
            else:
                print(f"Failed to upload file: {response.read().decode()}")

        except Exception as e:
            print(f"Error during file upload: {e}")
            # Clean up temporary encrypted file if it exists
            if 'encrypted_file_path' in locals() and os.path.exists(encrypted_file_path):
                os.remove(encrypted_file_path)

        finally:
            if connection is not None:
                connection.close()
    finally:
            if connection is not None:
                connection.close()

    # Clean up the temporary encrypted file
    os.remove(encrypted_file_path)
    log_audit("UPLOAD", username, f"Uploaded file: {os.path.basename(file_path)}")
    
def download_file(username):
    """Download a file from the server."""

    connection = None
    try:
        filename = input("\nPlease enter the name of the file to download: ")
        password = input("Please enter your password to continue download: ")
        
        if not verify_login(username, password):
            print("Invalid password.")
            return
        
        if not detect_legal_filename(filename):
            print("Invalid file name")
            return 
        # Check whether the target file exists
        if not detect_filename(filename):
            print("The entered file does not exist. Please try an existing file.")
            return
        
        # Get user's salt
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT salt_derivation FROM users WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()
        
        if not result:
            print("User not found.")
            return

        salt = result[0]
        key = derive_key(password, salt)
        
        # Download the file
        connection = http.client.HTTPConnection('localhost', 8000)
        download_path = f"/download?username={urllib.parse.quote(username)}&filename={urllib.parse.quote(filename)}"
        connection.request('GET', download_path)
        response = connection.getresponse()
        
        if response.status != 200:
            print(f"Download failed: {response.read().decode()}")
            return

        encrypted_data = response.read()
        iv_hex = response.getheader('IV')
        
        if not iv_hex:
            print("No IV found in response")
            return
            
        try:
            iv = bytes.fromhex(iv_hex)
        except ValueError:
            print("Invalid IV format")
            return
        finally:
            if connection is not None:
                connection.close()

        # Prepare output path
        user_dir = os.path.join('downloaded_file', username)
        os.makedirs(user_dir, exist_ok=True)
        output_path = os.path.join(user_dir, f"decrypted_{filename}")
        
        # Decrypt the file
        if not decrypt_file(encrypted_data, key, iv, output_path):
            print("Failed to decrypt the file")
            
    except Exception as e:
        print(f"Download error: {e}")
    finally:
        if connection is not None:
            connection.close()

def delete_file(username):
    """Delete a file from the server."""
    filename = input("\nPlease enter the name of the file to delete: ")
    if not detect_legal_filename(filename):
        print("Invalid file name")
        return 
    # Check whether the target file exists
    if not detect_filename(filename):
        print("The entered file does not exist. Please try an existing file.")
        return
    connection = http.client.HTTPConnection('localhost', 8000)
    delete_path = f"/delete?username={urllib.parse.quote(username)}&filename={urllib.parse.quote(filename)}"
    connection.request('DELETE', delete_path)

    response = connection.getresponse()
    print(f"Delete Status: {response.status}")
    print(f"Response: {response.read().decode()}")
    connection.close()
    log_audit("DELETE", username, f"Deleted file: {filename}")

def share_file(username):
    """Share a file with another user."""
    filename = input("\nPlease enter the name of the file to share: ")
    if not detect_legal_filename(filename):
        print("Invalid file name")
        return
    # Check whether the target file exists
    if not detect_filename(filename):
        print("The entered file does not exist. Please try an existing file.")
        return 
    
    target_user = input("Please enter the username of the user you want to share the file with: ")
    if target_user==username:
        print("You can't share file to yourself")
        return
    if not detect_user(target_user):
        print("The target user does not exist. Please try sharing with an existing user.")
        return
    
    password = input("Please enter your password: ")
    if not verify_login(username, password):
        print("Incorrect password. Please try again.")
        return
    
    # Retrieve the target user's public key from the server
    connection = http.client.HTTPConnection('localhost', 8000)
    connection.request('GET', f"/get_public_key?username={urllib.parse.quote(target_user)}")
    response = connection.getresponse()

    if response.status != 200:
        print("Failed to retrieve the public key. Please check the username.")
        return

    target_user_public_key_bytes = response.read()
    target_user_public_key = deserialize_public_key(target_user_public_key_bytes)
    connection.close()

    connection = http.client.HTTPConnection('localhost', 8000)
    connection.request('GET', f"/get_salt_derivation?username={urllib.parse.quote(username)}")
    response = connection.getresponse()
    if response.status != 200:
        print("Failed to retrieve the salt derivation. Please check your username.")
        return
    salt_derivation = response.read().decode('utf-8')
    connection.close()

    real_key = derive_key(password, salt_derivation)

    encrypted_real_key = target_user_public_key.encrypt(real_key, asym_padding.OAEP(
        mgf=asym_padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
    ))

    connection = http.client.HTTPConnection('localhost', 8000)
    payload = json.dumps({
        'owner': username,
        'filename': filename,
        'target_user': target_user,
        'encrypted_real_key': base64.b64encode(encrypted_real_key).decode('utf-8')
    })
    connection.request('POST', "/share_file", payload, {'Content-Type': 'application/json'})
    response = connection.getresponse()
    if response.status != 200:
        print(f"Failed to share file: {response.read().decode()}")
        return
    print("File successfully shared to user "+ target_user)
    connection.close()

    log_audit("File sharing", username, f"File {filename} shared with {target_user}")

def decrypt_my_secret_key_from_json(username, password):
    """Decrypt the user's private key from the JSON file."""
    with open('users.json', 'r') as f:
        user_data = json.load(f)
        user_entry = user_data[username]
        iv = base64.b64decode(user_entry["iv"])
        encrypted_private_key = base64.b64decode(user_entry["encrypted_private_key"])
        private_key = decrypt_key(password, iv, encrypted_private_key)
        return private_key

def read_shared_files(username):
    """Read and display shared files for the user."""
    password = input("Please enter your password: ")
    if not verify_login(username, password):
        print("Incorrect password. Please try again.")
        return

    connection = http.client.HTTPConnection('localhost', 8000)
    list_shared_path = f"/shared_files?username={urllib.parse.quote(username)}"
    connection.request('GET', list_shared_path)
    
    response = connection.getresponse()
    if response.status != 200:
        print("\nFailed to get shared files list.")
        return
    
    files = json.loads(response.read().decode())
    print("\nFiles that have been shared with you:")
    for i, file in enumerate(files, 1):
        print(f"{i}. File ID: {file['file_id']} {file['file_name']} Owner: {file['owner']}")

    index_input = input("\nPlease enter the index of the file to read: ")
    try:
        # Convert input to an integer and validate it
        index = int(index_input)
        if index < 1 or index > len(files):  # Ensure the index is within range
            print(f"Invalid index. Please enter a number between 1 and {len(files)}. Please try again.")
            return
    except ValueError:
        # Handle invalid input (e.g., non-integer input)
        print("Invalid input. Please enter a valid integer. Please try again.")
        return

    selected_file = files[index - 1]
    file_name = selected_file['file_name']
    file_owner = selected_file['owner']
    file_encrypted_real_key = base64.b64decode(selected_file['encrypted_real_key'])   
    connection = http.client.HTTPConnection('localhost', 8000)
    download_path = f"/download?username={urllib.parse.quote(str(file_owner))}&filename={urllib.parse.quote(file_name)}"
    connection.request('GET', download_path)
    response = connection.getresponse()

    if response.status == 200:
        try:
            encrypted_data = response.read()
            iv_hex = response.getheader('IV')

            if not iv_hex:
                print("No IV found in response.")
                return

            try:
                iv = bytes.fromhex(iv_hex)
            except ValueError:
                print("Invalid IV format.")
                return

            # Prepare output path
            user_dir = os.path.join('shared_file', username)
            os.makedirs(user_dir, exist_ok=True)
            output_path = os.path.join(user_dir, f"decrypted_shared_{file_name}")

            rsa_secret_key = decrypt_my_secret_key_from_json(username, password)

            real_key = rsa_secret_key.decrypt(
                file_encrypted_real_key,
                asym_padding.OAEP(
                    mgf=asym_padding.MGF1(algorithm=hashes.SHA256()),
                    algorithm=hashes.SHA256(),
                    label=None
                )
            )

            # Decrypt the file
            if not decrypt_file(encrypted_data, real_key, iv, output_path):
                print("Failed to decrypt the file.")

            # Open and read the file
            if output_path.lower().endswith('.txt'):
                with open(output_path, 'r') as file:
                    content = file.read()
                print("This is the shared text:\n" + content)
            else:
                print("Only txt files can be printed out.")
            log_audit("Read Shared File", username, f"{username} read the file {file_name} shared by {file_owner}")
        except Exception as e:
            print(f"Error: {str(e)}")
        finally:
            connection.close()
    else:
        print(f"Download failed: {response.read().decode()}")

def reset_password(username, current_password, new_password):
    """Reset the user's password."""
    if current_password == new_password:
        print("New password must be different from the current password.")
        return False

    # Retrieve the user's salt and hashed password from the database
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    cursor.execute('SELECT salt_password, password FROM users WHERE username = ?', (username,))
    result = cursor.fetchone()
    if result is None:
        print("User not found.")
        conn.close()
        return False

    salt, stored_hashed_password = result
    # Verify current password
    current_hashed = bcrypt.hashpw(current_password.encode('utf-8'), salt.encode('utf-8')).decode('utf-8')
    if current_hashed != stored_hashed_password:
        print("Current password is incorrect.")
        conn.close()
        return False

    # Hash the new password with existing salt
    new_hashed_password = bcrypt.hashpw(new_password.encode('utf-8'), salt.encode('utf-8')).decode('utf-8')

    # Update the password in the database
    cursor.execute('UPDATE users SET password = ?, salt_password = ? WHERE username = ?', (new_hashed_password, salt, username))
    conn.commit()
    conn.close()

    log_audit("PASSWORD RESET", username, "Password reset successful")
    return True

def verify_login(username, password):
    """Verify user login credentials."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    cursor.execute('SELECT salt_password, password FROM users WHERE username = ?', (username,))
    result = cursor.fetchone()
    conn.close()

    if result is None:
        print("User not found.")
        return False

    salt = result[0]
    stored_hashed_password = result[1]

    # Hash the entered plaintext password using the retrieved salt
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), salt.encode('utf-8')).decode('utf-8')

    # Verify if the hashed passwords match
    return hashed_password == stored_hashed_password

def update_email(username, input_password, new_email):
    """Update the user's email address."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()

    if not verify_login(username, input_password):
        print("Invalid password.")
        conn.close()
        return False

    cursor.execute('SELECT email FROM users WHERE username = ?', (username,))
    result = cursor.fetchone()

    if result == (new_email,):
        print("Your new email is identical to the original email.")
        conn.close()
        return False

    # Check whether the input email address complies with the regular expression
    regex = r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b'
    if not re.fullmatch(regex, new_email):
        print("Invalid email. Please check the email address.")
        conn.close()
        return False

    try:
        cursor.execute('UPDATE users SET email = ? WHERE username = ?', (new_email, username))
        cursor.execute('UPDATE users SET email_status = ? WHERE username = ?', ("False", username))
        print("Your email has been successfully updated; please verify your email to use it for login.")
        conn.commit()
        log_audit("Set/Update Email", username, f"{username} has successfully set/updated their email")
        return cursor.rowcount > 0  # Returns True if the user was found and updated

    finally:
        conn.close()

def verify_email(username):
    """Verify the user's email address."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    cursor.execute('SELECT email, email_status FROM users WHERE username = ?', (username,))
    result = cursor.fetchone()
    email = result[0]
    email_status = result[1]
    conn.close()

    if email == "None":
        print("\nYou have not provided an email.")
        return False

    elif email_status == "True":
        print("\nYou have verified your email before.")
        return True

    else:
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT EXISTS(SELECT 1 FROM otps WHERE username = ?)', (username,))
        result = bool(cursor.fetchone()[0])
        conn.close()

        if not result:
            send_otp_to_email(username, email)
        else:
            print("We have already sent a verification code in the last 5 minutes. Please check your mailbox.")

        input_otp_code = input("Enter the verification code: ")
        return examine_otp(username, input_otp_code)

def update_email_status(username):
    """Update the email status to verified."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    cursor.execute('UPDATE users SET email_status = ? WHERE username = ?', ("True", username))
    conn.commit()
    conn.close()

def examine_otp(username, input_otp_code):
    """Examine the One-Time Password (OTP) for verification."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()

    cursor.execute('SELECT EXISTS(SELECT 1 FROM otps WHERE username = ?)', (username,))
    result = bool(cursor.fetchone()[0])
    conn.close()

    if not result:
        print("Expired verification code.")
        return False
    else:
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT otp_code, created_at FROM otps WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()

        if time.time() - int(result[1]) > 300:
            print("The verification code has expired. Please verify your email again to receive a new one.")
            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()
            cursor.execute('DELETE FROM otps WHERE username = ?', (username,))
            conn.commit()
            conn.close()
            return False

        elif not bcrypt.checkpw(input_otp_code.encode('utf-8'), result[0]):
            print("The verification code is incorrect.")
            return False

        else:
            print("The verification code is correct.")
            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()
            cursor.execute('DELETE FROM otps WHERE username = ?', (username,))
            conn.commit()
            conn.close()
            return True

def send_otp_to_email(username, email):
    """Send a One-Time Password (OTP) to the user's email for verification."""
    otp = generate_otp()
    sender = "wang.r.jie.william@gmail.com"  # Update with a valid email
    password = "dapinbqvscfxjoxa"  # Update with the email password

    msg = MIMEText(f"From Team 19: Your verification code is: {otp}\nValid for 5 minutes.")
    msg['Subject'] = 'COMP3334 Project Login Verification Code'
    msg['From'] = sender
    msg['To'] = email

    try:
        with smtplib.SMTP_SSL('smtp.gmail.com', 465) as server:
            server.login(sender, password)
            server.sendmail(sender, [email], msg.as_string())
    except Exception as e:
        print(f"Error sending email: {e}")

    print("\nA verification code has been sent to your email. Please enter the verification code within 5 minutes.")
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    hashed_otp = bcrypt.hashpw(otp.encode('utf-8'), bcrypt.gensalt())
    cursor.execute('INSERT INTO otps (username, email, otp_code, created_at) VALUES (?,?,?,?)', (username, email, hashed_otp, str(int(time.time()))))
    conn.commit()
    conn.close()

def generate_otp(length=6):
    """Generate a One-Time Password (OTP) of specified length."""
    chars = string.digits + string.ascii_uppercase
    chars = chars.replace('0', '').replace('O', '')
    return ''.join(secrets.choice(chars) for _ in range(length))

def email_verified(username):
    """Check if the user's email is verified."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    cursor.execute('SELECT email_status FROM users WHERE username = ?', (username,))
    result = cursor.fetchone()[0]
    conn.close()
    return result == "True"

def log_in_by_email(username):
    """Log in a user by email."""
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    cursor.execute('SELECT EXISTS(SELECT 1 FROM otps WHERE username = ?)', (username,))
    result = bool(cursor.fetchone()[0])
    conn.close()
    if not result:
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT email FROM users WHERE username = ?', (username,))
        email = cursor.fetchone()[0]
        send_otp_to_email(username, email)
    else:
        print("We have already sent a verification code in 5 minutes. Please check your mailbox and try again.")

    input_otp_code = input("Please enter the verification code: ")
    return examine_otp(username, input_otp_code)

def detect_filename(filename):
    """Check if a filename exists in the database."""
    # Connect to the SQLite database
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    
    # Execute a query to check if the filename exists
    cursor.execute('SELECT COUNT(*) FROM files WHERE filename = ?', (filename,))
    count = cursor.fetchone()[0]
    
    # Close the database connection
    conn.close()
    
    # Return True if the filename exists, otherwise False
    return count > 0

def detect_user(username):
    """Check if a user exists in the database."""
    # Connect to the SQLite database
    conn = sqlite3.connect(DATABASE_NAME)
    cursor = conn.cursor()
    
    # Execute a query to check if the username exists
    cursor.execute('SELECT COUNT(*) FROM users WHERE username = ?', (username,))
    count = cursor.fetchone()[0]
    
    # Close the database connection
    conn.close()
    
    # Return True if the username exists, otherwise False
    return count > 0

def log_audit(action, username, details, status="SUCCESS"):
    """Log actions performed by users."""
    logging.info(f"{action} - {username} - {details}")

    try:
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('''
            INSERT INTO audit_logs 
            (action_type, username, details, status)
            VALUES (?,?,?,?)
        ''', (action, username, details, status))
        conn.commit()
    except Exception as e:
        logging.error(f"Log write failed: {str(e)}")
    finally:
        conn.close()