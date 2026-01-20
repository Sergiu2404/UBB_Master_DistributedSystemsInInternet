import javax.naming.*;
import java.io.*;
import javax.jms.*;
public class JmsSend {  
    public JmsSend(String host, String numeCoada, String mesaj) {
        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty("java.naming.provider.url", "tcp://" + host + ":61616");
            props.setProperty("queue." + numeCoada, numeCoada);
            Context context = new InitialContext(props);
            QueueConnectionFactory connFac = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
            Queue coada = (Queue)context.lookup(numeCoada);
            QueueConnection conn = connFac.createQueueConnection();
            QueueSession wrSesiune = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender writer = wrSesiune.createSender(coada);
            conn.start();
            TextMessage mes = wrSesiune.createTextMessage();
            mes.setText(mesaj);
            writer.send(mes);
            conn.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public static void main(String[] args) {
        if (args.length > 2) new JmsSend(args[0], args[1], args[2]); 
        else new JmsSend("localhost", "COADA", "Salut");
    }
}
