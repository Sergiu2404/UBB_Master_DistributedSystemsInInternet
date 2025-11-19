package p.clients;
import java.util.*;
import javax.naming.*;
import p.interfaces.Abac1;
import p.interfaces.Counter1;
public class Client1GF {
    static final Properties JNDIProps = new Properties();
    static {
        JNDIProps.put("java.naming.factory.initial", "com.sun.enterprise.naming.impl.SerialInitContextFactory");
        JNDIProps.put("org.omg.CORBA.ORBInitialHost", "localhost");
        JNDIProps.put("org.omg.CORBA.ORBInitialPort", "3700");
    }
    public static void main(String[] args) throws Exception {
        Context context = new InitialContext(JNDIProps);
        Abac1 abac = null;
        Counter1 counter = null;
        if (abac == null) try {
            abac = (Abac1) context.lookup("java:global/1ejb1Session/Abac1Bean!p.interfaces.Abac1");
        } catch (Exception e) { }
        if (abac == null) try {
            abac = (Abac1) context.lookup("java:global/4ear123/1ejbSession/Abac1Bean!p.interfaces.Abac1");
        } catch (Exception e) { }
        if (counter == null) try {
            counter = (Counter1) context.lookup("java:global/1ejbSession/Counter1Bean!p.interfaces.Counter1");
        } catch (Exception e) { }
        if (counter == null) try {
            counter = (Counter1) context.lookup("java:global/4ear123/1ejbSession/Counter1Bean!p.interfaces.Counter1");
        } catch (Exception e) { }
        System.out.println("Client1WF");
        System.out.println(abac);
        System.out.println(counter);
        System.out.println("12 + 13 = " + abac.add(12, 13));
        System.out.println("12 - 13 = " + abac.sub(12, 13));
        System.out.println("Urmeaza 10 incrementari, counter dupa incrementare:");
        for (int i = 0; i < 10; i++) {
            counter.inc();
            System.out.println(counter.getCount());
        }
        System.out.println("Urmeaza 7 decrementari, counter dupa decrementare:");
        for (int i = 7; i > 0; i--) {
            counter.dec();
            System.out.println(counter.getCount());
        }
    }
}
