import java.io.*;
public class SplitThr {
    public static void main(String a[]) throws Exception {
        long t1 = System.currentTimeMillis();
        long lung = (a.length >= 2) ? (Long.parseLong(a[1])) : 10000000;
        long LUNGIN = (new File(a[0])).length();
        long offs;
        String nume;
        System.out.println("Start SplitThr \""+a[0]+"\": "+LUNGIN+".");
        int nf, nrFiles = (int) ((LUNGIN + lung - 1) / lung);
        ThreadS[] copy = new ThreadS[nrFiles];
        long[] LUNG = new long[nrFiles];
        long[] OFFS = new long[nrFiles];
        String[] NUME = new String[nrFiles];
        for (nf = 0, offs = 0; nf < nrFiles; nf++) {
            nume = "00000" + (nf + 1);
            nume = nume.substring(nume.length() - 5);
            LUNG[nf] = lung;
            OFFS[nf] = offs;
            NUME[nf] = nume;
            offs += lung;
        }
        if (LUNGIN % lung > 0) LUNG[nrFiles -1] = LUNGIN % lung;
        RandomAccessFile in = new RandomAccessFile(a[0], "r");
        for (nf = 0; nf < nrFiles; nf++)
            copy[nf] = new ThreadS(LUNG[nf], OFFS[nf], NUME[nf], in);
        for (nf = 0; nf < nrFiles; nf++)
            copy[nf].join();
        in.close();
        long t2 = System.currentTimeMillis();
        System.out.println("Stop SplitThr. Durata "+(t2-t1)+" milisecunde");
    }
}

class ThreadS extends Thread {
    String nume;
    long lung, offs;
    RandomAccessFile in;
    public ThreadS(long lung, long offs, String nume, RandomAccessFile in) {
        this.lung = lung;
        this.offs = offs;
        this.nume = nume;
        this.in = in;
        this.start();
    }
    public void run() {
        byte[] b = new byte[8192];
        long lo = offs, ro = lung;
        int c;
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(nume));
            for ( ; ro > 0; ) {
                c = (int)Math.min((long)b.length, ro);
                synchronized (in) {
                    in.seek(lo);
                    c = in.read(b, 0, c);
                }
                if (c <= 0) break;
                out.write(b, 0, c);
                lo += c;
                ro -= c;
            }
            System.out.println(lung+" bytes ==> \""+nume+"\"");
            out.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
