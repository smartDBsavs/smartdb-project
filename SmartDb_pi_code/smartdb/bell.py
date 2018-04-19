import RPi.GPIO as GPIO
import time
import os
import glob
#GPIO SETUP
GPIO.setmode(GPIO.BCM)
Button = 7
n = 1
GPIO.setup(Button,GPIO.IN)
#loop
print("Program Running")
while 1 == 1:#loops forever till keyboard interupt (ctr + C) 
  if GPIO.input(Button) == False:#when button pressed:
    print("Button Pressed")
    
    #    ------|    photo & Bell    |------ #
    #Get FileName
    now = time.strftime("Date%m-%d-%yTime%H-%M-%S")
    #Make command to run OCMMDS
    command = "bash oscmds.sh " +  str(now)

    #run command
    os.system(command)
    #diagnostics
    print("Filename:", now)

    # ----| Pyrebase     |---- #
    print("Pyrebase")
    pyrecommand = 'python3 firebase_upload.py ' + ' "photos/' + now + '.jpg"'
    os.system(pyrecommand)

    # ----| Pyrebase     |---- #
    print("FCM notification")
    fcmcommand = 'python3 fcm_notify.py ' + now
    os.system(fcmcommand)

    time.sleep(10)
    # ----| Gate    |---- #
    print("Gate")
    pyrecommand = 'python3 gate_operate.py ' + now
    
    os.system(pyrecommand)
    

    # ----| Socket call by running client file     |---- #
    print("Socket")
    pyrecommand = 'python3 socket_client.py ' + now
    os.system(pyrecommand)

   
    # -- End Diagnostic Info
    print("Done Process")
    #-space out for next "Press of Button"
    print("")
    print("")
