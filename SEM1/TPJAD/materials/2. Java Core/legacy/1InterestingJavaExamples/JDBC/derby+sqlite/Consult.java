import java.io.*;
import java.sql.*;
import java.util.*;

public class Consult {

public Consult(String[] a) {
  Connection scon = null, dcon = null;
  Statement sst = null, dst = null;
  ResultSet srs = null, drs = null;
  try {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    Class.forName("org.sqlite.JDBC").newInstance();
    scon = DriverManager.getConnection("jdbc:sqlite:dbexemplu.db");
    dcon = DriverManager.getConnection("jdbc:derby:dbexemplu");
    sst = scon.createStatement();
    dst = dcon.createStatement();
    System.out.println("Sectiile");
    srs = sst.executeQuery("select nume, descriere from sectii");
    drs = dst.executeQuery("select nume, descriere from sectii");
    for ( ; ; ) {
      if (!srs.next()) break;
      if (!drs.next()) break;
      System.out.println(srs.getString(1)+" | "+srs.getString(2)+"\n"+drs.getString(1)+" | "+drs.getString(2));
    }
    sst.close();
    dst.close();
    scon.close();
    dcon.close();
  } catch (Exception ex) {
    ex.printStackTrace();
  }
}

public static void main(String[] a) {
  new Consult(a);
}
}
