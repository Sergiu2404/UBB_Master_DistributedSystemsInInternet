import java.net.*;
import java.io.*;
import java.util.*;

public class URLInfo {
   public static void main(String a[]) throws Exception {
      // Creeaza un URL la o resursa, deschide o conexiune cu acest URL,
      URL url = new URL(a[0]);
      URLConnection u = url.openConnection();
      // Afiseaza adresa URL si alte informatii despre ea
      System.out.println(u.getURL().toExternalForm() + ":");
      System.out.println("Tip continut: " + u.getContentType());
      System.out.println("Lungime continut: "+
            u.getContentLength());
      System.out.println("Ultima modificare: "+
            new Date(u.getLastModified()));
      System.out.println("Flagurile I/O. Input: "+u.getDoInput()+
            " Output: "+u.getDoOutput());
      // Citeste si afiseaza primele 10 linii de la URL.
      System.out.println("Primele 10 linii:");
      BufferedReader in = new BufferedReader(
            new InputStreamReader(u.getInputStream()));
      for(int i = 0; i < 10; i++) { 
         String linie = in.readLine();
         if (linie == null) break;
         System.out.println(linie);
      } // for
   } // URLInfo.main
} // URLInfo
