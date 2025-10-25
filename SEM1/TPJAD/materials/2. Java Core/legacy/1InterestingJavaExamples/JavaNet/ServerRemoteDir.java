import java.io.*;
import java.net.*;
public class ServerRemoteDir extends Thread {
   public final static int PORT = 8899;
   protected ServerSocket socket_intalnire;
   // Creaza socketul de intalnire si initializeaza thread.
   public ServerRemoteDir() {
      try {
         socket_intalnire = new ServerSocket(PORT);
      }// try
      catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }//catch
      this.start();
   }// ServerRemoteDir.ServerRemoteDir
   
   // Corpul thread-ului server. La fiecare conectare se creaza
   // un obiect de tip Conectare (definit mai jos), care trateaza
   // conexiunea printr-un socket nou
   public void run() {
      Socket socket_client;
      try {
         for (;;) {
            socket_client = socket_intalnire.accept();
            Conectare c = new Conectare(socket_client);
         }//for
      }//try
      catch (Exception e) {
         e.printStackTrace();
      }//catch
   }//ServerRemoteDir.run
   
   // Initializare server
   public static void main(String[] a) {
      new ServerRemoteDir();
   }// ServerRemoteDir.main
}// ServerRemoteDir

