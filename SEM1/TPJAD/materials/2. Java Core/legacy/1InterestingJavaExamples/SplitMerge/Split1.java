import java.io.*;
public class Split1 {
    public static void main(String a[]) throws Exception {
        long t1 = System.currentTimeMillis();
        long lung = (a.length >= 2) ? (Long.parseLong(a[1])) : 10000000;
        long LUNGIN = (new File(a[0])).length();
        String nume;
        System.out.println("Start Split1 \""+a[0]+"\": "+LUNGIN+".");
        int nf, nrFiles = (int) ((LUNGIN + lung - 1) / lung);
        long[] LUNG = new long[nrFiles];
        String[] NUME = new String[nrFiles];
        for (nf = 0; nf < nrFiles; nf++) {
            nume = "00000" + (nf + 1);
            nume = nume.substring(nume.length() - 5);
            LUNG[nf] = lung;
            NUME[nf] = nume;
        }
        if (LUNGIN % lung > 0) LUNG[nrFiles -1] = LUNGIN % lung;
        DataInputStream in = new DataInputStream(new FileInputStream(a[0]));
        for (nf = 0; nf < nrFiles; nf++)
            copy(LUNG[nf], NUME[nf], in);
        in.close();
        long t2 = System.currentTimeMillis();
        System.out.println("Stop Split1. Durata "+(t2-t1)+" milisecunde");
    }
    static void copy(long lung, String nume, DataInputStream in) {
        long ro = lung;
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(nume));
            for ( ; ro > 0; ro--)
                out.writeByte(in.readByte());
            System.out.println(lung+" bytes ==> \""+nume+"\"");
            out.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
