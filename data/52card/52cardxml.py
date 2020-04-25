# -*- coding: utf-8 -*-
"""
Created on Fri Apr 24 02:42:41 2020

@author: mavch
"""

def generateLayout():
    for r in range(4):
        for c in range(13):
            print('<cell name="'+str(r*13+c)+'">')
            print('<x_val>'+str(7+7*c)+'</x_val>')
            print('<y_val>'+str(3+12*r)+'</y_val>')
            print('</cell>')
    r = 4
    c = 6
    print('<cell name="'+str(52)+'">')
    print('<x_val>'+str(7+7*c)+'</x_val>')
    print('<y_val>'+str(3+12*r)+'</y_val>')
    print('</cell>')
    
def backerCards():
    for i in range(53):
        print("<card><name>D1</name><value>14</value><color>green</color><suit>c</suit><fixed>y</fixed></card>")
    
def mainCells():
    for i in range(52):
        s = '<cell name="{:}"><fan>none</fan><rotation>0</rotation><init_cards>'
        s += '<card>D1,U</card><card>Rd,D</card></init_cards></cell>'
        print(s.format(i))
    
generateLayout()