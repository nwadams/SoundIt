from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
import logging

logger = logging.getLogger('core.backend')

class VenueService:

    def updateCurrentPlaying(self):
        playlist = Playlist.objects.get(location_id = 1)
        try :
            current_playing = PlaylistItem.objects.get(playlist_id = playlist.id, item_state=1)
            current_playing.item_state=0
            current_playing.save()
            new_song = PlaylistItem.objects.filter(playlist_id = playlist.id, item_state=2).order_by("-votes")[0]
            new_song.item_state=1
            new_song.save()
            logger.debug("setting new current song" + str(new_song.pk))
            return new_song
        except:
            default = PlaylistItem()
            default.music_track = MusicTrack()
            default.music_track.name = "false"
            return default
    