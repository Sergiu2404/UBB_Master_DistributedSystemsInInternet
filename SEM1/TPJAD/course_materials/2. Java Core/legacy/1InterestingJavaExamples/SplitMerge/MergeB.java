import java.io.*;
import java.util.*;
public class MergeB {
    public static void main(String a[]) throws Exception {
        long t1 = System.currentTimeMillis();
        ArrayList<Long> LUNG = new ArrayList<Long>();
        ArrayList<String> NUME = new ArrayList<String>();
        int nf;
        long lung;
        String nume;
        System.out.println("Start MergeB \""+a[0]+"\".");
        for (nf = 1; ; nf++) {
            nume = "00000" + nf;
            nume = nume.substring(nume.length() - 5);
            if (!new File(nume).exists()) break;
            lung = (new File(nume)).length();
            if (lung == 0) break;
            LUNG.add(lung);
            NUME.add(nume);
        }
        DataOutputStream out = new DataOutputStream(new FileOutputStream(a[0]));
        for (nf = 0; nf < LUNG.size(); nf++)
            copy(LUNG.get(nf), NUME.get(nf), out);
         out.close();
        long t2 = System.currentTimeMillis();
        System.out.println("Stop MergeB. Durata "+(t2-t1)+" milisecunde.");
    }
    static void copy(long lung, String nume, DataOutputStream out) {
        byte[] b = new byte[8192];
        long ro = lung;
        int c;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(nume));
            for (; ro > 0; ) {
                c = (int)Math.min((long)b.length, ro);
                c = in.read(b, 0, c);
                if (c <= 0) break;
                out.write(b, 0, c);
                ro -= c;
            }
            System.out.println("\""+nume+"\" ==> "+lung+" bytes");
            in.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
