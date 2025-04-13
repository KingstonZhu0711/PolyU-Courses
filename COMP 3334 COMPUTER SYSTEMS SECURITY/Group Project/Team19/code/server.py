import http.server
import json
import socketserver
import urllib.parse
import sqlite3
from datetime import datetime
from urllib.parse import urlparse, parse_qs


# Initialize the SQLite database
DATABASE_NAME = 'File_System.db'

def init_db():
    """Initialize the database and create necessary tables."""
    try:
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        
        # Create users table
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                email_status TEXT NOT NULL,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                salt_password TEXT NOT NULL,
                salt_derivation TEXT NOT NULL,
                public_key TEXT NOT NULL,
                role TEXT NOT NULL DEFAULT 'user'
            )
        ''')

        # Create OTP table
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS otps (
                username TEXT PRIMARY KEY,
                email TEXT NOT NULL,
                otp_code TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
        ''')
        
        # Create files table
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS files (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                filename TEXT NOT NULL,
                file_data BLOB NOT NULL,
                user_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                UNIQUE (filename, user_id)
            )
        ''')
        
        # Create file encryption keys table
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS file_encryption_iv (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                filename TEXT NOT NULL,
                iv BLOB NOT NULL,
                username TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (filename) REFERENCES files(filename) ON DELETE CASCADE
            )
        ''')

        # Create shared_files table for the file sharing function
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS shared_files (
                owner INTEGER NOT NULL,
                file_id INTEGER NOT NULL,
                target_username INTEGER NOT NULL,
                encrypted_real_key TEXT NOT NULL
            )
        ''')
        
        # Create audit_logs table
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS audit_logs (
                log_id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                action_type VARCHAR(20) NOT NULL,
                username VARCHAR(50) NOT NULL,
                details TEXT,
                status VARCHAR(10) CHECK(status IN ('SUCCESS','FAILED'))
            )
        ''')
        
        # Commit the changes
        conn.commit()

    except sqlite3.Error as e:
        print(f"An error occurred: {e}")

    finally:
        conn.close()
        
class MyHandler(http.server.SimpleHTTPRequestHandler):
    """Custom request handler for HTTP requests."""

    def do_GET(self):
        """Handle GET requests."""
        if self.path.startswith('/login'):
            self.handle_login()
        elif self.path.startswith('/register'):
            self.handle_register()
        elif self.path.startswith('/logout'):
            self.handle_logout()
        elif self.path.startswith('/reset_password'):
            self.handle_reset_password()  
        elif self.path.startswith('/download'):
            self.handle_file_download()
        elif self.path.startswith('/files'):
            self.handle_list_files()
        elif self.path.startswith('/shared_files'):
            self.handle_shared_files()
        elif self.path.startswith('/read_shared'):
            self.handle_read_shared()
        elif self.path.startswith('/get_salt_password'):
            self.handle_get_salt_password()
        elif self.path.startswith('/get_salt_derivation'):
            self.handle_get_salt_derivation()
        elif self.path.startswith('/get_public_key'):
            self.handle_get_public_key()
        else:
            self.send_response(404)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"404 Not Found")

    def do_POST(self):
        """Handle POST requests."""
        if self.path.startswith('/upload_iv'):
            self.handle_iv_upload()
        elif self.path.startswith('/upload_file'):
            self.handle_file_upload()
        elif self.path.startswith('/share_file'):
            self.handle_file_share()
        else:
            self.send_response(404)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"404 Not Found")

    def do_DELETE(self):
        """Handle DELETE requests."""
        if self.path.startswith('/delete'):
            self.handle_file_delete()
        else:
            self.send_response(404)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"404 Not Found")

    def handle_login(self):
        """Handle user login."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]
        password = params.get('password', [None])[0]  # This is the hashed password sent from the client

        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT password FROM users WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()

        if result is None:
            self.send_response(401)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Login failed! User not found.")
            return

        stored_hashed_password = result[0]

        # Compare the stored hashed password with the one sent from the client
        if stored_hashed_password != password:
            self.send_response(401)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Login failed! Invalid password.")
        else:
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Login successful!")

    def handle_get_salt_password(self):
        """Retrieve the salt used for password hashing."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]

        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT salt_password FROM users WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()

        if result is None:
            self.send_response(404)
            self.end_headers()
            self.wfile.write(b"User not found!")
        else:
            salt = result[0]
            self.send_response(200)
            self.send_header('Content-type', 'text/plain')
            self.end_headers()
            self.wfile.write(salt.encode('utf-8'))

    def handle_get_salt_derivation(self):
        """Retrieve the salt used for key derivation."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]

        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT salt_derivation FROM users WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()

        if result is None:
            self.send_response(404)
            self.end_headers()
            self.wfile.write(b"User not found!")
        else:
            salt = result[0]
            self.send_response(200)
            self.send_header('Content-type', 'text/plain')
            self.end_headers()
            self.wfile.write(salt.encode('utf-8'))

    def handle_get_public_key(self):
        """Retrieve the public key for the given user."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT public_key FROM users WHERE username = ?', (username,))
        result = cursor.fetchone()
        conn.close()
        if result is None:
            self.send_response(404)
            self.end_headers()
            self.wfile.write(b"User not found!")
        else:
            public_key = result[0]
            self.send_response(200)
            self.send_header('Content-type', 'text/plain')
            self.end_headers()
            self.wfile.write(public_key.encode('utf-8'))

    def handle_register(self):
        """Handle user registration."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]
        password = params.get('password', [None])[0]
        salt_password = params.get('salt_password', [None])[0]
        salt_derivation = params.get('salt_derivation', [None])[0]
        public_key = params.get('public_key', [None])[0]
        role = params.get('role', [None])[0]

        if role not in ('user', 'admin'):
            role = 'user'

        try:
            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()
            cursor.execute('INSERT INTO users (username, email, email_status, password, salt_password, salt_derivation, public_key, role) VALUES (?,?,?,?,?,?,?,?)',
                       (username, "None", "False", password, salt_password, salt_derivation, public_key, role))
            conn.commit()
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Registration successful!")
        except sqlite3.IntegrityError:
            print(f"IntegrityError: Username '{username}' already exists.")
            self.send_response(400)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Username already exists!")
        except Exception as e:
            print(f"Error in handle_register: {e}")
            self.send_response(500)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Internal Server Error")
        finally:
            conn.close()

    def handle_logout(self):
        """Handle user logout."""
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write(b"Logout Successful!")

    def handle_iv_upload(self):
        """Handle initialization vector (IV) upload."""
        try:
            content_length = int(self.headers['Content-Length'])
            body = self.rfile.read(content_length)
            data = json.loads(body)

            username = data.get('username')
            filename = data.get('filename')
            iv_hex = data.get('iv')

            # Convert IV from hex to bytes
            iv = bytes.fromhex(iv_hex) if iv_hex else None

            user_id = self.get_user_id(username)
            if user_id is not None:
                conn = sqlite3.connect(DATABASE_NAME)
                cursor = conn.cursor()

                try:
                    conn.execute('BEGIN')
                    cursor.execute('INSERT INTO file_encryption_iv (filename, iv, username) VALUES (?, ?,?)',
                               (filename, iv, username))
                    conn.commit()
                    self.send_response(200)
                    self.send_header('Content-type', 'text/html')
                    self.end_headers()
                    self.wfile.write(b"IV uploaded successfully!")

                except sqlite3.IntegrityError:
                    self.send_response(400)
                    self.send_header('Content-type', 'text/html')
                    self.end_headers()
                    self.wfile.write(b"IV already exists!")

                except Exception as e:
                    print(f"Database error: {e}")  # Log the error
                    self.send_response(500)
                    self.send_header('Content-type', 'text/html')
                    self.end_headers()
                    self.wfile.write(b"Internal Server Error")

                finally:
                    conn.close()
            else:
                self.send_response(401)
                self.send_header('Content-type', 'text/html')
                self.end_headers()
                self.wfile.write(b"User not found!")

        except Exception as e:
            print(f"Error handling IV upload: {e}")  # Log the error
            self.send_response(500)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Internal Server Error")

    def handle_file_upload(self):
        """Handle file upload."""
        try:
            content_length = int(self.headers['Content-Length'])
            file_data = self.rfile.read(content_length)

            filename = self.headers.get('Filename')
            username = self.headers.get('User-ID')

            user_id = self.get_user_id(username)  # Retrieve the user ID based on the username
            if user_id is not None:
                conn = sqlite3.connect(DATABASE_NAME)
                cursor = conn.cursor()

                try:
                    conn.execute('BEGIN')
                    # Insert the file, associating it with the user
                    cursor.execute('INSERT INTO files (filename, file_data, user_id) VALUES (?, ?, ?)',
                               (filename, file_data, user_id))
                    conn.commit()
                    self.send_response(200)
                    self.send_header('Content-type', 'text/html')
                    self.end_headers()
                    self.wfile.write(b"File uploaded successfully!")

                except sqlite3.IntegrityError:
                    self.send_response(400)
                    self.send_header('Content-type', 'text/html')
                    self.end_headers()
                    self.wfile.write(b"File already exists for this user! Please delete it and try again.")

                except Exception as e:
                    print(f"Database error: {e}")
                    self.send_response(500)
                    self.send_header('Content-type', 'text/html')
                    self.end_headers()
                    self.wfile.write(b"Internal Server Error")

                finally:
                    conn.close()

            else:
                self.send_response(401)
                self.send_header('Content-type', 'text/html')
                self.end_headers()
                self.wfile.write(b"User not found!")

        except Exception as e:
            print(f"Error handling file upload: {e}")
            self.send_response(500)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Internal Server Error")

    def handle_file_download(self):
        """Handle file download."""
        try:
            query = urllib.parse.urlparse(self.path).query
            params = urllib.parse.parse_qs(query)
            username = params.get('username', [None])[0]
            filename = params.get('filename', [None])[0]

            if not username or not filename:
                self.send_response(400)
                self.end_headers()
                self.wfile.write(b"Missing username or filename")
                return

            user_id = self.get_user_id(username)
            if user_id is None:
                self.send_response(401)
                self.send_header('Content-type', 'text/html')
                self.end_headers()
                self.wfile.write(b"User not found!")
                return

            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()

            cursor.execute('''SELECT f.file_data, fk.iv 
                            FROM files f
                            LEFT JOIN file_encryption_iv fk ON f.filename = fk.filename
                            WHERE f.filename = ? AND f.user_id = ? AND fk.username = ?''' , (filename, user_id, username))

            result = cursor.fetchone()
            conn.close()

            if not result:
                self.send_response(404)
                self.send_header('Content-type', 'text/html')
                self.end_headers()
                self.wfile.write(b"File not found or access denied!")
                return

            file_data, iv = result
            if iv is None:
                self.send_response(500)
                self.send_header('Content-type', 'text/html')
                self.end_headers()
                self.wfile.write(b"IV not found for this file!")
                return

            self.send_response(200)
            self.send_header('Content-type', 'application/octet-stream')
            self.send_header('Content-Disposition', f'attachment; filename="{filename}"')
            self.send_header('IV', iv.hex())
            self.end_headers()
            self.wfile.write(file_data)

        except Exception as e:
            print(f"Error during file download: {e}")
            self.send_response(500)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Internal Server Error")
    
    def handle_file_share(self):
        """Handle file sharing."""
        try:
            # Step 1: Read the raw request payload
            content_length = int(self.headers['Content-Length']) if 'Content-Length' in self.headers else 0
            if content_length == 0:
                self.send_response(400)  # Bad Request
                self.end_headers()
                self.wfile.write(b"Empty request body")
                return

            raw_body = self.rfile.read(content_length)

            # Step 2: Parse the JSON content
            try:
                payload = json.loads(raw_body)
            except json.JSONDecodeError:
                self.send_response(400)  # Bad Request
                self.end_headers()
                self.wfile.write(b"Invalid JSON payload")
                return

            # Step 3: Extract fields from the parsed JSON
            owner = payload.get('owner')
            filename = payload.get('filename')
            target_user = payload.get('target_user')
            encrypted_real_key = payload.get('encrypted_real_key')

            # Step 4: Validate required fields
            if not all([owner, filename, target_user, encrypted_real_key]):
                self.send_response(400)  # Bad Request
                self.end_headers()
                self.wfile.write(b"Missing required fields in JSON payload")
                return

            # Step 5: Interact with the database
            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()

            # Get file ID and user ID
            cursor.execute('SELECT id FROM files WHERE filename=? AND user_id=(SELECT id FROM users WHERE username=?)',
                           (filename, owner))
            result = cursor.fetchone()

            if not result:
                self.send_response(404)  # Not Found
                self.end_headers()
                self.wfile.write(b"File or user not found")
                return

            file_id = result[0]

            # Write in the shared_files table
            cursor.execute('INSERT INTO shared_files VALUES (?,?,?,?)',
                           (owner, file_id, target_user, encrypted_real_key))
            conn.commit()
            conn.close()

            # Step 6: Respond with success
            self.send_response(200)
            self.end_headers()
            self.wfile.write(b"Sharing success")

        except Exception as e:
            print(f"Sharing error: {e}")
            self.send_response(500)  # Internal Server Error
            self.end_headers()
            self.wfile.write(b"Internal server error")

    def handle_shared_files(self):
        """Retrieve the list of shared files for a user."""
        try:
            query = urllib.parse.urlparse(self.path).query
            params = urllib.parse.parse_qs(query)
            username = params.get('username', [None])[0]

            user_id = self.get_user_id(username)
            if not user_id:
                self.send_response(404)
                self.end_headers()
                return

            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()
            
            # Get the shared files
            cursor.execute('''
                SELECT sf.file_id, f.filename, sf.owner, sf.encrypted_real_key
                FROM shared_files sf JOIN files f ON sf.file_id = f.id
                WHERE sf.target_username = ?
            ''', (username,))

            shared_files = [
                {'file_id': row[0], 'file_name': row[1], 'owner': row[2], 'encrypted_real_key': row[3]}
                for row in cursor.fetchall()
            ]
            
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(shared_files).encode())
            
        except Exception as e:
            print(f"Error fetching shared files: {e}")
            self.send_response(500)
            self.end_headers()

        finally:
            conn.close()
    
    def handle_read_shared(self):
        """Handle reading a shared file."""
        try:
            user_id = self.get_user_id_from_cookie()  # Assuming there's a method to get user ID from a cookie
            params = parse_qs(urlparse(self.path).query)
            filename = params['filename'][0]
        
            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()
        
            # Verify access
            cursor.execute('''SELECT 1 FROM shared_files s 
                            JOIN files f ON s.file_id = f.id 
                            WHERE f.filename=? AND s.target_username=?''',
                          (filename, user_id))

            if not cursor.fetchone():
                self.send_response(403)  # Forbidden
                self.end_headers()
                return
            
            # Read file content
            cursor.execute('SELECT file_data FROM files WHERE filename=? AND user_id=?', (filename, user_id))
            file_data = cursor.fetchone()

            if file_data:
                self.send_response(200)
                self.send_header('Content-type', 'text/plain')
                self.end_headers()
                self.wfile.write(file_data[0])  # Send the file content
            else:
                self.send_response(404)  # Not Found

        except FileNotFoundError:
            self.send_response(404)  # File not found

        except Exception as e:
            print(f"Read shared error: {e}")
            self.send_response(500)  # Internal Server Error
    
    def get_user_id(self, username):
        """Retrieve user ID based on the username."""
        print(f"Looking up user ID for username: {username}")
        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('SELECT id FROM users WHERE LOWER(username) = LOWER(?)', (username,))
        result = cursor.fetchone()
        conn.close()
        print(f"Retrieved user ID: {result[0] if result else 'None'}")
        return result[0] if result else None

    def handle_reset_password(self):
        """Handle password reset for a user."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]
        new_hashed_password = params.get('new_password', [None])[0]

        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()

        try:
            # Update the password in the database
            cursor.execute('UPDATE users SET password = ? WHERE username = ?', (new_hashed_password, username))
            if cursor.rowcount == 0:
                self.send_response(404)
                self.send_header('Content-type', 'text/html')
                self.end_headers()
                self.wfile.write(b"User not found.")
                return

            conn.commit()

            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Password reset successful!")

        except Exception as e:
            print(f"Database error: {e}")
            self.send_response(500)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"Internal server error")

        finally:
            conn.close()

    def handle_list_files(self):

        try:
            print(f"Incoming request path: {self.path}")
            query = urllib.parse.urlparse(self.path).query
            params = urllib.parse.parse_qs(query)
            username = params.get('username', [None])[0]
            print(f"Parsed username: {username}")

            user_id = self.get_user_id(username)
            if user_id is None:
                self.send_response(404)
                self.end_headers()
                self.wfile.write(b"User not found!")
                return

            conn = sqlite3.connect(DATABASE_NAME)
            cursor = conn.cursor()
            cursor.execute('SELECT id, filename, user_id FROM files WHERE user_id = ?', (user_id,))
            files = cursor.fetchall()
            conn.close()

            response_data = [{'id': file[0], 'filename': file[1], 'user_id': file[2]} for file in files]
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(response_data).encode())

        except Exception as e:
            print(f"Error handling request: {e}")
            self.send_response(500)
            self.end_headers()
            self.wfile.write(b"Internal Server Error")

    def handle_file_delete(self):
        """Handle file deletion."""
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        username = params.get('username', [None])[0]
        filename = params.get('filename', [None])[0]

        user_id = self.get_user_id(username)
        if user_id is None:
            self.send_response(401)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"User not found!")
            return

        conn = sqlite3.connect(DATABASE_NAME)
        cursor = conn.cursor()
        cursor.execute('DELETE FROM files WHERE filename = ? AND user_id = ?', (filename, user_id))
        cursor.execute('DELETE FROM file_encryption_iv WHERE filename = ? AND username=?', (filename, username))
        conn.commit()
        conn.close()

        if cursor.rowcount > 0:
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"File deleted successfully.")
        else:
            self.send_response(404)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b"File not found!")

# Initialize the database
init_db()

# Specify the port and create the server
PORT = 8000
with socketserver.TCPServer(("", PORT), MyHandler) as httpd:
    print(f"Serving at port {PORT}")
    httpd.serve_forever()