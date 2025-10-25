import java.io.*;
import java.sql.*;
import java.util.*;
public class Load {
    public Load() {
        BufferedReader in = null;
        String[] tok = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String linie, nume, numesectie, descriere, caracterizare;
        int varsta, ids;
        char tip;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            con = DriverManager.getConnection("jdbc:odbc:MS Access Database;DBQ=src/RESURSA/dbexemplu.mdb");
            st = con.createStatement();
            in = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        for ( ; ; ) {
            try {
                linie = in.readLine();
                if (linie == null) break;
                if (linie.isEmpty()) continue;
                tip = linie.charAt(0);
                tok = linie.split("[|]");
                nume = tok[1].trim();
                switch (tip) {
                    case 'S':
                        if (tok.length != 3) continue;
                        descriere = tok[2].trim();
                        st.executeUpdate("insert into sectii (nume, descriere) values ('"+nume+"','"+descriere+"')");
                        break;
                    case 'A':
                        if (tok.length != 5) continue;
                        varsta = Integer.parseInt(tok[2].trim());
                        numesectie = tok[3].trim();
                        rs = st.executeQuery("select id from sectii where nume = '"+numesectie+"'");
                        if (!rs.next()) continue;
                        ids = rs.getInt(1);
                        caracterizare = tok[4];
                        st.executeUpdate("insert into angajati (nume, varsta, sectia, caracterizare) values ('"+
                                nume+"',"+varsta+","+ids+",'"+caracterizare+"')");
                        break;
                   default: continue;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        try {
            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String a[]) {
        new Load();
    }
}
