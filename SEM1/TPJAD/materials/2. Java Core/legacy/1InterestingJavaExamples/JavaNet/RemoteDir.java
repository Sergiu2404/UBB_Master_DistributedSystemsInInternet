import java.io.*;
import java.net.*;
public class RemoteDir {
   public static final int PORT = 8899;
   public static void main(String[] a) {
      Socket s;
      String rezumat;
      try {
         s = new Socket(a[0], PORT);
         DataInputStream in = new DataInputStream(s.getInputStream());
         DataOutputStream out = new DataOutputStream(s.getOutputStream());
         byte[] b = a[1].getBytes();  // Nume director in sir de bytes
         out.writeInt(a[1].length()); // Trimite lungimea
         out.write(b);            // Trimite numele de director
         int nr_oct = in.readInt();         // Citeste lungimea
         b = new byte[nr_oct];
         in.read(b);                       // Citeste rezumatul
         rezumat = new String(b);
         System.out.println(rezumat);     // Afiseaza rezumatul
      }//try
      catch (Exception e) {
         e.printStackTrace();
      }//catch
   }// RemoteDir.main
}// RemoteDir

