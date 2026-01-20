package p.beans;
import javax.naming.*;
import javax.ejb.*;
import javax.jms.*;
import javax.annotation.*;
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName="destinationType",propertyValue="javax.jms.Queue"),
    @ActivationConfigProperty(propertyName="destinationLookup", propertyValue="java:global/jms/COADA_in_EJB")
})
public class MdBean implements MessageListener {
    public void onMessage(Message msg) {
        try {
            TextMessage mesaj = (TextMessage) msg;
            mesaj = (TextMessage)msg;
            System.out.println(mesaj.getText());
        } catch (Exception e) { e.printStackTrace();}
    }
}
