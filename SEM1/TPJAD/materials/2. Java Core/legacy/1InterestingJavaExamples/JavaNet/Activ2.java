import java.io.*;
import java.net.*;
public class Activ2 {   
  public static void main(String[] a) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Socket s = new Socket((a.length==0)?"localhost":a[0], 12345);
    DataInputStream sin = new DataInputStream(s.getInputStream());
    DataOutputStream sout = new DataOutputStream(s.getOutputStream());
    String linie = "";
    for ( ; ; ) { 
      System.out.print("Primit de la Pasiv \""+linie+"\". Da un raspuns: ");
      if (linie.indexOf("STOP")==0) break;
      linie = in.readLine().trim();
      sout.writeInt(linie.length());
      byte[] b = linie.getBytes();
      sout.write(b, 0, b.length);
      if (linie.indexOf("STOP")==0) break;
      int n = sin.readInt();
      b = new byte[n];
      n = sin.read(b, 0, b.length);
      if (n <= 0) break;
      linie = new String(b, 0, n);
    }
    sout.close();
    sin.close();
    in.close();
    s.close();
  }
}
