import java.util.*;
public class Consult {
    public Consult(String numeSectie, Integer varsta) {
        List<Sectie> ls = null;
        List<Angajat> la = null;
        Dao.openSession();
        System.out.println("Sectiile");
        ls = Dao.findAllSectii();
        for ( Sectie s : ls )
            System.out.println(s.nume+" | "+s.descriere);
        System.out.println("Angajatii");
        la = Dao.findAllAngajati();
        for ( Angajat a : la )
            System.out.println(a.nume+" | "+a.varsta+" | "+a.sectie.nume+" | "+a.caracterizare);
        System.out.println("Angajatii din "+numeSectie+" mai batrani decat "+varsta);
        la = Dao.findAngajatiSectieVarsta(numeSectie, varsta);
        for ( Angajat a : la )
            System.out.println(a.nume);
        Dao.closeSession();
    }
    public static void main(String[] a) {
        if (a.length > 1) new Consult(a[0], Integer.parseInt(a[1]));
        else if (a.length > 0) new Consult(a[0], 0);
        else new Consult("", 0);
    }
}
