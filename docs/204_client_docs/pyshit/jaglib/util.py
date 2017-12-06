__author__ = 'user'
from ctypes import c_byte


def calc_namehash(name):
    name = name.upper()
    namehash = 0
    for i in range(len(name)):
        namehash = (namehash * 61 + ord(name[i]) - 32) & 0xffffffff
    return namehash


def byte(ubyte):
    return c_byte(ubyte)
