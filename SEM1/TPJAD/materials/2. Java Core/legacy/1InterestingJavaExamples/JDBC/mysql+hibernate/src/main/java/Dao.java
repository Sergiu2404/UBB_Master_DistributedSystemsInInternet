import org.hibernate.*;
import org.hibernate.cfg.*;
import java.util.*;
public class Dao {
    private static SessionFactory factory = null;
    private static Session session = null;
    public static void openSession() {
        try { factory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) { System.out.println("ERROARE "+e); }
        session = factory.openSession();
    }
    public static void closeSession() {
        session.close();
    }
    public static Sectie findSectie(Integer id) {
        return (Sectie)session.get(Sectie.class, id);
    }
    public static Sectie findSectie(String nume) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query =  session.createQuery("from Sectie s where s.nume=:nume");
            query.setParameter("nume", nume);
            List<Sectie> sectii =  query.list();
            session.getTransaction().commit();
            if (sectii.size() < 1) return null;
            return sectii.get(0);
        } catch (Exception e) {
            System.out.println(e); return null;
        }
    }
    public static List<Sectie> findAllSectii() {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Sectie> sectii = session.createQuery("from Sectie").list();
            session.getTransaction().commit();
            return sectii;
        } catch (Exception e) {
            System.out.println(e); return null;
        }
    }
    public static Angajat findAngajat(Integer id) {
        return (Angajat)session.get(Angajat.class, id);
    }
    public static Angajat findAngajat(String nume) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Angajat a where a.nume=:nume");
            query.setParameter("nume", nume);
            List<Angajat> angajati = query.list();
            session.getTransaction().commit();
            if (angajati.size() < 1) return null;
            return angajati.get(0);
        } catch (Exception e) {
            System.out.println(e); return null;
        }
    }
    public static List<Angajat> findAllAngajati() {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Angajat> angajati = session.createQuery("from Angajat").list();
            session.getTransaction().commit();
            return angajati;
        } catch (Exception e) {
            System.out.println(e); return null;
        }
    }
    public static List<Angajat> findAngajatiSectieVarsta(String sectie, Integer varsta) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Angajat a where a.sectie.nume=:sectie and a.varsta>:varsta");
            query.setParameter("sectie", sectie);
            query.setParameter("varsta", varsta);
            List<Angajat> angajati = query.list();
            session.getTransaction().commit();
            return angajati;
        } catch (Exception e) {
            System.out.println(e); return null;
        }
    }
    public static void saveSectie(Sectie sectie) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(sectie);
            session.getTransaction().commit();
        } catch (Exception e) { tx.rollback();System.out.println(e); }
     }
    public static void saveAngajat(Angajat angajat) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(angajat);
            session.getTransaction().commit();
        } catch (Exception e) { tx.rollback();System.out.println(e); }
     }
}
