import javax.naming.*;
import java.io.*;
import javax.jms.*;
import java.util.*;
public class ExecJmsClie {
    private Context ctx = null;
    private QueueConnectionFactory connFac = null;
    private QueueConnection conn = null;
    private QueueSession writeSes = null, readSes = null;
    private javax.jms.Queue queueReq = null, queueRes = null;
    private QueueSender writer = null;
    private QueueReceiver reader = null;
    public ExecJmsClie(String host, String requestQ, String responseQ) throws Exception {
        System.out.println("Client JMS Java: "+host+":61616 req: "+requestQ+" res: "+responseQ);
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
        writer = writeSes.createSender(queueReq);
        reader = readSes.createReceiver(queueRes);
        conn.start();
        TextMessage mes = writeSes.createTextMessage();
        mes.setText("ping");
        writer.send(mes);
        System.out.println(toText(reader.receive()));
        mes = writeSes.createTextMessage();
        mes.setText("upcase|negru");
        writer.send(mes);
        System.out.println(toText(reader.receive()));
        mes = writeSes.createTextMessage();
        mes.setText("add|66|75");
        writer.send(mes);
        System.out.println(toText(reader.receive()));
        mes = writeSes.createTextMessage();
        mes.setText("ping");
        writer.send(mes);
        System.out.println(toText(reader.receive()));
        conn.close();
    }
    String toText(Message mes) { // unele STOMP trimit BytesMessage
        String res = "";
        try {
            res = ((TextMessage)mes).getText();
        } catch (Exception e) { try { // Unele STOMP intorc sir de bytes
            BytesMessage bm = (BytesMessage) mes;
            byte[] b = new byte[(int)bm.getBodyLength()];
            bm.readBytes(b);
            res = new String(b);
        } catch (Exception ex) {} }
        return res;
    }
    public static void main(String[] args) throws Exception {
        if (args.length > 2)
            new ExecJmsClie(args[0], args[1], args[2]);
        else
            new ExecJmsClie("localhost", "Jcerere", "Jraspuns");
    }
}
