'''
Created on Jan 21, 2013

@author: anuj
'''

from django.utils import simplejson


def internalServerErrorResponse(msg): 
    result = []
    result.append({"Error Message": msg})
    return simplejson.dumps(result)