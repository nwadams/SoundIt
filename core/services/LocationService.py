'''
Created on Mar 2, 2013

@author: Nick
'''
import logging
from backend.models import LocationMap


logger = logging.getLogger('core.backend')

class LocationService:
    
    def checkIn(self, consumer, location):
        location_map = LocationMap(consumer = consumer, location = location)
        
        location_map.is_active = True
        
        location_map.save()
        
        return location_map