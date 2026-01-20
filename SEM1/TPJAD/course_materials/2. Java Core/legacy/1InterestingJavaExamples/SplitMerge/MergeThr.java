import java.io.*;
import java.util.*;
public class MergeThr {
    public static void main(String a[]) throws Exception {
        long t1 = System.currentTimeMillis();
        ArrayList<Long> LUNG = new ArrayList<Long>();
        ArrayList<Long> OFFS = new ArrayList<Long>();
        ArrayList<String> NUME = new ArrayList<String>();
        int nf;
        long offs, lung;
        String nume;
        System.out.println("Start MergeThr \""+a[0]+"\".");
        for (nf = 1, offs = 0; ; nf++) {
            nume = "00000" + nf;
            nume = nume.substring(nume.length() - 5);
            if (!new File(nume).exists()) break;
            lung = (new File(nume)).length();
            if (lung == 0) break;
            LUNG.add(lung);
            OFFS.add(offs);
            NUME.add(nume);
            offs += lung; // Ultima valoare este lungimea iesirii a[0]
        }
        ThreadM[] copy = new ThreadM[OFFS.size()];
        RandomAccessFile out = new RandomAccessFile(a[0], "rw");
        for (nf = 0; nf < OFFS.size(); nf++)
            copy[nf] = new ThreadM(LUNG.get(nf), OFFS.get(nf), NUME.get(nf), out);
        for (nf = 0; nf < OFFS.size(); nf++)
            copy[nf].join();
        out.close();
        long t2 = System.currentTimeMillis();
        System.out.println("Stop MergeThr. Durata "+(t2-t1)+" milisecunde.");
    }
}

class ThreadM extends Thread {
    String nume;
    long lung, offs;
    RandomAccessFile out;
    public ThreadM(long lung, long offs, String nume, RandomAccessFile out) {
        this.lung = lung;
        this.offs = offs;
        this.nume = nume;
        this.out = out;
        this.start();
    }
    public void run() {
        byte[] b = new byte[8192];
        long lo = offs, ro = lung;
        int c;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(nume));
            for ( ; ro > 0; ) {
                c = (int)Math.min((long)b.length, ro);
                c = in.read(b, 0, c);
                if (c <= 0) break;
                synchronized (out) {
                    out.seek(lo);
                    out.write(b, 0, c);
                }
                lo += c;
                ro -= c;
            }
            System.out.println("\""+nume+"\" ==> "+lung+" bytes");
            in.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
