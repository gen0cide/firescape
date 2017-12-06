__author__ = 'user'
import bz2
from file import File


def from_file(filename):
    data = bytearray()
    with open(filename, "rb") as f:
        byte = f.read(1)
        while byte != "":
            data.append(ord(byte))
            byte = f.read(1)
    if not data:
        raise TypeError("not a jagex archive")


def prep_data(src, size, srcpos=0):
    comp = bytearray(size + 4)
    comp[0] = ord("B")
    comp[1] = ord("Z")
    comp[2] = ord("h")
    comp[3] = ord("1")
    for i in range(size):
        comp[i + 4] = src[srcpos + i]
    return comp


class Archive:
    def __init__(self, data=bytearray()):
        self.data = data
        self.files = []
        self.unpacked = False
        self.loaded = False

    def unpack(self):
        if self.unpacked:
            pass
        size = ((self.data[0] & 0xff) << 16) + ((self.data[1] & 0xff) << 8) + (self.data[2] & 0xff)
        sizec = ((self.data[3] & 0xff) << 16) + ((self.data[4] & 0xff) << 8) + (self.data[5] & 0xff)
        data = bytearray(sizec)
        for i in range(sizec):
            data[i] = self.data[i + 6]
        print "size: %d sizec: %d" % (size, sizec)
        if size != sizec:
            data = bz2.decompress(prep_data(data, sizec))
        self.data = bytearray(data)
        self.unpacked = True

    def read(self):
        if self.loaded:
            pass
        entries = ((self.data[0] & 0xff) << 8) + (self.data[1] & 0xff)
        offset = entries * 10 + 2
        for entry in range(entries):
            pos = entry * 10 + 2
            namehash = ((self.data[pos] & 0xff) << 24) + ((self.data[pos + 1] & 0xff) << 16) + \
                       ((self.data[pos + 2] & 0xff) << 8) + (self.data[pos + 3] & 0xff)
            size = ((self.data[pos + 4] & 0xff) << 16) + ((self.data[pos + 5] & 0xff) << 8) + \
                   (self.data[pos + 6] & 0xff)
            sizec = ((self.data[pos + 7] & 0xff) << 16) + ((self.data[pos + 8] & 0xff) << 8) + \
                    (self.data[pos + 9] & 0xff)
            filedata = bytearray(size)
            if size != sizec:
                filedata = bz2.decompress(prep_data(self.data, sizec, offset))  # untested
            else:
                for i in range(size):
                    filedata[i] = self.data[offset + i]
            self.add_file(File(filedata, namehash))
            offset += sizec
        del self.data
        print "files:", len(self.files)
        self.loaded = True

    def add_file(self, datafile):
        if datafile not in self.files:
            self.files.append(datafile)

    def get_file(self, namehash):
        file_ = filter(lambda f: f.namehash == namehash, self.files)
        return None if file_ == [] else file_[0]

    def prep(self):
        if not self.unpacked:
            self.unpack()
        if not self.loaded:
            self.read()
