import java.io.*;
import java.net.*;
public class PasivTCP {
   
   public static void main(String[] a) throws Exception {
      semnaturaTemporala st = new semnaturaTemporala();
      BufferedReader in = new BufferedReader(
      new InputStreamReader(System.in));
      ServerSocket ss = new ServerSocket(2345);
      Socket s;
      BufferedReader sin;
      PrintStream sout;
      String linie;
      for ( ; ; ) { // ciclu de asteptare mesaje
         s = ss.accept(); // S-a stabilit o conexiune
         sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
         sout = new PrintStream(s.getOutputStream());
         for ( ; ; ) { // Ciclul de dialog in cadrul conexiunii
            linie = sin.readLine();
            System.out.println("Activ: "+linie+"<"+st.semnaturaTemporala()+">");
            if (linie.indexOf("STOP")==0)
               break;
            System.out.print("Pasiv? "); // Cere o linie
            linie = in.readLine();       // Citeste linia
            linie += "<"+st.semnaturaTemporala()+">";
            sout.println(linie);         // Trimite linia la client
         }// for conexiune
         break;
      }// for accept
      sout.close();
      sin.close();
      in.close();
      s.close();
      ss.close();
   }// PasivTCP.main
}// PasivTCP

