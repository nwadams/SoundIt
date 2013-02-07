class InvalidDeviceError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class LocationNotFoundError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class MusicTrackNotFoundError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class PlaylistNotFoundError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class UnableToAddMusicError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class TrackAlreadyInPlaylistError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class UnableToVoteError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class UserNotFoundError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    
class UnableToGetVoteHistoryError(Exception):
    
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)