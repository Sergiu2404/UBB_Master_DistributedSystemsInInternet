import javax.naming.*;
import java.io.*;
import java.net.*;
import javax.jms.*;
import java.util.*;
public class ExecJmsServ implements javax.jms.MessageListener {
    private Context ctx = null;
    private QueueConnectionFactory connFac = null;
    private QueueConnection conn = null;
    private QueueSession writeSes = null, readSes = null;
    private javax.jms.Queue queueReq = null, queueRes = null;
    private QueueSender writer = null;
    private QueueReceiver reader = null;
    public ExecJmsServ(String host, String requestQ, String responseQ) throws Exception {
        Properties props = new Properties();
        props.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty("java.naming.provider.url", "tcp://"+host+":61616");
        props.setProperty("queue."+requestQ, requestQ);
        props.setProperty("queue."+responseQ, responseQ);
        ctx = new InitialContext(props);
        connFac = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
        queueReq = (javax.jms.Queue) ctx.lookup(requestQ);
        queueRes = (javax.jms.Queue) ctx.lookup(responseQ);
        conn = connFac.createQueueConnection();
        writeSes = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        readSes = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        writer = writeSes.createSender(queueRes);
        reader = readSes.createReceiver(queueReq);
        reader.setMessageListener(this);
        conn.start();
        System.out.println("Server JMS Java wait at: "+host+":61616 req: "+requestQ+" res: "+responseQ);
    }
    public void onMessage(Message mes) {
        String req = "", res = "";
        try { req = ((TextMessage) mes).getText();
        } catch (Exception e) { try { // Unele STOMP intorc sir de bytes
            BytesMessage bm = (BytesMessage) mes;
            byte[] b = new byte[(int)bm.getBodyLength()];
            bm.readBytes(b);
            req = new String(b);
        } catch (Exception ex) {} }
        String[] t = (req + "|0|0|0").split("[|]");
        if (t[0].equals("ping")) res = ping();
        else if (t[0].equals("upcase")) res = upcase(t[1]);
        else if (t[0].equals("add")) res = "" + add(Integer.parseInt(t[1]), Integer.parseInt(t[2]));
        else res = "Apel eronat";
        try {
            TextMessage mesT = writeSes.createTextMessage();
            mesT.setText(req+": "+res);
            writer.send(mesT);
        } catch (JMSException e) { System.out.println("Eroare JMS raspuns "+e); }
    }
    public String ping() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            return "Java JMS: "+host.getHostName()+" ("+host.getHostAddress()+"), "+new Date();
        } catch (Exception e) { return "ERROR"; }
    }
    public String upcase(String s) { return s.toUpperCase(); }
    public int add(int a, int b) { return a + b; }
    public static void main(String[] args) throws Exception {
        if (args.length > 2)
            new ExecJmsServ(args[0], args[1], args[2]);
        else
            new ExecJmsServ("localhost", "Jcerere", "Jraspuns");
    }
}
