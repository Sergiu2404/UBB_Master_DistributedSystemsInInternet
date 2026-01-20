import java.net.*;
import java.util.*;
public class semnaturaTemporala {
   public String semnaturaTemporala() {
      String buf=null;
      try {
         //obtine numele masinii locale
         String hostname = (InetAddress.getLocalHost()).getHostName();
         buf = hostname+" "+
         //  Thread.currentThread().getThreadGroup()+"\t"+
         //  Thread.currentThread()+"\t"+
         new Date();
      }//try
      catch (UnknownHostException e) {
         e.printStackTrace();
      }//catch
      return buf;
   }//semnaturaTemporala.semnaturaTemporala
}//semnaturaTemporala

