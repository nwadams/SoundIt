from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
import logging
from xcptions.Errors import PlaylistNotFoundError


logger = logging.getLogger('core.backend')

class VenueService:

    def updateCurrentPlaying(self, location_id):
        playlist = Playlist.objects.get(location_id = location_id, is_active=True)
        try :
            current_playing = PlaylistItem.objects.get(playlist_id = playlist.id, item_state=1)
            current_playing.item_state=0
            current_playing.save()
        except:
            logger.warn('no song at location_id ' + location_id + ' with item state 1')
        
        try:  
            new_song_list = PlaylistItem.objects.filter(playlist_id = playlist.id, item_state=2).order_by("-votes")
            new_song = new_song_list[0]
            
            if not new_song:
                default = PlaylistItem()
                default.music_track = MusicTrack()
                default.music_track.name = "false"
                return default
            
            num_votes = new_song.votes
            for song in new_song_list:
                if song.votes == num_votes:
                    if new_song.music_track.name > song.music_track.name:
                        new_song = song
                else:
                    break
            
            new_song.item_state = 1
            new_song.save()    
            logger.debug("setting new current song" + str(new_song.pk))
            return new_song
        except:
            raise PlaylistNotFoundError(new_song_list)

    