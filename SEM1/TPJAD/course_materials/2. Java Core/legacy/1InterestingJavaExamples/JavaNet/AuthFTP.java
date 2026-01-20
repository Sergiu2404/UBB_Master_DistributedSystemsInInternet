import java.io.*;
import java.net.*;
public class AuthFTP {
   public static boolean isFTPUser(String host, String user, String password) {
      Socket socket = null;
      BufferedReader in = null;
      PrintStream out = null;
      String s;
      try {
         socket = new Socket(host, 21);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new PrintStream(socket.getOutputStream());
         s = in.readLine();
         if (s.indexOf("220") != 0)
            return(false);
         out.println("user "+user);
         s = in.readLine();
         if (s.indexOf("331") != 0)
            return(false);
         out.println("pass "+password);
         s=in.readLine();
         if (s.indexOf("230") != 0)
            return(false);
         return(true);
      }//try
      catch (Exception e) {
         e.printStackTrace();
         return(false);
      }//catch
      finally {
         try {
            if (out != null)
               out.close();
            if (in != null)
               in.close();
            if (socket != null)
               socket.close();
            in = null;
            out = null;
            socket = null;
         }//try
         catch (Exception e) {
         }//catch
      }//finally
   }//AuthFTP.isUser
   
   public static void main(String a[]) {
      if (AuthFTP.isFTPUser(a[0], a[1], a[2]))
         System.out.println("Bine");
      else
         System.out.println("Rau");
   }//AuthFTP.main
}//AuthFTP

