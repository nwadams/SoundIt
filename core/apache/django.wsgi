import os
import sys

path = '/Users/anuj/Documents/workspace/SoundIt/core'
if path not in sys.path:
    sys.path.append(path)

os.environ['DJANGO_SETTINGS_MODULE'] = 'core.settings'

import django.core.handlers.wsgi
application = django.core.handlers.wsgi.WSGIHandler()
