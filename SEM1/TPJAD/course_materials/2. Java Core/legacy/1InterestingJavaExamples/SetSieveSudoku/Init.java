// Citeste de la intrarea standard si valideaza alfabetul, grupele si jocul initial.
// Fisierul de intrare va fi unul ca in exemplele urmatoare:
/*
  |    1.txt    |  2.txt  |    3.txt           |
  |    n = 9    |  n = 5  |    n = 9           |
  +-------------+---------+--------------------+
  |  123456789  |  ABCDE  |  A CD EI LO RAX .  |
  |  aaabbbccc  |  22XXX  |  aaaabbbbc         |
  |  aaabbbccc  |  22ccX  |  aaaabbbcc         |
  |  aaabbbccc  |  2cceX  |  addebbccc         |
  |  dddeeefff  |  1ceee  |  dddeeefcc         |
  |  dddeeefff  |  1111e  |  gdddefffc         |
  |  dddeeefff  |  -----  |  ggdeeefff         |
  |  ggghhhiii  |  ----D  |  ggghheffi         |
  |  ggghhhiii  |  --E--  |  gghhhiiii         |
  |  ggghhhiii  |  ---B-  |  ghhhhiiii         |
  |  7---4--9-  |  A----  |  -R---E-I-         |
  |  38-2----4  |         |  --X--O-D-         |
  |  --5--6---  |         |  -X--DA---         |
  |  12--3----  |         |  --R-I---C         |
  |  -5-----8-  |         |  CLE-O-RA-         |
  |  ----8--79  |         |  ----C-X--         |
  |  ---1--4--  |         |  ---XA--C-         |
  |  8----7-31  |         |  -A-R--O--         |
  |  -7--9---6  |         |  -C-I---R-         |
  +-------------+---------+--------------------+
*/
// Din prima linie se retin pentru alfabet numai caractere alfanumerice distincte.
// gl.n si gl.alfabet retin constantele jocului.
// Se numara celulele (prin marcajele lor) si cardinalele acestora: toate trebuie sa fie gl.n.
// Din definitia initiala a matricei jocului, se retin caracterele ce verifica cerintele jocului.
// Se initializeaza cele 3 * n multimi, ce contin ce lipseste in matrice pe linii, coloane si grupe
// Intoarce 0 la esec si 1 la initializare corecta.
import java.io.*;
import java.util.*;
public class Init {
    public static final String alnum="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // Date constante pe durata rezolvarii jocului
    int n = 0; // Dimensiunea jocului
    String alfabet = null; // Alfabetul jocului
    String ag = null; // Alfabetul de marcare a grupurilor de celule
    int[][] ig = null; // Indicii celulelor
    // Date la care se are acces sincronizat
    int esecuri; // Numar curent de esecuri
    int solutii; // Numarul curent de solutii
    int curent; // Numarul ultimuluipartial creat
    int partiale; // Numar de partiale active sau de pornit
    Hashtable<Integer, Impur> listIm; // Retine partile impure ale threadurilor active
    Impur initial = null;
/*
Init(BufferedReader iN);        // Citeste de la intrarea standard si valideaza alfabetul, grupele si jocul initial
void prinT(Impur im, String s); // Tipareste continutul unui obiect Impur
Impur(int n);                   // Creaza un obiect Impur pentru un joc partial de dimensiune n
ThrSud(Init gl, int curent);    // Executie a unui thread pentru un joc partial
void delListIm(Impur im);       // Sterge un obiect Impur din lista de threaduri active
*/
    public Init(BufferedReader iN) throws Exception {
        int i, j, k;
        int[] ingr = new int[alnum.length()];
        char c;
        esecuri = 0;
        solutii = 0;
        curent = 0;
        partiale = 1;
        listIm = new Hashtable<Integer, Impur>();
        String linie = iN.readLine().trim();
        Set p = new Set(alnum.length());
        for (i = 0; i < linie.length(); i++) p.addChar(linie.charAt(i), alnum);
        n = p.len(); // Fixeaza constanta n a jocului
        alfabet = "";
        for ( i = 0; i < p.total(); i++) {
            c = p.getChar(i, alnum);
            if (c != Set.GOL) alfabet += c;
        }
        ag = ""; // Alfabetul de marcare a grupurilor de celule
        ig = new int[n][n];// Indicii grupelor de celule
        // Celulele dintr-un grup se marcheaza cu acelasi caracter
        for (i = 0; i < n; i++) {
            if ((linie = iN.readLine()) == null) break;
            for (j = 0; j < n && j < linie.length(); j++) {
                c = linie.charAt(j);
                if (alnum.indexOf(c) == -1) continue;
                k = ag.indexOf(c);
                if (k == -1) {
                    k = ag.length();
                    if (k < alnum.length()) ag += c;
                    ig[i][j] = k;
                    ingr[k] = 1;
                } else {
                    ig[i][j] = k;
                    ingr[k]++;
                }
            }
        }
        boolean b = ag.length() == n;
        for (i = 0; i < n; i++) b &= ingr[i] == n;
        if (!b) {
            System.out.println("Definirea celor "+n+" grupe de celule este eronata! STOP");
            return;
        }
        // Se preia tabelul initial
        initial = new Impur(n);
        initial.curent = 0;
        for (i = 0; i < n; i++) {
            if ((linie = iN.readLine()) == null) break;
            for (j = 0; j < linie.length() && j < n; j++) {
                c = linie.charAt(j);
                if (alnum.indexOf(c) == -1) continue;
                if (initial.ml[i].posChar(c, alfabet) >= 0) continue;
                if (initial.mc[j].posChar(c, alfabet) >= 0) continue;
                if (initial.mg[ig[i][j]].posChar(c, alfabet) >= 0) continue;
                initial.table[i][j] = c;
                initial.ml[i].addChar(c, alfabet);
                initial.mc[j].addChar(c, alfabet);
                initial.mg[ig[i][j]].addChar(c, alfabet);
            }
        }
        for (i = 0; i < n; i++) {
            initial.ml[i].complement();
            initial.mc[i].complement();
            initial.mg[i].complement();
        }
    }
}
