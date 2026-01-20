import java.sql.*;
public class Consult {
    public Consult(String[] a) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String numesectie = "";
        int varsta = 0;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            con = DriverManager.getConnection("jdbc:sqlserver://RESURSA;DatabaseName=dbexemplu", "user", "password");
            st = con.createStatement();
            System.out.println("Sectiile");
            rs = st.executeQuery("select nume, descriere from sectii");
            for ( ; ; ) {
                if (!rs.next()) break;
                System.out.println(rs.getString(1)+" | "+rs.getString(2));
            }
            System.out.println("Angajatii");
            rs = st.executeQuery("select a.nume, a.varsta, s.nume, a.caracterizare from angajati a "+
                    "inner join sectii s on s.id = a.sectia");
            for ( ; ; ) {
                if (!rs.next()) break;
                System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4));
            }
            if (a.length > 0) numesectie = a[0];
            if (a.length > 1) varsta = Integer.parseInt(a[1]);
            System.out.println("Angajatii din "+numesectie+" mai batrani decat "+varsta);
            rs = st.executeQuery("select a.nume from angajati a "+
                    "inner join sectii s on s.id = a.sectia "+
                    "where s.nume = '"+numesectie+"' and a.varsta > "+varsta);
            for ( ; ; ) {
                if (!rs.next()) break;
                System.out.println(rs.getString(1));
            }
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        new Consult(a);
    }
}
