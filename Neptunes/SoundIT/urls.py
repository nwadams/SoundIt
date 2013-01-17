from django.conf.urls import patterns, url

from SoundIT import views 

urlpatterns = patterns('',
    # ex: /soundit/
    url(r'^$', views.index, name='index'),
    # ex: /soundit/
    
)
