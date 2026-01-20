import java.net.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
public class  Applet2CGI extends Applet {
   static final String NL = System.getProperty("line.separator");
   public void init() {
      URL url;
      URLConnection urlCon;
      DataOutputStream out;
      BufferedReader in;
      String s="", linie;
      byte b[];
      int i;
      TextArea raspuns = new TextArea();
      try {
         linie = getParameter("URL");
         url = new URL(linie+"/cgi-bin/upcase.cgi");
         s = "Conexiune la URL: "+url.toExternalForm()+NL;
         urlCon = url.openConnection();// Deschide conexiunea
         urlCon.setDoOutput(true); 		// Permite si scrierea in resursa
         out = new DataOutputStream(urlCon.getOutputStream());
         // Citeste liniile date ca parametri in HTML si le trimite la CGI
         for (i=1; ((linie = getParameter("Linia" + i))!= null); i++) {
            linie += "\n";
            b = linie.getBytes();
            out.write(b);
         }//for
         // Creeaza un obiect care va citi linii de la CGI
         in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
         // Citeste liniile de la CGI si le depune in s
         for (i=1; ((linie = in.readLine())!= null); i++) {
            s += linie+NL;
         }//for
         in.close();
         out.close();
         this.add(raspuns);	// Adauga obiectul la applet
         this.setVisible(true);// Fa appletul vizibil
         raspuns.setText(s);	// Depune liniile de raspuns
      }//try
      catch(Exception e) {
         s = "AppletCGI: "+e;
      }//catch
   }//Applet2CGI.init
}//Applet2CGI

