import socket
# Remember to change the SERVER_HOST for client program also before running
SERVER_HOST = '172.16.141.128'
SERVER_PORT = 80

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect with server
client_socket.connect((SERVER_HOST, SERVER_PORT))

# Ask for input command
request = input('Input HTTP request command:\n')
client_socket.send(request.encode())
print('Server response:\n')

# Receive responses and print them out one line by one line
BUFFER_SIZE = 1024  
response_chunk = client_socket.recv(BUFFER_SIZE)
while response_chunk:
    # Decode the bytes to a string for images and print line by line
    response_lines = response_chunk.decode('utf-8', 'ignore').splitlines()
    for line in response_lines:
        print(line)
    response_chunk = client_socket.recv(BUFFER_SIZE)

# Close client connection after receiving response
client_socket.close()
