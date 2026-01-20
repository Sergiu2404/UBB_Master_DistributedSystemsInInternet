package p.clients;
import java.io.*;
import java.util.*;
import javax.naming.*;
import p.dtos.IdNameDTO;
import p.interfaces.IdNameR;
public class Client1WF {
    static final Properties JNDI = new Properties();
    static {
        JNDI.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        JNDI.put("java.naming.provider.url", "http-remoting://localhost:8080");
    }
    static final String JNDIIdNameR = "1jpaCs/IdNameBean!p.interfaces.IdNameR";
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
