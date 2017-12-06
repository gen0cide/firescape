with open('opcodes.csv', 'r') as f:
    content = f.readlines()

ids = {}

for l in content:
    line = l.strip().split(',')
    if line[0].startswith('SV_'):
        ids[line[2]] = line[0]

f = open('v233_opcode_list.txt', 'r')
for line in f:
    id = line.strip()
    thing = ids.get(id)
    if thing == None:
        print 'unknown:', id


##f1 = open('../../233/src/client.java', 'r')
##f2 = open('../../233/src/client.java.mod', 'w')
##
##for line in f1:
##    for i in ids.items():
##        line = line.replace('newPacket(' + i[0] + ')',
##                            'newPacket(Opcode.getClient(Version.CLIENT, Command.Client.' + i[1] + '))')
##    f2.write(line)
##
##f1.close()
##f2.close()
