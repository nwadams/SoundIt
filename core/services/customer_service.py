'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import Customer 
import hashlib
import random

class CustomerService:
    
    def login(self, device_id, password):
        try:
            customer = Customer.objects.get(device_id=device_id)
        except Customer.DoesNotExist:
            return False
        # this might be redundant. Verify.
        if customer is None:
            return False
        hashed_password = hashlib.md5(customer.salt + password).hexdigest()
        return hashed_password == customer.password

    def register(self, device_id, password):
        # TODO: Improve password salting
        salt = str(random.randint(10,99))
        hashed_password = hashlib.md5(salt + password).hexdigest()
        new_customer = Customer(device_id=device_id, password=hashed_password, salt=salt)
        new_customer.save()
        return new_customer
    
    def checkIfCurrentCustomer(self, device_id):
        try: 
            Customer.objects.get(device_id = device_id)
        except (KeyError, Customer.DoesNotExist):
            return False
        return True