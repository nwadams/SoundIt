'''
Created on Jan 21, 2013

@author: anuj
'''

class TrackAlreadyInPlaylistException(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)