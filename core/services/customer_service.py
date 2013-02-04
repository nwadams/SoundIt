'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import Customer 
import hashlib
import random
import logging

logger = logging.getLogger('core.backend')

class CustomerService:
    
    def login(self, device_id, password):
        try:
            customer = Customer.objects.get(device_id=device_id)
        except Customer.DoesNotExist:
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
        new_customer = Customer(device_id=device_id, password=hashed_password, salt=salt)
        new_customer.save()
        return new_customer
    
    def checkIfCurrentCustomer(self, device_id):
        logger.debug("Checking if user with device id " + str(device_id) + " already exists.")
        try: 
            Customer.objects.get(device_id = device_id)
        except (KeyError, Customer.DoesNotExist):
            return False
        return True