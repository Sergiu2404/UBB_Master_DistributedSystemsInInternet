import java.io.*;
import java.sql.*;
import java.util.*;

public class Load {

public Load() {
  BufferedReader in = null;
  StringTokenizer stok = null;
  Connection scon = null, dcon = null;
  Statement sst = null, dst = null;
  ResultSet rs = null;
  String linie, nume, numesectie, descriere, caracterizare;
  int varsta, ids;
  char tip;
  try {
    Class.forName("org.sqlite.JDBC").newInstance();
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    scon = DriverManager.getConnection("jdbc:sqlite:dbexemplu.db");
    dcon = DriverManager.getConnection("jdbc:derby:dbexemplu");
    sst = scon.createStatement();
    dst = dcon.createStatement();
    in = new BufferedReader(new InputStreamReader(System.in));
  } catch (Exception ex) {
    ex.printStackTrace();
    System.exit(1);
  }
  for ( ; ; ) {
    try {
      linie = in.readLine();
      if (linie == null) break;
      stok = new StringTokenizer(linie, "|");
      if (stok.countTokens() != 3 && stok.countTokens() != 5) continue;
      tip = stok.nextToken().charAt(0);
      if (tip != 'S' && tip != 'A') continue;
      if (tip == 'S' && stok.countTokens() != 2) continue;
      if (tip == 'A' && stok.countTokens() != 4) continue;
      nume = stok.nextToken();
      switch (tip) {
      case 'S':
        descriere = stok.nextToken();
        sst.executeUpdate("insert into sectii (nume, descriere) values ('"+nume+"','"+descriere+"')");
        dst.executeUpdate("insert into sectii (nume, descriere) values ('"+nume+"','"+descriere+"')");
        break;
      case 'A':
        varsta = Integer.parseInt(stok.nextToken());
        numesectie = stok.nextToken();
        rs = sst.executeQuery("select id from sectii where nume = '"+numesectie+"'");
        if (!rs.next()) continue;
        ids = rs.getInt(1);
        caracterizare = stok.nextToken();
        sst.executeUpdate("insert into angajati (nume, varsta, sectia, caracterizare) values ('"+
          nume+"',"+varsta+","+ids+",'"+caracterizare+"')");
        dst.executeUpdate("insert into angajati (nume, varsta, sectia, caracterizare) values ('"+
          nume+"',"+varsta+","+ids+",'"+caracterizare+"')");
        break;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      continue;
    }
  }
  try {
    sst.close();
    scon.close();
    dst.close();
    dcon.close();
  } catch (Exception ex) {
    ex.printStackTrace();
  }
}
public static void main(String a[]) {
  new Load();
}
}
