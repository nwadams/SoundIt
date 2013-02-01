'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import User 
import hashlib
import random

class UserService:
    
    def login(self, device_id, password):
        user = User.objects.get(device_id=device_id)
        if user is None:
            return False
        hashed_password = hashlib.md5(user.salt + password).hexdigest()
        return hashed_password == user.password

    def register(self, device_id, password):
        salt = str(random.randint(10,99))
        hashed_password = hashlib.md5(salt + password).hexdigest()
        new_user = User(device_id=device_id, password=hashed_password, salt=salt)
        new_user.save()
        return new_user
    
    def checkIfCurrentUser(self, device_id):
        try: 
            User.objects.get(device_id = device_id)
        except (KeyError, User.DoesNotExist):
            return False
        return True