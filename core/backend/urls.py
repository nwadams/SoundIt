from django.conf.urls import patterns, url

from backend import views

urlpatterns = patterns('',

    # ex: /signUp?device_id=id
    url(r'^signUp/$', views.signUp, name='signUp'),

    # ex: /login?user_id=id&password=pwd
    # Needs to be deprecated for now.
    url(r'^login/$', views.login, name='login'),
    
    url(r'^getLocations/$', views.getLocations, name='getLocation'),
    
    url(r'^getCategories/$', views.getCategories, name='getCategories'),
    
    url(r'^checkIn/$', views.checkInLocation, name='checkInLocation'),
    
    url(r'^checkOut/$', views.checkOutLocation, name='checkOutLocation'),
    
    # ex: /addToPlaylist?user_id=id&location_id=id&music_track_id=id
    url(r'^addToPlaylist/$', views.addToPlaylist, name='addToPlaylist'),
    
    # ex: /voteUp?device_id=id&location_id=id&music_track_id=id
    url(r'^voteUp/$', views.voteUp, name='voteUp'),
    
    # ex: /refreshPlaylist?user_id=id&location_id=id
    url(r'^refreshPlaylist/$', views.refreshPlaylist, name='refreshPlaylist'),
   
    # ex: /getLibrary?device_id=id&location_id=id
    url(r'^getSongLibrary/$', views.getSongLibrary, name='getSongLibrary'),
    
    url(r'^venue/getNextSong/$', views.venueGetNextSong, name='venueGetNextSong'),
    
    url(r'^venue/getCurrentSong/$', views.venueGetCurrentSong, name='venueGetCurrentSong'),
    
    url(r'^venue/index.html', views.index, name='venueTesting'),
    
    url(r'^getFormattedLibrary/$', views.getFormattedLibrary, name='getFormattedLibrary'),
    
    url(r'^refreshPlaylistFormatted/$', views.refreshPlaylistFormatted, name='getLibraryFormatted'),
)
