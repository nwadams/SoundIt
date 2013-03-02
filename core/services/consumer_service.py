'''
Created on Jan 21, 2013

@author: anuj
'''
from backend.models import Consumer
import hashlib
import random
import logging

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

    def register(self, device_id, password, email_address):
        consumer = self.checkIfCurrentUser(device_id)
        
        if consumer is None: 
            logger.debug("Registering customer with device " + str(device_id))
            consumer = Consumer(device_id=device_id)
        
        api_token = hashlib.md5(device_id).hexdigest()

        salt = str(random.randint(10,99))
        hashed_password = hashlib.md5(salt + password).hexdigest()
        
        consumer.api_token = api_token
        consumer.email_address = email_address
        consumer.salt = salt
        consumer.password = hashed_password
        
        consumer.save()
        return consumer
    
    def register_with_token(self, device_id, auth_token, token_type):
        consumer = None
        
        return consumer
    
    def checkIfCurrentUser(self, device_id):
        logger.debug("Checking if user with device id " + str(device_id) + " already exists.")
        consumer = None
        try: 
            consumer = Consumer.objects.get(device_id = device_id)
        except (KeyError, Consumer.DoesNotExist):
            return consumer
        return consumer