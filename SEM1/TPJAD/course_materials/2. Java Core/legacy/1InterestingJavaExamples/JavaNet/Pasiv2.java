import java.io.*;
import java.net.*;
public class Pasiv2 {
  public static void main(String[] a) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    ServerSocket ss = new ServerSocket(12345);
    Socket s = ss.accept();
    DataInputStream sin = new DataInputStream(s.getInputStream());
    DataOutputStream sout = new DataOutputStream(s.getOutputStream());
    for ( ; ; ) {
      int n = sin.readInt();
      byte[] b = new byte[n];
      n = sin.read(b, 0, b.length);
      if (n <= 0) break;
      String linie = new String(b, 0, n);
      System.out.print("Primit de la Activ\""+linie+"\". Da un raspuns: ");
      if (linie.indexOf("STOP")==0) break;
      linie = in.readLine().trim();
      sout.writeInt(linie.length());
      b = linie.getBytes();
      sout.write(b, 0, b.length);
      if (linie.indexOf("STOP")==0) break;
    }
    sout.close();
    sin.close();
    in.close();
    s.close();
    ss.close();
  }
}
