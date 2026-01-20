import java.io.*;
import java.net.*;
public class ServerTelnet {
   
   public boolean telnet(BufferedReader in, PrintStream out) throws Exception {
      String s = in.readLine();
      Process p = (Runtime.getRuntime()).exec(s);
      p.waitFor();
      InputStream pin = p.getInputStream();
      byte[] b = new byte[1000];
      int n = pin.read(b);
      out.print("Iesirea comenzii \""+s+"\" este: <###\r\n"+(new String(b,0,n))+"\r\n###>"+
      ", Lungime: "+n+" caractere, Cod de retur: "+p.exitValue()+"\r\n");
      return p.exitValue()==0;
   }//ServerTelnet.telnet
   
   public static void main(String[] a) throws Exception {
      ServerTelnet st = new ServerTelnet();
      ServerSocket ss = new ServerSocket(5656);
      Socket s = ss.accept(); // S-a stabilit o conexiune
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      PrintStream out = new PrintStream(s.getOutputStream());
      st.telnet(in, out);
      out.close();
      in.close();
      s.close();
      ss.close();
   }// ServerTelnet.main
}// ServerTelnet

