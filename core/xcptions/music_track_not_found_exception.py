'''
Created on Feb 1, 2013

@author: anuj
'''

class MusicTrackNotFoundError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)