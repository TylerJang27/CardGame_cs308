# -*- coding: utf-8 -*-
"""
Created on Sun Apr 26 20:52:45 2020

@author: mavch
"""

def generateLayout():
    for i in range(16):
        r = i//4
        c = i%4
        msg = '<cell name="{:}"><x_val>{:}</x_val><y_val>{:}</y_val></cell>'
        print(msg.format(i, 20+20*r, 5+20*c))
    print(msg.format("update", 20, 85))
    print(msg.format("step", 80, 85))
        
def generateCards():
    msg1 = '<card><name>AS</name><value>1</value><color>black</color><suit>s</suit></card>'
    for i in range(16):
        print(msg1)
    print('<card><name>QH</name><value>2</value><color>green</color><suit>h</suit></card>')
    print('<card><name>KH</name><value>2</value><color>green</color><suit>h</suit></card>')
        
def generateCells():
    msg = '<cell name="{:}"><fan>none</fan><rotation>0</rotation><init_cards><card>B,U</card></init_cards></cell>'
    for i in range(16):
        print(msg.format(i))
    print('<cell name="update"><fan>none</fan><rotation>0</rotation><init_cards><card>update,U</card></init_cards></cell>')
    
generateLayout()