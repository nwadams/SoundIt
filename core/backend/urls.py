from django.conf.urls import patterns, url

from backend import views

urlpatterns = patterns('',

    # ex: /signUp?device_id=id
    url(r'^signUp/$', views.signUp, name='signUp'),
    url(r'^register/$', views.register, name='register'),

    # ex: /login?user_id=id&password=pwd
    # Needs to be deprecated for now.
    # url(r'^login/$', views.login, name='login'),
    
    url(r'^getLocations/$', views.getLocations, name='getLocations'),
    
    # ex: /addToPlaylist?user_id=id&location_id=id&music_track_id=id
    url(r'^addToPlaylist/$', views.addToPlaylist, name='addToPlaylist'),

    # ex: /voteUp?device_id=id&location_id=id&music_track_id=id
    url(r'^voteUpAndroid/$', views.voteUpAndroid, name='voteUp'),
    
    # ex: /voteUp?device_id=id&location_id=id&music_track_id=id
    url(r'^voteUp/$', views.voteUp, name='voteUp'),
    
    # ex: /refreshPlaylist?user_id=id&location_id=id
    url(r'^refreshPlaylist/$', views.refreshPlaylist, name='refreshPlaylist'),
    url(r'^refreshPlaylistAndroid/$', views.refreshPlaylistAndroid, name='refreshPlaylistAndroid'),
    url(r'^refreshPlaylistiOS/$', views.refreshPlaylistiOS, name='refreshPlaylistiOS'),

    
    # ex: /getLibrary?device_id=id&location_id=id
    url(r'^getLibrary/$', views.getLibrary, name='getLibrary'),
    
    # ex: /getVoteHistory?device_id=id&location_id=id
    url(r'^getVoteHistory/$', views.getVoteHistory, name='getVoteHistory'),
    
    url(r'^venue/getNextSong/$', views.venueGetNextSong, name='venueGetNextSong'),
    
    url(r'^venue/testing/$', views.testing, name='venueTesting'),
)
