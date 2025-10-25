// Creaza un obiect IMPUR pentru un joc partial:
// Tabloul de joc va avea peste tot GOL, iar cele 3*gl.n multimi vor fi vide
public class Impur {
    int curent = -1; // Numarul partialului
    int parinte = -1; // Numarul partialului parinte
    char[][] table = null; // Tabelul partial
    Set[] ml = null; // Multimile pentru linii
    Set[] mc = null; // Multimile pentru coloane
    Set[] mg = null; // Multimile pentru grupuri
    public Impur(int n) {
        table = new char[n][n];
        ml = new Set[n];
        mc = new Set[n];
        mg = new Set[n];
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) table[i][j] = Set.GOL;
            ml[i] = new Set(n);
            mc[i] = new Set(n);
            mg[i] = new Set(n);
        }
    }
}
