import pyrebase
from pyfcm import FCMNotification
import RPi.GPIO as GPIO
import time
import sys
config = {
  "apiKey": "apiKey",
  "authDomain": "smartdoorbell-9ecf1.firebaseapp.com",
  "databaseURL": "https://smartdoorbell-9ecf1.firebaseio.com/",
  "storageBucket": "smartdoorbell-9ecf1.appspot.com"
}
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(18,GPIO.OUT)
push_service = FCMNotification(api_key="AAAAsatN4b8:APA91bG-6nTsznUvEhlTcoSwsbOPKxD_7nprD6S6jbanL3kGTbHpPkHnthIJzeoKWeVcQMIKUL5BEpDS8PnyTX5Y97-T9DCCOHqS6eSuU0pDHXsto98AUeQuwc4BglKrMUqKPpego0ho")
firebase = pyrebase.initialize_app(config)
now=sys.argv[1]
db = firebase.database()
regid = []
status='0'
all_users = db.child("Logs_Details").get()
for user in all_users.each():
    check=user.val().get("dateTime")
    if now == check:
        status=user.val().get("status")
        if status=='1':
            GPIO.output(18,GPIO.LOW)
            time.sleep(10)
            GPIO.output(18,GPIO.HIGH)
        break
print (status)
