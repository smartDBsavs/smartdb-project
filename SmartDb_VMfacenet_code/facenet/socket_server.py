import socket
import os
import subprocess
import sys
import json

def server_program():

    # get the hostname
    host = socket.gethostname()
    port = 5000  # initiate port no above 1024

    server_socket = socket.socket()  # get instance
    # look closely. The bind() function takes tuple as argument
    server_socket.bind(('', port))  # bind host address and port together

    # configure how many client the server can listen simultaneously
    server_socket.listen(2)
    conn, address = server_socket.accept()  # accept new connection
    print("Connection from: " + str(address))
    #while True:
        # receive data stream. it won't accept data packet greater than 1024 bytes
    data = conn.recv(1024).decode()
    if not data:
    	print("not gadbad")
            # if data is not received break
    print("from connected user: " + str(data))

    s2_out = subprocess.check_output([sys.executable, "facenet.py", data])
    print ("Script1 Run...")
    nw=s2_out.decode("utf-8")[:]
    a,b = nw.split(',')
    print(a)
    print ("Script1 ends...")
    data = a
    conn.send(data.encode())  # send data to the client
    conn.close()  # close the connection

if __name__ == '__main__':
    print("Server Ready")
    server_program()