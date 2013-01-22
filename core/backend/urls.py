from django.conf.urls import patterns, url

from backend import views

urlpatterns = patterns('',

    # ex: /signUp?device_id=id
    url(r'^signUp/$', views.signUp, name='signUp'),

    # ex: /login?user_id=id&password=pwd
    url(r'^login/$', views.login, name='login'),
    
    # ex: /addToPlaylist/<location_id>?user_id=id&track_id=id
    url(r'^addToPlaylist/(?P<location_id>\d+)/$', views.addToPlaylist, name='addToPlaylist'),

    # ex: /voteUp/<location_id>?user_id=id&track_id=id
    url(r'^voteUp/(?P<location_id>\d+)/$', views.voteUp, name='voteUp'),

)
