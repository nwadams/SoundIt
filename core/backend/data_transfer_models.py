from django.db import models
from django.utils import simplejson
from backend.models import Playlist
from backend.models import MusicTrack
from backend.models import Customer

PLAYLIST_ITEM_STATUS = (
                        (0, 'PLAYED'),
                        (1, 'PLAYING'),
                        (2, 'TO_BE_PLAYED'),
                        )

class PlaylistItemVotes(models.Model):
    playlist = models.ForeignKey(Playlist)
    music_track = models.ForeignKey(MusicTrack)
    votes = models.IntegerField(default=0)
    rank_played = models.IntegerField()
    current_ranking = models.IntegerField()
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    item_state = models.IntegerField(PLAYLIST_ITEM_STATUS)
    is_voted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "PlaylistItemVotes{" + \
    "id=" + str(self.pk) + \
    ", playlist='" + str(self.playlist) + '\'' + \
    ", music_track='" + str(self.music_track) + '\'' + \
    ", customer='" + str(self.customer) + '\'' + \
    ", is_voted='" + str(self.is_voted) + '\'' + \
    ", votes='" + str(self.votes) + "\'" + \
    ", rank_played=" + str(self.rank_played) + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"

    def toDict(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"playlist": self.playlist})
        result.append({"customer": self.customer})
        result.append({"is_voted": self.is_voted})
        result.append({"music_track": self.music_track})
        result.append({"votes": self.votes})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)