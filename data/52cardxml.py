# -*- coding: utf-8 -*-
"""
Created on Fri Apr 24 02:42:41 2020

@author: mavch
"""

def generateLayout():
    for r in range(4):
        for c in range(13):
            print('<cell name="'+str(r*13+c)+'">')
            print('<xval>'+str(5+7*c)+'</xval>')
            print('<yval>'+str(16+12*r)+'</yval>')
            print('</cell>')
    r = 4
    c = 0
    print('<cell name="'+str(r*13+c)+'">')
    print('<xval>'+str(5+7*c)+'</xval>')
    print('<yval>'+str(16+12*r)+'</yval>')
    print('</cell>')
    
    
generateLayout()