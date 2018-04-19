import pyrebase
from pyfcm import FCMNotification
import time
import sys
config = {
  "apiKey": "apiKey",
  "authDomain": "smartdoorbell-9ecf1.firebaseapp.com",
  "databaseURL": "https://smartdoorbell-9ecf1.firebaseio.com/",
  "storageBucket": "smartdoorbell-9ecf1.appspot.com"
}
push_service = FCMNotification(api_key="AAAAsatN4b8:APA91bG-6nTsznUvEhlTcoSwsbOPKxD_7nprD6S6jbanL3kGTbHpPkHnthIJzeoKWeVcQMIKUL5BEpDS8PnyTX5Y97-T9DCCOHqS6eSuU0pDHXsto98AUeQuwc4BglKrMUqKPpego0ho")
firebase = pyrebase.initialize_app(config)

db = firebase.database()
storage = firebase.storage()
url = storage.child("photos/" + sys.argv[1] + ".jpg").get_url("none")

regid = [];
all_users = db.child("Registration_Details").get()
for user in all_users.each():
    #print(user.key())
    #regid = regid + '"' + (user.val().get("registrationId")) + '"' + ',';
    regid.append(user.val().get("registrationId"))

#regid = regid[:-1]
#regid = "[" + regid + "]"
print (regid)

#Notification code
data_message = {
    "title" : "Hello! Someone is at the Door",
    "message" : sys.argv[1],
    "label" : "Unknown",
    "url" : url,
    "status":"0",
    "btnStatus":"Allow/DisAllow",
    "action" : "notification"
}

registration_ids = regid
message_title = "SmartDB Alert"
message_body = "Hello! Someone is at the Door"
result = push_service.notify_multiple_devices(registration_ids=registration_ids, message_title=message_title, message_body=message_body,data_message=data_message)
print(result)





