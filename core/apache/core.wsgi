import os
import sys

#path = '/home/nwadams/django'
#if path not in sys.path:
#    sys.path.insert(0, '/home/nwadams/django')
sys.path.append('/root/prod/SoundIt/core')
sys.path.append('/root/prod/SoundIt/core/core')

os.environ['DJANGO_SETTINGS_MODULE'] = 'core.settings'

import django.core.handlers.wsgi
application = django.core.handlers.wsgi.WSGIHandler()

