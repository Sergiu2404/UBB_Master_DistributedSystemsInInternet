import java.io.*;
import java.net.*;
public class PasivUDP {
   
   public static void main(String[] a) throws Exception {
      semnaturaTemporala st = new semnaturaTemporala();
      BufferedReader in = new BufferedReader(
      new InputStreamReader(System.in));
      DatagramSocket si = new DatagramSocket(2345); // Receptie la portul local 2345
      DatagramSocket so = new DatagramSocket(); // Emisie
      DatagramPacket p;
      InetAddress activ;
      byte [] b;
      String linie;
      for ( ; ; ) { // ciclu de asteptare mesaje
         b = new byte[1000];
         p = new DatagramPacket(b, b.length); // Creeaza p cu 1000 locuri
         si.receive(p); // Recepteaza linie de la portul local 23435
         activ = p.getAddress(); // De la cine a venit?
         linie = new String(p.getData()); // Converteste p la string
         linie = linie.substring(0,p.getLength()); // Retine doar ce a primit
         System.out.println("Activ: "+linie+"<"+st.semnaturaTemporala()+">");
         if (linie.indexOf("STOP")==0)
            break;
         System.out.print("Pasiv? "); // Cere o linie
         linie = in.readLine(); // Citeste linia
         linie += "<"+st.semnaturaTemporala()+">";
         b = linie.getBytes(); // Converteste linia la bytes
         p = new DatagramPacket(b, b.length, activ, 5432); // Pune linia, adresa si portul destinatar
         so.send(p); // Trimite pachetul spre portul 5432 al lui Activ
      }// for
      so.close();
      si.close();
      in.close();
   }// PasivUDP.main
}// PasivUDP

