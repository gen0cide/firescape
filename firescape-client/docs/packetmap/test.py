
def readv(fname, dic):
    f = open(fname, 'r')
    for line in f:
        id = line.split(',')
        dic[id[0]] = id[1].strip()
    f.close()

v202_c = {}
v202_s = {}
v204_c = {}
v204_s = {}
v204_new_c = {}
v204_new_s = {}

readv('v202_c.txt', v202_c)
readv('v202_s.txt', v202_s)
readv('v204_c.txt', v204_c)
readv('v204_s.txt', v204_s)
readv('v204_new_c.txt', v204_new_c)
readv('v204_new_s.txt', v204_new_s)

def findk(dic, val):
    for k,v in dic.items():
        if v == val:
            return k

names = {}


for k,v in v204_s.items():
    names[k] = findk(v204_new_s, v)

for k,v in v204_c.items():
    names[k] = findk(v204_new_c, v)

#print "command,v202,v204"

##for k,v in v202_c.items():
##    print "%s,%s,%s" % (names[k], v, v204_c[k])
##
##for k,v in v202_s.items():
##    print "%s,%s,%s" % (names[k], v, v204_s[k])

for k,v in v204_c.items():
    print "put(Command.%s, %s);" % (names[k], v)

for k,v in v204_s.items():
    print "put(Command.%s, %s);" % (names[k], v)
