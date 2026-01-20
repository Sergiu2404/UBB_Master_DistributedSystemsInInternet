public class Set {
    /* Operatii cu multimi reprezentate pe biti, cu elemente bit, char, int etc.
       Functiile care pot modifica multimi intorc 0 daca multimea nu se schimba
        Set(int n);   // Creaza o multime vida in care pot intra n elemente
        int len();  // Intoarce cardinalul multimii (numarul de biti 1)
        int total();  // Intoarce numarul total de elemente posibile in multime (numarul de biti)
        int getBit(int i);  // Intoarce valoarea celui de-al i-lea bit
        int getBit1(int i);  // Intoarce pozitia celui de-al i-lea bit 1 din multime
        int getBit0(int i);  // Intoarce pozitia celui de-al i-lea bit 0 din multime
        int putBit(int i, int v);  // Seteaza valoarea celui de-al i-lea bit cu valoarea v (0 sau 1)
        int addChar(char c, String alfabet);  // Adauga la multime un caracter c din alfabetul cu elementele multimii
        int delChar(char c, String alfabet);  // Sterge din multime un caracter c din alfabetul cu elementele multimii
        char getChar(int i, String alfabet);  // Intoarce al i-lea caracter din multime
        int posChar(char c, String alfabet);  // Intoarce pozitia caracterului c din alfabet in multime, sau -1
        int intersection(Set s);  // Multime va contine intersectia cu multimea s
        int union_(Set s);   // Multime va contine reuniunea cu multimea s
        int complement();   // Multime va contine complementara ei
        Set copy();  // Intoarce pointer la o copie a multime
        String toStr(String alfabet);  // Intoarce un string cu reprezentarea nultimii de caractere
        String toBits();  // Intoarce un string cu reprezentarea nultimii de biti
        int[] toInt();  // Intoarce un tablou de len(multime) intregi cu indicii elementelor din multime
    */
    public static final char GOL = '-';
    static final byte[] M_010_ = {(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x10, (byte) 0x20, (byte) 0x40, (byte) 0x80};
    static final byte[] M_101_ = {(byte) 0xfe, (byte) 0xfd, (byte) 0xfb, (byte) 0xf7, (byte) 0xef, (byte) 0xdf, (byte) 0xbf, (byte) 0x7f};
    // Initializeaza spatiul necesar pentru multime de n elemente
    int totaL;
    byte[] multime;
    public Set(int n) {
        totaL = n;
        int no = (n + 7) / 8;
        multime = new byte[no];
        for (int i = 0; i < no; i++) multime[i] = (byte) 0;
    }
    // Intoarce numarul maxim de elemente posibile in multime
    int total() { return totaL; }
    // Intoarce cardinalul multimii (numarul de biti 1)
    int len() {
        int j, k;
        for (j = 0, k = 0; j < total(); j++) {
            if ((multime[j / 8] & M_010_[j % 8]) == 0) continue;
            k++;
        }
        return k;
    }
    // Intoarce valoarea celui de-al i-lea bit din multime
    int getBit(int i) {
        if (i < 0 || i >= total()) return 0;
        return ((multime[i / 8] & M_010_[i % 8]) != 0) ? 1 : 0;
    }
    // Intoarce pozitia celui de-al i-lea bit 1 din multime
    int getBit1(int i) {
        int j, k;
        if (i < 0 || i >= len()) return -1;
        for (j = 0, k = 0; j < total(); j++) {
            if ((multime[j / 8] & M_010_[j % 8]) != 0) {
                if (k == i) return j;
                k++;
            }
        }
        return -1;
    }
    // Intoarce pozitia celui de-al i-lea bit 0 din multime
    int getBit0(int i) {
        int j, k;
        if (i < 0 || i >= total() - len()) return -1;
        for (j = 0, k = 0; j < total(); j++) {
            if ((multime[j / 8] & M_010_[j % 8]) == 0) {
                if (k == i) return j;
                k++;
            }
        }
        return -1;
    }
    // Seteaza valoarea celui de-al i-lea bit cu valoarea v (0 sau 1)
    int putBit(int i, int v) {
        if (i < 0 || i >= total()) return 0;
        byte c = multime[i / 8];
        multime[i / 8] = (byte) (multime[i / 8] & M_101_[i % 8] | (v & 0x1) << (i % 8));
        if (c == multime[i / 8]) return 0; else return 1;
    }
    // Adauga la multime un caracter c din alfabetul cu elementele multimii
    int addChar(char c, String alfabet) {
        int p = alfabet.indexOf(c);
        if (p == -1) return -1;
        byte cc = multime[p / 8];
        multime[p / 8] = (byte) (multime[p / 8] & M_101_[p % 8] | M_010_[p % 8]);
        if (cc == multime[p / 8]) return 0; else return 1;
    }
    // Sterge din multime un caracter c din alfabetul cu elementele multimii
    int delChar(char c, String alfabet) {
        int p = alfabet.indexOf(c);
        if (p == -1) return -1;
        byte cc = multime[p / 8];
        multime[p / 8] &= M_101_[p % 8];
        if (cc == multime[p / 8]) return 0; else return 1;
    }
    // Intoarce al i-lea caracter din multime
    char getChar(int i, String alfabet) {
        int j, k;
        if (i < 0 || i >= len()) return GOL;
        for (j = 0, k = 0; j < total(); j++) {
            if ((multime[j / 8] & M_010_[j % 8]) != 0) {
                if (k == i) return alfabet.charAt(j);
                k++;
            }
        }
        return GOL;
    }
    // Intoarce pozitia caracterului c din alfabet in multime, sau -1
    int posChar(char c, String alfabet) {
        int k = alfabet.indexOf(c);
        if (k == -1) return -1;
        return ((multime[k / 8] & M_010_[k % 8]) == 0) ? -1 : k;
    }
    // Multime va contine intersectia cu multimea s
    int intersection(Set s) {
        int i, k, n = total();
        if (n != s.total()) return 0;
        for (i = 0, k = 0; i < (n + 7) / 8; i++) {
            byte c = multime[i];
            multime[i] &= s.multime[i];
            if (c == multime[i]) k = 1;
        }
        return k;
    }
    // Multime va contine reuniunea cu multimea s
    int union_(Set s) {
        int i, k, n = total();
        if (n != s.total()) return 0;
        for (i = 0, k = 0; i < (n + 7) / 8; i++) {
            byte c = multime[i];
            multime[i] |= s.multime[i];
            if (c == multime[i]) k = 1;
        }
        return k;
    }
    // Multime va contine complementara ei
    int complement() {
        int i;
        for (i = 0; i < (total() + 7) / 8; i++)
            multime[i] = (byte) (~multime[i]);
        return 1;
    }
    // Intoarce pointer la o copie a multime
    Set copy() {
        Set dest = new Set(total());
        dest.totaL = total();
        for (int i = 0; i < total(); i++) dest.multime[i] = multime[i];
        return dest;
    }
    // Intoarce un string cu reprezentarea nultimii de caractere
    String toStr(String alfabet) {
        int i;
        String s = "[";
        for (i = 0; i < total(); i++) {
            if ((multime[i / 8] & M_010_[i % 8]) == 0) continue;
            s += alfabet.charAt(i) + ",";
        }
        if (s.endsWith(",")) s = s.substring(0,s.length()-1);
        s += ']';
        return s;
    }
    // Intoarce un string cu reprezentarea multimii de biti
    String toBits() {
        int i;
        String s = "";
        for (i = 0; i < total(); i++)
            s += ((multime[i / 8] & M_010_[i % 8]) == 0) ? "0" : "1";
        return s;
    }
    // Intoarce un tablou de len(multime) intregi cu indicii elementelor din multime
    int[] toInt() {
        int i, k;
        int[] s = new int[len()];
        for (i = 0, k = 0; i < total(); i++)
            if ((multime[i / 8] & M_010_[i % 8]) == 0) s[k++] = i;
        return s;
    }
}
