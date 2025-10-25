import java.io.*;
import java.net.*;
import java.util.*;

public class Time {
   // A se vedea RFC 868
   final long Sec_1900_1970 = 2208988800L; // numarul de secunde intre 1900 (RFC868) si 1970 (java.util)
   
   String timeUDP1(String host) throws Exception {
      DatagramSocket s;
      DatagramPacket p;
      Date c;
      long l;
      String h;
      byte [] b = new byte[4];
      p = new DatagramPacket(b, 0, InetAddress.getByName(host), 37);
      s = new DatagramSocket();
      s.send(p);                         // Trimite la server un pachet vid (RFC868)
      p.setLength(4);
      s.receive(p);                      // Asteapta cei 4 octeti si-i pune in pachet
      h = (p.getAddress()).getHostName(); // Adresa masinii de unde a venit pachetul se scoate din pachet
      s.close();
      Integer[] in = new Integer[1];
      ConvertBytes.getInt(p.getData(), 0, in);
      l = (long)in[0].intValue(); // l contine numarul de secunde de la 01:01:1900
      l -= Sec_1900_1970;   // Numarul de secunde pana la 1970
      c = new Date(l*1000); // Creaza o data in milisecunde de la 1970
      return c.toString()+" "+h;
   }//Time.timeUDP1
   
   String timeUDP2(String host) throws Exception {
      DatagramSocket so, si;
      DatagramPacket po, pi;
      Date c;
      int portLocal;
      long l;
      String h;
      byte [] b = new byte[4];
      po = new DatagramPacket(b, 0, InetAddress.getByName(host), 37);
      pi = new DatagramPacket(b, 4);
      so = new DatagramSocket();
      portLocal = so.getLocalPort();
      so.send(po);                         // Trimite la server un pachet vid (RFC868)
      so.close();
      si = new DatagramSocket(portLocal);
      si.receive(pi);                      // Asteapta cei 4 octeti si-i pune in pachet
      h = (pi.getAddress()).getHostName(); // Adresa masinii de unde a venit pachetul se scoate din pachet
      si.close();
      Integer[] in = new Integer[1];
      ConvertBytes.getInt(pi.getData(), 0, in);
      l = (long)in[0].intValue(); // l contine numarul de secunde de la 01:01:1900
      l -= Sec_1900_1970;   // Numarul de secunde pana la 1970
      c = new Date(l*1000); // Creaza o data in milisecunde de la 1970
      return c.toString()+" "+h;
   }//Time.timeUDP2
   
   String timeTCP(String host) throws Exception {
      Socket s;
      DataInputStream in;
      Date c;
      long l;
      String h;
      s = new Socket(host, 37);
      h = (s.getInetAddress()).getHostName(); // Se stie deja adresa masinii server time
      in = new DataInputStream(s.getInputStream()); // Pe in vin 4 octeti in bigendian
      byte[] b = new byte[4];
      in.read(b);
      Integer[] i = new Integer[1];
      ConvertBytes.getInt(b, 0, i);
      l = (long)i[0].intValue(); // l contine numarul de secunde de la 01:01:1900
      in.close();
      s.close();
      l -= Sec_1900_1970;         // Numarul de secunde pana la 1970
      c = new Date(l*1000);       // Creaza o data in milisecunde de la 1970
      return c.toString()+" "+h;
   }//Time.timeTCP
   
   public static void main(String[] a) throws Exception {
      Time t = new Time();
      System.out.println(t.timeUDP1("linux.scs.ubbcluj.ro")); // Trebuie sa fie server time si sa asculte UDP
      System.out.println(t.timeUDP2("linux.scs.ubbcluj.ro")); // Trebuie sa fie server time si sa asculte UDP
      System.out.println(t.timeTCP("linux.scs.ubbcluj.ro")); // Trebuie sa fie server time si sa asculte TCP
      System.out.println((new Date())+" "+(InetAddress.getLocalHost()).getHostName());
   }// Time.main
}// Time
