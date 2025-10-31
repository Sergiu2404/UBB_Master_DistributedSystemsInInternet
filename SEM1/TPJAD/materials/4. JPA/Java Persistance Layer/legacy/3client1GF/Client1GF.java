package p.clients;
import java.io.*;
import java.util.*;
import javax.naming.*;
import p.dtos.IdNameDTO;
import p.interfaces.IdNameR;
public class Client1GF {
    static final Properties JNDI = new Properties();
    static {
        JNDI.put("java.naming.factory.initial", "com.sun.enterprise.naming.impl.SerialInitContextFactory");
        JNDI.put("org.omg.CORBA.ORBInitialHost", "localhost");
        JNDI.put("org.omg.CORBA.ORBInitialPort", "3700");
    }
    static final String JNDIIdNameR = "java:global/1jpaCs/IdNameBean!p.interfaces.IdNameR";
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("id:");
        int id = Integer.parseInt(in.readLine());
        System.out.print("name:");
        String name = in.readLine();
        in.close();
        IdNameR proxy = (IdNameR) (new InitialContext(JNDI)).lookup(JNDIIdNameR);
        proxy.cudR(id, name);
        for (IdNameDTO d : proxy.findAllR()) System.out.println(d);
    }
}
