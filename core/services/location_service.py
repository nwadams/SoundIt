'''
Created on Mar 2, 2013

@author: Nick
'''
import logging
from backend.models import LocationMap
from backend.models import Location
import datetime


logger = logging.getLogger('core.backend')

class LocationService:
    
    def checkIn(self, consumer, location):
        try: 
            location_map = LocationMap.objects.get(consumer=consumer, is_active=True)
            self.checkOut(consumer)
        except (KeyError, LocationMap.DoesNotExist):
            pass
            
        location_map = LocationMap(consumer = consumer, location = location)
        
        location_map.is_active = True
        
        location_map.save()
        
        return location_map
    

    def checkOut(self, consumer):
        location_map = None
        try: 
            location_map = LocationMap.objects.get(consumer=consumer, is_active=True)
        except (KeyError, LocationMap.DoesNotExist):
            logger.warn("not checked in")
            return
            
        location_map.is_active = False
        location_map.check_out_time = datetime.datetime.now()
        
        location_map.save()