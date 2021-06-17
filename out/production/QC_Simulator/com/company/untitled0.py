# -*- coding: utf-8 -*-
"""
Created on Sat May 29 16:48:23 2021

@author: lenovo
"""

file = open("D:\\Java\\QC_Simulator\\Instructions.txt","r")
lines = file.readlines()

if not lines:
    exit()
for line in lines:
    line = line.strip("\n")
    line = line.split(" ")
    print(line)