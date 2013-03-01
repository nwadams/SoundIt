'''
Created on Feb 26, 2013

@author: Nick
'''

from backend.models import Consumer 
import hashlib
import random
import logging
import requests

logger = logging.getLogger('core.backend')

class ConsumerService:
    
    def login(self, device_id, password):
        try:
            customer = Consumer.objects.get(device_id=device_id)
        except Consumer.DoesNotExist:
            logger.warning("Could not find customer with device " + str(device_id))
            return False
        # this is probably redundant. Verify.
        if customer is None:
            logger.warning("Could not find customer with device " + str(device_id))
            return False
        hashed_password = hashlib.md5(customer.salt + password).hexdigest()
        return hashed_password == customer.password

    def register(self, device_id, password):
        # TODO: Improve password salting
        logger.debug("Registering customer with device " + str(device_id))
        salt = str(random.randint(10,99))
        hashed_password = hashlib.md5(salt + password).hexdigest()
        new_customer = Consumer(device_id=device_id, password=hashed_password, salt=salt)
        new_customer.save()
        return new_customer
    
    def register_with_token(self, device_id, auth_token, token_type):
        
        consumer = self.checkIfCurrentConsumer(device_id)
        
        if consumer is not None:
            return consumer
            
        consumer = Consumer()
        if token_type.ascii_lowercase == 'google':
            r = requests.get('https://www.googleapis.com/oauth2/v1/userinfo', {'access_token',auth_token})
            
        
        consumer.save()
        
        return consumer
        
    
    def checkIfCurrentConsumer(self, device_id):
        logger.debug("Checking if user with device id " + str(device_id) + " already exists.")
        try: 
            return Consumer.objects.get(device_id = device_id)
        except (KeyError, Consumer.DoesNotExist):
            return None