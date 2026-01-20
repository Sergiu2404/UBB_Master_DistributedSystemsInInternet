var Stomp = require('stomp-client');
function ExecJmsClie(host, requestQ, responseQ) {
    console.log("\nClient JMS NodeJs send at: "+host+":61613 req: "+requestQ+" res: "+responseQ);
    var clie = new Stomp(host, 61613);
    clie.connect(function(sessionId) {
        clie.subscribe("/queue/"+responseQ, function(body, headers) { 
            console.log(body);
        });
        clie.publish("/queue/"+requestQ, "ping");
        clie.publish("/queue/"+requestQ, "upcase|negru");
        clie.publish("/queue/"+requestQ, "add|66|75");
        clie.publish("/queue/"+requestQ, "ping");
    });
};
if (process.argv.length > 4)
    ExecJmsClie(process.argv[2], process.argv[3], process.argv[4])
else 
    ExecJmsClie("localhost", "Ncerere", "Nraspuns")
