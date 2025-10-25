import java.io.*;
import java.net.*;
import java.util.*;

public class MiniHttpd {

    public static void main(String a[]) {
        if (a.length != 1) {
            System.out.println("Apel: java MiniHttpd port");
            System.exit(1);
        }// if
        new MiniHttpd(Integer.parseInt(a[0]));
    }// MiniHttpd.main

    public MiniHttpd(int port) {
        ServerSocket server;
        Socket socket;
        ThreadClient client;
        int nrCerere = 0;
        try {
            server = new ServerSocket(port);
            System.out.println(InetAddress.getLocalHost()
                    .getCanonicalHostName()
                    + "("
                    + InetAddress.getLocalHost().getHostAddress()
                    + ")"
                    + ":"
                    + port + ". " + (new Date()) + ". MiniHttpd start ...\n");
            for (;;) {
                socket = server.accept(); // Asteapta conectarea de la un client
                nrCerere++;
                client = new ThreadClient(socket, nrCerere);
                client.start(); // Creeaza si porneste un thread pentru client
            }// for
        }// try
        catch (Exception e) {
            e.printStackTrace();
        }// catch
    }// MiniHttpd.run
}// MiniHttpd

class ThreadClient extends Thread {
    Socket socket = null;
    int nrCerere = 0;

    public ThreadClient(Socket socket, int nrCerere) {
        this.socket = socket;
        this.nrCerere = nrCerere;
    }// ThreadClient.ThreadClient

    public void run() {
        PrintStream out = null;
        BufferedReader in = null;
        DataInputStream dis = null;
        File file = null;
        byte[] b = null;
        String cerere = "";
        String numeFisier = "";
        String err = "<html><head><title>Pagina de eroare</title></head>"
                + "<body><h1>HTTP/1.1 404  Not Found</h1></body></html>";
        String httpResp = "HTTP/1.1 ";
        String dateResp = "Date ";
        String serverResp = "Server: MiniHttpd ";
        String contentTypeResp = "Content-Type: ";
        String contentLengthResp = "Content-Length: ";
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            cerere = in.readLine();
// Se trateaza doar prima linie a cererii (vezi protocolul HTTP), se ignora restul.
// Nu se iau in considerare headerele de cerere, nici body cu parametrii cererii.
            
            System.out.println("Cerere" + nrCerere + ": '" + cerere + "' de la: "
                    + socket.getInetAddress().getHostName() + ". " + new Date());
            String[] t = cerere.split("[ ]");
            if (t.length == 3 && t[1].startsWith("/")
                    && (t[0].equals("GET") || t[0].equals("POST"))
                    && (t[2].equals("HTTP/1.0") || t[2].equals("HTTP/1.1"))) {
                if (t[1].equals("/"))
                    numeFisier = "index.html";
                else
                    numeFisier = t[1].substring(1);
                if (numeFisier.endsWith(".html") || numeFisier.endsWith(".htm")) {
// Se presupune ca fisierul este mic si e citit tot deodata.
// In realitate se obtine lungimea, dupa care se trimite pe bucati, ("chunks")
// trimitand mai intai headerul "Transfer-Encoding: chunked"
                    file = new File(numeFisier);
                    b = new byte[(int) file.length()];
                    dis = new DataInputStream(new FileInputStream(numeFisier));
                    dis.readFully(b);
                    dis.close();
                    out.println(httpResp + "200 Ok");
                    out.println(dateResp + (new Date()));
                    out.println(serverResp);
                    out.println(contentLengthResp + b.length);
                    out.println(contentTypeResp + "text/html");
                    out.println();
                    out.println(new String(b));
                    System.out.println("Servit Cerere" + nrCerere);
                } else if (numeFisier.endsWith(".jpg")) {
// Se presupune ca fisierul este mic si e citit tot deodata.
// In realitate se obtine lungimea, dupa care se trimite pe bucati ("chunks")
// trimitand mai intai headerul "Transfer-Encoding: chunked"
// Acum se trimite raspunsul binar. 
// In realitate, mai intai se encodeaza Base64 folosind standardul
// JPEG File Interchange Format (JFIF) , se trimite un header si apoi continutul.
                    
                    file = new File(numeFisier); 
// Trebuie testat daca fisierul exista si daca nu sa se raspunda adecvat - - -
                    b = new byte[(int) file.length()];
                    dis = new DataInputStream(new FileInputStream(numeFisier));
                    dis.readFully(b);
                    dis.close();
                    out.println(httpResp + "200 Ok");
                    out.println(dateResp + (new Date()));
                    out.println(serverResp);
                    out.println(contentLengthResp + b.length);
                    out.println(contentTypeResp + "image/jpeg");
                    out.println();
                    out.write(b);
                    System.out.println("Servit Cerere" + nrCerere);
                } else {
// In realitate, aici se dau raspunsuri la erori: 1XX, 2XX, 3XX, 4XX, 5XX

                    out.println(httpResp + "404 Fisier neadmis" + numeFisier);
                    out.println(dateResp + (new Date()));
                    out.println(serverResp);
                    out.println(contentTypeResp + "text/html");
                    out.println(contentLengthResp + err.length());
                    out.println();
                    out.println(err);
                    System.out.println("Servit Cerere" + nrCerere
                            + " FISIER NEADMIS");
                }
            } else {
                System.out.println("Servit Cerere" + nrCerere
                        + " Cerere ERONATA");
            }
            in.close();
            out.close();
            socket.close();
        }// try
        catch (Exception e) {
            e.printStackTrace();
        }// catch
    }// ThreadClient.run
}// ThreadClient
