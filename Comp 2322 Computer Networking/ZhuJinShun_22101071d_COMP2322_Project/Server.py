import socket
import os
import time
from datetime import datetime, timezone

# REMEMBER TO CHANGE THE SERVER HOST INTO THE IP ADDRESS OF THE WLAN USER IS GOING TO USE 
SERVER_HOST = '172.16.141.128'  # The server's host IP address（In this case, is the IP address for POLYU WLAN in HUNG HOM HALL）
SERVER_PORT = 80  # The server's port number
LOG_FILE = 'server_log.txt'  # The name of the log file created containing the records

def log_request(client_ip, access_time, filename, content_type):
    # Logs information about each request to the log file
    record = 'Client: {}\nAccess Time: {}\nRequested File: {}\nResponse Type: {}\n'.format(
        client_ip, access_time, filename, content_type)
    with open(LOG_FILE, 'a') as log_file:
        log_file.write(record + '\n')

def get_file_last_modified(filename):
    # Retrieves the last modified timestamp of a file and converts it to a formatted string
    timestamp = os.path.getmtime(filename)
    last_modified = datetime.fromtimestamp(timestamp, tz=timezone.utc)
    return last_modified.strftime('%a, %d %b %Y %H:%M:%S GMT')
                                  
# Create a TCP socket and bind it to the specified host and port
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_socket.bind((SERVER_HOST, SERVER_PORT))
server_socket.listen(1)
print("Listening on port %s ..." % SERVER_PORT)

while True:
    # Accept client connections
    client_connection, client_address = server_socket.accept()
    print("The client has successfully connected to the server")

    # Receive the request from the client
    request = client_connection.recv(1024).decode()
    print(request)

    # Getting inputted values and store according to its order
    headers = request.split('\n')
    fields = headers[0].split()
    request_type = fields[0]
    filename = fields[1]
    client_ip = client_address[0]
    access_time = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())

    #GET command request
    if request_type == 'GET':
        if filename == '/':
            filename = '/index.html'
        try:
            # Read the files and check in 'files' folder or not
            file_path = 'files' + filename
            fin = open(file_path, 'rb')
            content = fin.read()
            fin.close()

            content_type = 'null'
            
            # Determine the content type based on the file extension
            if filename.endswith('.jpg') or filename.endswith('.jpeg'):
                content_type = 'image/jpeg'
            elif filename.endswith('.png'):
                content_type = 'image/png'
            elif filename.endswith('.gif'):
                content_type = 'image/gif'
            elif filename.endswith('.html'):
                content_type = 'text/html'
            elif filename.endswith('.txt'):
                content_type = 'text/txt'
                
            # Update the latest modified timestamp of the file
            file_latest_modified = get_file_last_modified(file_path)
            
            if 'If-Modified-Since' in headers:
                # Check if the file has not been modified since the specified date
                requested_date = headers[headers.index('If-Modified-Since')].split(': ')[1].strip()
                requested_last_modified = datetime.strptime(requested_date, '%a, %d %b %Y %H:%M:%S %Z')
                file_latest_modified = datetime.strptime(get_file_last_modified(file_path), '%a, %d %b %Y %H:%M:%S %Z')

                if file_latest_modified <= requested_last_modified:
                    # Send a 304 Not Modified response
                    response = 'HTTP/1.1 304 Not Modified\n\n'
                    client_connection.sendall(response.encode())
                    client_connection.close()
                    continue
            
            response_headers = 'HTTP/1.1 200 OK\nContent-Type: {}\nContent-Length: {}\nLast-Modified: {}\n\n'.format(
                content_type, os.path.getsize(file_path), access_time).encode()

            # Send back headers and contents for GET command
            response_message = response_headers + content
            client_connection.sendall(response_message)

            # Log the request
            log_request(client_ip, access_time, filename, content_type)

        except FileNotFoundError:
            # Send a 404 Not Found response
            response = 'HTTP/1.1 404 NotFound\n\n404 Not Found'.encode()
            client_connection.sendall(response)

    #HEAD command request
    elif request_type == 'HEAD':
        if filename == '/':
            filename = '/index.html'
        try:
            # Read the files and check in 'files' folder or not
            file_path = 'files' + filename
            fin = open(file_path, 'rb')
            fin.close()

            content_type = 'null'
            
            # Determine the content type based on the file extension
            if filename.endswith('.jpg') or filename.endswith('.jpeg'):
                content_type = 'image/jpeg'
            elif filename.endswith('.png'):
                content_type = 'image/png'
            elif filename.endswith('.gif'):
                content_type = 'image/gif'
            elif filename.endswith('.html'):
                content_type = 'text/html'
                
            # Update the latest modified timestamp of the file
            file_latest_modified = get_file_last_modified(file_path)
            
            if 'If-Modified-Since' in headers:
                # Check if the file has not been modified since the specified date
                requested_date = headers[headers.index('If-Modified-Since')].split(': ')[1].strip()
                requested_last_modified = datetime.strptime(requested_date, '%a, %d %b %Y %H:%M:%S %Z')
                file_latest_modified = datetime.strptime(get_file_last_modified(file_path), '%a, %d %b %Y %H:%M:%S %Z')

                if file_latest_modified <= requested_last_modified:
                    # Send a 304 Not Modified response
                    response = 'HTTP/1.1 304 Not Modified\n\n'
                    client_connection.sendall(response.encode())
                    client_connection.close()
                    continue
            
            # Construct the response with headers and file content
            response_headers = 'HTTP/1.1 200 OK\nContent-Type: {}\nContent-Length: {}\nLast-Modified: {}\n\n'.format(
                content_type, os.path.getsize(file_path), access_time).encode()

            # Construct the response with headers
            client_connection.sendall(response_headers)
            # Log the request
            log_request(client_ip, access_time, filename, content_type)

        except FileNotFoundError:
            # Send a 404 Not Found response
            response = 'HTTP/1.1 404 Not Found\n\n404 Not Found'.encode()
            client_connection.sendall(response)
    else:
        # Send a 400 Bad Request response
        response = 'HTTP/1.1 400 Bad Request\n\n400 Bad Request Not Supported'.encode()
        client_connection.sendall(response)

    client_connection.close()
    
    #Continue or not function to let user choose to continue or quit/ Persistent Connection (Keep-Alive)
    user_input = input("Do you want to continue? (Y/N): ")
    if user_input.upper() == 'N':
        break

#Exit the server/ non-persistent connection (close) when N is entered
server_socket.close()
print("The server has successfully closed")
