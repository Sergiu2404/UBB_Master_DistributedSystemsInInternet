// Extinde operatii cu multimi din Set, in sensul ca multimea se reprezinta
// intr-un fisier, accesand la un moment dat doar o PAGINA cu MULTIME biti.
// La schimbul de pagini, se salveaza pagina veche daca a fost modificata.
import java.io.*;
public class Pagini {
/*
int npag(long n);  // Da pagina bitului n
int noff(long n);  // Da offsetul bitului n in pagina
Pagini(String numefisier, long long n);  // Creaza fisier si initializeaza paginile cu toti bitii 1
int GetPag(int newpag);              // Citeste, daca este cazul, pagina solicitata
int GetBit(long i);          // Intoarce valoarea celui de-al i-lea bit {
int PutBit(long i, int v);   // Depune valoarea v al celui de-al i-lea bit
int Read(byte[] t, int o);   // Citeste (eventual din bucati) exact o octeti din fisier si ii depune la t
void Close();                 // Inchide fisier, cu eventuala salvare a ultimei pagini
*/
    public static final int MULTIME = 9;
    public static final int LPAG = 4 + (MULTIME + 7) / 8;
    public Set pagina;   // Pagina curenta
    public int pag;      // Numarul paginii curente
    public int mod;      // Indicator de modificare a multimii
    public long nb;      // Numarul total de biti
    public int np;       // Numarul total de pagini
    RandomAccessFile fisier; // Fisierul ce contine multimile;
    // Din numarul bitului se obtine pagina si offsetul
    public int npag(long n) { return ((int)(n / MULTIME)); }
    public int noff(long n) { return ((int)(n % MULTIME)); }
    // Creaza fisier si initializeaza paginile cu toti bitii 1
    public Pagini(String numefisier, long n) throws Exception {
        fisier = new RandomAccessFile(numefisier, "rw");
        nb = n;
        np = (int)((n + MULTIME - 1) / MULTIME);
        mod = 0;
        pagina = new Set(MULTIME);
        pagina.complement();
        int i;
        for (i = 0; i < np - 1; i++) {
            fisier.writeInt(pagina.totaL);
            fisier.write(pagina.multime);
        }
        i = (int)(nb % MULTIME);
        if ( i == 0) i = MULTIME;
        pagina.totaL = i;
        pagina.complement();
        fisier.writeInt(pagina.totaL);
        fisier.write(pagina.multime);
        fisier.seek(0);
        pagina.totaL = fisier.readInt();
        Read(pagina.multime);
    }
    // Citeste, daca este cazul, pagina solicitata
    public int GetPag(int newpag) throws Exception {
        if (newpag == pag || newpag < 0 || newpag >= np) return 0;
        if ( mod == 1) {
            fisier.seek((long)(pag * LPAG));
            fisier.writeInt(pagina.totaL);
            fisier.write(pagina.multime);
        }
        fisier.seek((long)(newpag * LPAG));
        pagina.totaL = fisier.readInt();
        Read(pagina.multime);
        pag = newpag;
        mod = 0;
        return 1;
    }
    // Intoarce valoarea celui de-al i-lea bit
    public int GetBit(long i) throws Exception {
        if (i < 0 || i >= nb) return -1;
        GetPag(npag(i));
        return pagina.getBit(noff(i));
    }
    // Depune valoarea v al celui de-al i-lea bit
    public int PutBit(long i, int v) throws Exception {
        if (i < 0 || i >= nb) return -1;
        GetPag(npag(i));
        int r = pagina.putBit(noff(i), v);
        if (r != 0) mod = 1;
        return r;
    }
    // Citeste bucati dintr-o pagina pana la citirea completa
    public int Read(byte[] t) throws Exception {
        int o = t.length, ro = o, oc, pt = 0;
        for ( ; ; ) {
            oc = fisier.read(t, pt, ro);
            if (oc == ro || oc <= 0) return oc;
            pt += oc;
            ro -= oc;
        }
    }
    // Inchide, cu eventuala salvare a ultimei pagini
    public void Close() throws Exception {
        if ( mod == 1) {
            fisier.seek((long)(pag * LPAG));
            fisier.writeInt(pagina.totaL);
            fisier.write(pagina.multime);
        }
        fisier.close();
    }
}
