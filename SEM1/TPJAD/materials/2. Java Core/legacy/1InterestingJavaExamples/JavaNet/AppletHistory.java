import java.net.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
import java.util.*;
public class  AppletHistory extends Applet {
   static final String NL = System.getProperty("line.separator");
   URL url;
   URLConnection urlCon;
   DataInputStream in;
   DataOutputStream out;
   String start="", navigator="", masina="";
   byte b[];
   public void init() {
      try {
         masina = getParameter("URL");
         start = ""+new Date();
         navigator = InetAddress.getLocalHost().toString();
      }//try
      catch(Exception e) {
         e.printStackTrace();
      }//catch
   }//AppletHistory.init
   public void destroy() {
      try {
         url = new URL(masina+"/cgi-bin/history.cgi");
         urlCon = url.openConnection();// Deschide conexiunea
         urlCon.setDoOutput(true); 		// Permite si scrierea in resursa
         in = new DataInputStream(urlCon.getInputStream());
         out = new DataOutputStream(urlCon.getOutputStream());
         start = start+" "+(new Date())+" "+navigator;
         b = start.getBytes();
         out.write(b);
         out.close();
         in.close();
      }//try
      catch(Exception e) {
         e.printStackTrace();
      }//catch
   }//AppletHistory.destroy
}//AppletHistory

