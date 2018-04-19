import socket
import time
import sys
import pyrebase

def client_program():
    host = socket.gethostname()  # as both code is running on same pc
    port = 5000  # socket server port number

    client_socket = socket.socket()  # instantiate
    client_socket.connect(('192.168.1.12', port))  # connect to the server

   
    message = sys.argv[1] # take input

    #while message.lower().strip() != 'bye':
    print(message)
    client_socket.send(message.encode())  # send message
    data = client_socket.recv(1024).decode()  # receive response
    
    config = {
    "apiKey": "apiKey",
    "authDomain": "smartdoorbell-9ecf1.firebaseapp.com",
    "databaseURL": "https://smartdoorbell-9ecf1.firebaseio.com/",
    "storageBucket": "smartdoorbell-9ecf1.appspot.com"
    }
    firebase = pyrebase.initialize_app(config)
    db = firebase.database()
    regid = []
    label = data
    print(data)
    all_users = db.child("Logs_Details").get()
    for user in all_users.each():
        check=user.val().get("dateTime")
        if message == check:
            db.child("Logs_Details").child(user.key()).update({"label":label})
            break
    print ("Label" + label)
    
    print('Received from server: ' + data)  # show in terminal 

    #message = input(" -> ")  # again take input
    if not data:
       print('gadbad')
    client_socket.close()  # close the connection


if __name__ == '__main__':
    client_program()