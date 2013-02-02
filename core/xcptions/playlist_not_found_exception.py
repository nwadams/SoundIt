'''
Created on Jan 22, 2013

@author: anuj
'''

class PlaylistNotFoundError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)