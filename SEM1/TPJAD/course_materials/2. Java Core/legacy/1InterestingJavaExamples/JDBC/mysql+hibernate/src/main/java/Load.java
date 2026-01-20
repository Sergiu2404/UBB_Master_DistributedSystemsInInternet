import java.io.*;
public class Load {
    public Load() {
        BufferedReader in = null;
        String[] tok = null;
        String linie = "";
        char tip;
        Dao.openSession();
        try {
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
                switch (tip) {
                    case 'S':
                        if (tok.length != 3) continue;
                        Dao.saveSectie(new Sectie(null, tok[1], tok[2]));
                        break;
                    case 'A':
                        if (tok.length != 5) continue;
                        Sectie sectie = Dao.findSectie(tok[3]);
                        if (sectie == null) continue;
                        Dao.saveAngajat(new Angajat(null, tok[1], Integer.parseInt(tok[2]), sectie, tok[3]));
                        break;
                   default: continue;
                }
            } catch (Exception ex) { ex.printStackTrace(); continue; }
        }
        System.out.println(Dao.findAllSectii());
        System.out.println(Dao.findAllAngajati());
        Dao.closeSession();
    }
    public static void main(String a[]) {
        new Load();
    }
}
