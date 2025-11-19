import sys
from stompest.config import StompConfig
from stompest.sync import Stomp
from stompest.protocol import StompSpec
import socket
from datetime import datetime
class ExecJmsServ:
    def __init__(self, host, requestQ, responseQ):
        serv = Stomp(StompConfig("tcp://"+host+":61613"))
        serv.connect()
        serv.subscribe("/queue/"+requestQ, {StompSpec.ACK_HEADER: StompSpec.ACK_CLIENT_INDIVIDUAL})
        print ("Server Python JMS wait at: "+host+":61613 req: "+requestQ+" res: "+responseQ)
        while True:
            frame = serv.receiveFrame()
            serv.ack(frame)
            mes = (frame.body).decode()
            t = (mes+"|0|0|0").split("|")
            if t[0] == "ping":
                res = self.ping()
            elif t[0] == "upcase":
                res = self.upcase(t[1])
            elif t[0] == "add":
                res = str(self.add(int(t[1]), int(t[2])))
            else:
                res = "Apel eronat"
            serv.send("/queue/"+responseQ, (mes+": "+res).encode())
    def ping(self):
        name = socket.gethostname()
        ip = socket.gethostbyname(name)
        return ("Python JMS(stomp) "+name+"("+ip+"), "+str(datetime.now()))
    def upcase(self, s): return s.upper()
    def add(self, a, b): return a + b
if len(sys.argv) > 3: 
    ExecJmsServ(args[1], args[2], args[3])
else: 
    ExecJmsServ("localhost", "Pcerere", "Praspuns")
