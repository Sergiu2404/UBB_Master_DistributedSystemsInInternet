import javax.naming.*;
import java.io.*;
import javax.jms.*;
public class JmsRecv implements MessageListener {
    public JmsRecv(String host, String numeCoada) {
        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty("java.naming.provider.url", "tcp://" + host + ":61616");
            props.setProperty("queue." + numeCoada, numeCoada);
            Context context = new InitialContext(props);
            QueueConnectionFactory connFac = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
            Queue coada = (Queue) context.lookup(numeCoada);
            QueueConnection conn = connFac.createQueueConnection();
            QueueSession rdSesiune = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueReceiver reader = rdSesiune.createReceiver(coada);
            reader.setMessageListener(this);
            conn.start();
        } catch (Exception e) {e.printStackTrace();}
    }
    public void onMessage(Message mes) {
        try {
            System.out.println(((TextMessage)mes).getText());
        } catch (Exception e) {e.printStackTrace();}
    }
    public static void main(String[] args) {
        if (args.length > 1) new JmsRecv(args[0], args[1]); 
        else new JmsRecv("localhost", "COADA");
    }
}
