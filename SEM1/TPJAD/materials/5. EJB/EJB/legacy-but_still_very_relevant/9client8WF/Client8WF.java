package p.clients;
import java.util.*;
import javax.naming.*;
import p.interfaces.JmsSend;
public class Client8WF {
    static final Properties JNDIProps = new Properties();
    static {
        JNDIProps.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        JNDIProps.put("java.naming.provider.url","http-remoting://localhost:8080");
    }
    static final String JNDI = "9ejbSend8/JmsSendBean!p.interfaces.JmsSend";
    public static void main(String[] args) throws Exception {
        System.out.println("Client8WF");
        Context context = new InitialContext(JNDIProps);
        JmsSend jmsSend = (JmsSend) context.lookup(JNDI);
        jmsSend.send("Am dat un mesaj");
    }
}
