import sys
from stompest.config import StompConfig
from stompest.sync import Stomp
from stompest.protocol import StompSpec
class ExecJmsClie:
    def __init__(self, host, requestQ, responseQ):
        print ("Client Python JMS send at: "+host+":61613 req: "+requestQ+" res: "+responseQ)
        clie = Stomp(StompConfig("tcp://"+host+":61613"))
        clie.connect()
        clie.subscribe("/queue/"+responseQ, {StompSpec.ACK_HEADER: StompSpec.ACK_CLIENT_INDIVIDUAL})
        clie.send("/queue/"+requestQ, "ping".encode())
        frame = clie.receiveFrame()
        print (frame.body.decode())
        clie.ack(frame)
        clie.send("/queue/"+requestQ, "upcase|negru".encode())
        frame = clie.receiveFrame()
        print (frame.body.decode())
        clie.ack(frame)
        clie.send("/queue/"+requestQ, "add|66|75".encode())
        frame = clie.receiveFrame()
        print (frame.body.decode())
        clie.ack(frame)
        clie.send("/queue/"+requestQ, "ping".encode())
        frame = clie.receiveFrame()
        print (frame.body.decode())
        clie.ack(frame)
        clie.disconnect()
if len(sys.argv) > 3:
    ExecJmsClie(sys.argv[1], sys.argv[2], sys.argv[3])
else: 
    ExecJmsClie("localhost", "Pcerere", "Praspuns")
