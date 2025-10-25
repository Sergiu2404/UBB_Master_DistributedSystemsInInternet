import java.io.*;
import java.net.*;

// Trimite un mesaj prin UDP:
// Masina destinatie este primul argument al liniei de comanda
// Textul mesajului este al doilea argument al liniei

public class DatagramSend {
  static final int PORT = 9898;
  public static void main (String args[]) throws Exception {
    if (args.length !=2) {
      System.out.println("Utilizare java Masina Mesaj");
      System.exit(1);
    }
    
    // Obtine adresa Internet a masinii
    InetAddress adresa = InetAddress.getByName(args[0]);
    
    // Converteste mesajul intr-un sir de bytes
    int lungime = args[1].length();
    byte [] mesaj = args[1].getBytes();

    // Initializeaza pachetul cu date si adresa
    DatagramPacket pachet = new DatagramPacket(
                   mesaj, lungime, adresa, PORT);

    // Creaza socket si emite pachetul prin el
    DatagramSocket s = new DatagramSocket();
    s.send(pachet);
  }
}

