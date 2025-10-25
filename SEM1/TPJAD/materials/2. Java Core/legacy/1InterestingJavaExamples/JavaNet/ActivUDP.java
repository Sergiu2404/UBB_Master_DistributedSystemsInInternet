import java.io.*;
import java.net.*;
public class ActivUDP {
   
   public static void main(String[] a) throws Exception {
      semnaturaTemporala st = new semnaturaTemporala();
      BufferedReader in = new BufferedReader(
      new InputStreamReader(System.in));
      DatagramSocket si = new DatagramSocket(5432); // Receptie la portul local 5432
      DatagramSocket so = new DatagramSocket(); // Emisie
      DatagramPacket p;
      InetAddress pasiv = InetAddress.getByName((a.length==0)?"localhost":a[0]);
      byte [] b;
      String linie;
      for ( ; ; ) { // ciclu de asteptare mesaje
         System.out.print("Activ? "); // Cere o linie
         linie = in.readLine(); // Citeste linia
         linie += "<"+st.semnaturaTemporala()+">";
         b = linie.getBytes(); // Converteste linia la bytes
         p = new DatagramPacket(b, b.length, pasiv, 2345); // Pune linia, adresa si portul destinatar
         so.send(p); // Trimite pachetul spre portul 2345 al lui Pasiv
         if (linie.indexOf("STOP")==0)
            break;
         b = new byte[1000];
         p = new DatagramPacket(b, b.length); // Creeaza p cu 1000 locuri
         si.receive(p); // Recepteaza linie de la portul local 5432
         linie = new String(p.getData()); // Converteste p la string
         linie = linie.substring(0,p.getLength()); // Retine doar ce a primit
         System.out.println("Pasiv: "+linie+"<"+st.semnaturaTemporala()+">");
      }// for
      so.close();
      si.close();
      in.close();
   }// ActivUDP.main
}// ActivUDP

