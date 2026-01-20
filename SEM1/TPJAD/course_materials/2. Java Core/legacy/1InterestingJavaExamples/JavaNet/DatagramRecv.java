import java.io.*;
import java.net.*;

// Receptioneaza un mesaj prin UDP:
// Textul mesajului este primit prin socket este afisat la iesire

public class DatagramRecv {
  static final int PORT = 9898;
  public static void main (String args[]) throws Exception {
    
    byte[] b = new byte[1024];
    String mesaj;
    
    // Creaza un pachet gol unde sa receptioneze
    DatagramPacket pachet = new DatagramPacket(b, b.length);

    // Creaza socket care asteapta la port
    DatagramSocket s = new DatagramSocket(PORT);
    for(;;) {
      // Asteapta sosirea unei datagrame
      s.receive(pachet);

      // Converteste continutul la un string
      mesaj = new String(b, 0, pachet.getLength());

      // Afiseaza mesajul la iesire
      System.out.println("Receptionat de la: "+
        pachet.getAddress().getHostName()+":"+
        pachet.getPort()+":\n"+mesaj);
    }
  }
}

