package p.beans;
import javax.ejb.*;
import javax.annotation.*;
import javax.naming.*;
import javax.inject.*;
import javax.jms.*;
import p.interfaces.JmsSend;
@Stateless
@Remote(JmsSend.class)
@JMSDestinationDefinition(name = "java:global/jms/COADA_in_EJB", interfaceName = "javax.jms.Queue")
public class JmsSendBean implements JmsSend {
    @Inject
    JMSContext context;
    @Resource(mappedName="java:global/jms/COADA_in_EJB")
    private Queue coada;
    public void send(String mesaj) {
        try {
        context.createProducer().send(coada, mesaj);
        } catch (Exception e) {e.printStackTrace();}
    }
}
