__author__ = 'user'


class File:
    def __init__(self, data, namehash):
        self.data = data
        self.namehash = namehash

    def __str__(self):
        return "jaglib.file.File(data_len=%d, namehash=%d)" % (len(self.data), self.namehash)

    def __repr__(self):
        return self.__str__()

    def __eq__(self, other):
        if type(self) is not type(other):
            return False
        return len(self.data) == len(other.data) and self.namehash == other.namehash    # todo
