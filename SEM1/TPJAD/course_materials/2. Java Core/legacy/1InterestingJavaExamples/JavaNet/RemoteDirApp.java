import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
public class RemoteDirApp extends Applet implements ActionListener {
   public static final int PORT = 8899;
   Socket s;
   DataInputStream in;
   DataOutputStream out;
   TextField director;
   String masina;
   TextArea rezumat;
   // Linia "director" va primi numele directorului
   // Stringul "masina" va fi preluat ca parametru HTML
   // Zona de date "rezumat" va afisa rezumatul
   public void init() {
      try {
         director = new TextField("Aici se da numele directorului");  // Preia numele de director
         director.addActionListener(this);
         rezumat = new TextArea();
         rezumat.setEditable(false);
         this.setLayout(new BorderLayout());
         this.add("North", director);
         this.add("Center", rezumat);
         masina = this.getParameter("Masina"); // Citeste parametrul HTML
         s = new Socket(masina, PORT);
         in = new DataInputStream(s.getInputStream());
         out = new DataOutputStream(s.getOutputStream());
      }//try
      catch (Exception e) {
         e.printStackTrace();
      }//catch
   }//RemoteDirApp.init
   
   // Cand se da la director un text, evenimentul este
   // interceptat si linia este trimisa serverului
   public void actionPerformed(ActionEvent e) {
      int n;
      byte[] b;
      if (e.getSource() == director) {
         try {
            b = (director.getText()).getBytes();
            out.writeInt(b.length);      // Trimite lungimea
            out.write(b);          // Trimite numele de director
            n = in.readInt();
            b = new byte[n];
            in.read(b);
            rezumat.setText(new String(b));
         }//try
         catch (Exception e2) {
            ;
         }//catch
      }//if
   }// RemoteDirApp.actionPerformed
}// RemoteDirApp

