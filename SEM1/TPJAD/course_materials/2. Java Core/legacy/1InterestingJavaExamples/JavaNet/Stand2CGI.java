import java.net.*;
import java.io.*;
public class Stand2CGI {
   static final String NL = System.getProperty("line.separator");
   static public void main(String a[]) {
      URL url;
      URLConnection urlCon;
      DataOutputStream out;
      BufferedReader in;
      String s = "";
      byte b[];
      try {
         url = new URL(a[0]+"/cgi-bin/upcase.cgi");
         urlCon = url.openConnection();// Deschide conexiunea
         urlCon.setDoOutput(true);		// permite si scrierea
         System.out.println("Conexiune la URL: "+url.toExternalForm());
         in = new BufferedReader(new InputStreamReader(System.in));
         out = new DataOutputStream(urlCon.getOutputStream());
         // Citeste liniile de la intrarea standard si le trimite la CGI
         for ( ; ((s=in.readLine()) != null); ) {
            s += NL;
            b = s.getBytes();
            out.write(b);
         }//for
         out.close();
         // Creaza un obiect care va citi linii de la CGI
         in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
         // Citeste liniile venite de la CGI si le afiseaza pe iesirea standard
         for ( ; ((s=in.readLine()) != null); ) {
            System.out.println(s);
         }//for
         in.close();
      }//try
      catch(Exception e) {
         System.out.println("Stand2CGI: "+e);
      }//catch
   }//Stand2CGI.main
}//Stand2CGI

