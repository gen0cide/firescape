__author__ = 'user'
import sys
from jaglib.archive import Archive
from jaglib.file import File
from jaglib.util import calc_namehash


def parse(data):
    a = Archive(data)
    a.prep()
    f = a.get_file(calc_namehash("om05050.dat"))
    print f


if __name__ == "__main__":
    filename = "../204/data204/maps63.jag"
    if len(sys.argv) > 1:
        filename = sys.argv[1]
    try:
        data = bytearray()
        with open(filename, "rb") as f:
            byte = f.read(1)
            while byte != "":
                data.append(ord(byte))
                byte = f.read(1)
        if not data:
            print "file is die"
            raise SystemExit
        parse(data)
    except IOError as e:
        if e.errno == 2:
            print "unable to find file", filename
        else:
            print e.errno, e
