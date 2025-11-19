var os = require("os");
var ip = require("ip");
var Stomp = require('stomp-client');
function ExecJmsServ(host, requestQ, responseQ) {
    console.log("Server JMS NodeJs send at: "+host+":61613 req: "+requestQ+" res: "+responseQ);
    var serv = new Stomp(host, 61613);
    serv.connect(function(sessionId) {
        serv.subscribe("/queue/"+requestQ, function(mes, headers) { 
            t = (mes + "|0|0|0").split("|");
            if (t[0] == "ping")
                res = ping();
            else if (t[0] == "upcase")
                res = upcase(t[1]);
            else if (t[0] == "add")
                res = add(parseInt(t[1]), parseInt(t[2])).toString();
            else
                res = "Apel eronat";
            serv.publish("/queue/"+responseQ, mes+": "+res)
        });
    });
};
function ping() {
    name = os.hostname();
    return "NodeJs JMS(stomp) "+name+"("+ip.address()+"), "+(new Date());
}
function upcase(s) { return s.toUpperCase(); }
function add(a, b) { return a + b; }
if (process.argv.length > 4)
    ExecJmsServ(process.argv[2], process.argv[3], process.argv[4])
else 
    ExecJmsServ("localhost", "Ncerere", "Nraspuns")
