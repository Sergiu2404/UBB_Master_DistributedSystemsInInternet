import java.io.*;
import java.net.*;
public class ActivTCP {
   
   public static void main(String[] a) throws Exception {
      semnaturaTemporala st = new semnaturaTemporala();
      BufferedReader in = new BufferedReader(
      new InputStreamReader(System.in));
      Socket s = new Socket((a.length==0)?"localhost":a[0], 2345);
      BufferedReader sin = new BufferedReader(new InputStreamReader(s.getInputStream()));;
      PrintStream sout = new PrintStream(s.getOutputStream());
      String linie;
      for ( ; ; ) { // ciclu de asteptare mesaje
         System.out.print("Activ? "); // Cere o linie
         linie = in.readLine(); // Citeste linia
         linie += "<"+st.semnaturaTemporala()+">";
         sout.println(linie); // Trimite linia
         if (linie.indexOf("STOP")==0)
            break;
         linie = sin.readLine();
         System.out.println("Pasiv: "+linie+"<"+st.semnaturaTemporala()+">");
      }// for
      sout.close();
      sin.close();
      in.close();
      s.close();
   }// ActivTCP.main
}// ActivTCP

