import sys
import pyrebase

config = {
  "apiKey": "AIzaSyBE7KQui8SCGpxPA6AOMdcebh87X_390-s",
  "authDomain": "smartdoorbell-9ecf1.firebaseapp.com",
  "databaseURL": "https://smartdoorbell-9ecf1.firebaseio.com/",
  "storageBucket": "smartdoorbell-9ecf1.appspot.com"
}

firebase = pyrebase.initialize_app(config)
storage = firebase.storage()

# as admin
storage.child(sys.argv[1]).put(sys.argv[1])
print ("File upload success")
