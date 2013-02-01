import os
import sys

sys.path.append('/root/prod/SoundIt/core')
sys.path.append('/root/prod/SoundIt/core/core')

os.environ['DJANGO_SETTINGS_MODULE'] = 'core.settings'

import django.core.handlers.wsgi
application = django.core.handlers.wsgi.WSGIHandler()

