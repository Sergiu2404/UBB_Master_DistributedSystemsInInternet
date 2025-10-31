package p.clients;
import java.io.*;
import java.util.*;
import java.net.*;
public class Client1LaServletUrl {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("id:");
        int id = Integer.parseInt(in.readLine());
        System.out.print("name:");
        String name = in.readLine();
        in.close();
        URL url = new URL("http://localhost:8080/1jpaCs/client?id="+id+"&name="+name);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        for ( ; ; ) {
            String linie = in.readLine();
            if (linie == null) break;
            System.out.println(linie);
        }
        in.close();
    }
}
