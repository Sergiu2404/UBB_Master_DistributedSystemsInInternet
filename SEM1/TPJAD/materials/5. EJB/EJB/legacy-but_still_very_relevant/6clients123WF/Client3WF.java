package p.clients;
import java.util.*;
import javax.naming.*;
import p.interfaces.AbacR;
import p.interfaces.CounterR;
public class Client3WF {
    static final Properties JNDIProps = new Properties();
    static {
        JNDIProps.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        JNDIProps.put("java.naming.provider.url","http-remoting://localhost:8080");
    }
    public static void main(String[] args) throws Exception {
        Context context = new InitialContext(JNDIProps);
        AbacR abac = null;
        CounterR counter = null;
        if (abac == null) try {
            abac = (AbacR) context.lookup("3ejbCs/AbacBean!p.interfaces.AbacR");
        } catch (Exception e) { }
        if (abac == null) try {
            abac = (AbacR) context.lookup("4ear123/3ejbCs/AbacBean!p.interfaces.AbacR");
                System.out.println("abac");
        } catch (Exception e) { }
        if (counter == null) try {
            counter = (CounterR) context.lookup("3ejbCs/CounterBean!p.interfaces.CounterR");
        } catch (Exception e) { }
        if (counter == null) try {
            counter = (CounterR) context.lookup("4ear123/3ejbCs/CounterBean!p.interfaces.CounterR");
        } catch (Exception e) { }
        System.out.println("Client3WF");
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
