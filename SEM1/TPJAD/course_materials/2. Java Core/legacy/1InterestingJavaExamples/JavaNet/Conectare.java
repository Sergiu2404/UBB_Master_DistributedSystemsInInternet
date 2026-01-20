import java.io.*;
import java.net.*;
// Clasa care defineste thread-ul de tratare a unei conexiuni
class Conectare extends Thread {
   protected DataInputStream in;
   protected DataOutputStream out;
   Socket client;
   
   // Initializeaza "fisierul" de socket si thread-ul
   public Conectare(Socket socket_client) {
      client = socket_client;
      try {
         in = new DataInputStream(client.getInputStream());
         out = new DataOutputStream(client.getOutputStream());
      }//try
      catch (Exception e) {
         e.printStackTrace();
         return;
      }//catch
      this.start();
   }// Conectare.Conectare
   
   // Furnizeaza serviciul
   public void run() {
      final String NL = System.getProperty("line.separator");
      String s;
      try {
         // Citeste lungimea si numele directorului
         int nr_oct = in.readInt();
         System.out.println(nr_oct);
         byte[] b = new byte[nr_oct];
         in.read(b);
         String director = new String(b);
         System.out.println(director);
         
         File f = new File(director);
         if (f.isDirectory()) {
            // Obtine rezumatul si-l face un string unic
            String[] rezumat = f.list();
            s = new String();
            for(int i=0; i<rezumat.length; i++)
               s += rezumat[i]+NL;
            b = s.getBytes(); // Converteste in sir de bytes
            // Trimite lungimea si continutul
            out.writeInt(s.length());
            System.out.println(s.length());
            
            out.write(b);
         }//if director
        else {
            s=director+ " nu este un director";
            out.writeInt(s.length());
            b=s.getBytes(); 
            out.write(b);
          }
// in cazul in care cel de-al doilea parametru nu este director se 
// intra intr-o asteptare infinita (daca nu se pune varianta de else)
// clientul asteapta infinit rezumatul de la server iar serverul nu // trimite nimic

      }//try
      catch (Exception e) {
         e.printStackTrace();
      }//catch
   }// Conectare.run
}// Conectare

