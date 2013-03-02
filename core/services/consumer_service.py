'''
Created on Jan 21, 2013

@author: anuj
'''
from backend.models import Consumer
from xcptions.Errors import InvalidAuthTokenError
from xcptions.Errors import InvalidAuthTokenTypeError
import hashlib
import random
import logging
import requests

logger = logging.getLogger('core.backend')

class ConsumerService:
    
    def login(self, device_id, api_token):
        try:
            customer = Consumer.objects.get(device_id=device_id)
        except Consumer.DoesNotExist:
            logger.warning("Could not find customer with device " + str(device_id))
            return False
        # this is probably redundant. Verify.
        if customer is None:
            logger.warning("Could not find customer with device " + str(device_id))
            return False
        hashed_password = hashlib.md5(customer.salt + api_token).hexdigest()
        return hashed_password == customer.password

    def register(self, device_id, password, email_address, name):
        consumer = self.__checkIfCurrentUser__(device_id)
        
        if consumer is None: 
            logger.debug("Registering customer with device " + str(device_id))
            consumer = Consumer(device_id=device_id)
        
        api_token = hashlib.md5(device_id).hexdigest()

        salt = str(random.randint(10,99))
        hashed_password = hashlib.md5(salt + password).hexdigest()
        
        consumer.name = name
        consumer.api_token = api_token
        consumer.email_address = email_address
        consumer.salt = salt
        consumer.password = hashed_password
        
        consumer.save()
        return consumer
    
    def register_with_token(self, device_id, auth_token, token_type, facebook_id):
        consumer = self.__checkIfCurrentUser__(device_id)
        
        if consumer is None: 
            logger.debug("Registering customer with device " + str(device_id))
            consumer = Consumer(device_id=device_id)
                     
        if token_type.lower() == 'google':
            logger.debug("Registering customer with google auth token")
            r = requests.get('https://www.googleapis.com/oauth2/v1/userinfo?access_token=' + auth_token)
            
            if r.status_code == 200 :
                json = r.json()
                consumer.name = json.get('name', '')
                consumer.email_address = json.get('email', '')
                consumer.google_id = json.get('id', '')                   
            else :
                raise InvalidAuthTokenError(auth_token)
        elif token_type.lower() == 'facebook':
            logger.debug("Registering customer with facebook auth token")
            r = requests.get('https://graph.facebook.com/' + facebook_id + '?access_token=' + auth_token)
            
            if r.status_code == 200 :
                json = r.json()
                consumer.name = json.get('name', '')
                consumer.email_address = json.get('email', '')
                consumer.facebook_id = json.get('id', '')                   
            else :
                raise InvalidAuthTokenError(auth_token)
        else:
            raise InvalidAuthTokenTypeError(token_type)

        api_token = hashlib.md5(device_id).hexdigest()

        salt = str(random.randint(10,99))
        hashed_password = hashlib.md5(salt + str(random.randint(10,1000))).hexdigest()
        
        consumer.api_token = api_token
        consumer.salt = salt
        consumer.password = hashed_password
        
        consumer.save()
        return consumer
        
            
    def __checkIfCurrentUser__(self, device_id):
        logger.debug("Checking if user with device id " + str(device_id) + " already exists.")
        consumer = None
        try: 
            consumer = Consumer.objects.get(device_id = device_id)
        except (KeyError, Consumer.DoesNotExist):
            return consumer
        return consumer