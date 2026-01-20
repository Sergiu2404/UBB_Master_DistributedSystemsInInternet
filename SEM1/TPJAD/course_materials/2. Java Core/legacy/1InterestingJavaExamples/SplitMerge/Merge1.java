import java.io.*;
import java.util.*;
public class Merge1 {
    public static void main(String a[]) throws Exception {
        long t1 = System.currentTimeMillis();
        ArrayList<Long> LUNG = new ArrayList<Long>();
        ArrayList<String> NUME = new ArrayList<String>();
        int nf;
        long lung;
        String nume;
        System.out.println("Start Merge1 \""+a[0]+"\".");
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
        System.out.println("Stop Merge1. Durata "+(t2-t1)+" milisecunde.");
    }
    static void copy(long lung, String nume, DataOutputStream out) {
        long ro = lung;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(nume));
            for (; ro > 0; ro--)
                out.writeByte(in.readByte());
            System.out.println("\""+nume+"\" ==> "+lung+" bytes");
            in.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
