import java.net.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
public class  AppletCounterCGI extends Applet {
   static final String nonLitereCifre = " ,./<>?;':\"[]{}`~!@#$%^&*()_-+=|\\";
   public void init() {
      URL url;
      URLConnection urlCon;
      BufferedReader in;
      DataOutputStream out;
      StringTokenizer st;
      String s = "";
      byte b[];
      Label raspuns;
      try {
         url = new URL(this.getCodeBase().toString()+"cgi-bin/counterCGI.cgi");
         s = url.toString();
         s = s.substring(s.lastIndexOf("/"));
         st = new StringTokenizer(s, nonLitereCifre);
         s = "";
         while (st.hasMoreTokens()) {
            s += st.nextToken();
         }//while
         s = s.toLowerCase();
         s += ".count";
         urlCon = url.openConnection();// Deschide conexiunea
         urlCon.setDoOutput(true);		// Permite scrierea
         out = new DataOutputStream(urlCon.getOutputStream());
         b = s.getBytes();
         out.write(b);
         System.out.println(s);
         in = new BufferedReader(   // Creeaza obiect ce va citi o linie din CGI
                              new InputStreamReader(urlCon.getInputStream()));
         s = in.readLine();            // Citeste linia si o depune in s
         raspuns = new Label(s, Label.CENTER);       // Depune linia
         this.add(raspuns);	         // Adauga obiectul la applet
         this.setVisible(true);        // Fa appletul vizibil
         in.close();
         out.close();
      }//try
      catch(Exception e) {
         e.printStackTrace();
      }//catch
   }//AppletCounterCGI.init
}//AppletCounterCGI

