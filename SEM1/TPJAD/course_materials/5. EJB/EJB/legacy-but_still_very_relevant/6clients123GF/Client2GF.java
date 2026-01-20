package p.clients;
import java.util.*;
import javax.naming.*;
import p.interfaces.Abac2;
import p.interfaces.Counter2;
public class Client2GF {
    static final Properties JNDIProps = new Properties();
    static {
        JNDIProps.put("java.naming.factory.initial", "com.sun.enterprise.naming.impl.SerialInitContextFactory");
        JNDIProps.put("org.omg.CORBA.ORBInitialHost", "localhost");
        JNDIProps.put("org.omg.CORBA.ORBInitialPort", "3700");
    }
    public static void main(String[] args) throws Exception {
        Context context = new InitialContext(JNDIProps);
        Abac2 abac = null;
        Counter2 counter = null;
        if (abac == null) try {
            abac = (Abac2) context.lookup("java:global/2ejbSingleton/Abac2Bean!p.interfaces.Abac2");
        } catch (Exception e) { }
        if (abac == null) try {
            abac = (Abac2) context.lookup("java:global/4ear123/2ejbSingleton/Abac2Bean!p.interfaces.Abac2");
        } catch (Exception e) { }
        if (counter == null) try {
            counter = (Counter2) context.lookup("java:global/2ejbSingleton/Counter2Bean!p.interfaces.Counter2");
        } catch (Exception e) { }
        if (counter == null) try {
            counter = (Counter2) context.lookup("java:global/4ear123/2ejbSingleton/Counter2Bean!p.interfaces.Counter2");
        } catch (Exception e) { }
        System.out.println("Client2WF");
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
