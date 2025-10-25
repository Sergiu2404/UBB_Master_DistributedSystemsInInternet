// Se da un alfabet de n caractere alfanumerice,
// o matrice nXn partajata in n grupe de celule, fiecare grup avand n caractere
// si o matrice initiala a jocului.
// Orice joc, partial sau complet, trebuie sa aiba caractere din alfabet distincte
// pe fiecare linie, coloana sau grup de celule.
// Comentariul de la Init.c precizeaza modul de preluare a datelor initiale.
// Ja iesirea standard se dau toate jocurile partiale, parintele fiecaruia dintre jocuri,
// solutiile jocului si jocurile partiale care nu conduc la solutii.

// Programul principal
import java.io.*;
import java.util.*;
public class Sudoku {
    Init gl = null;
    public Sudoku() {
        int i, j;
        char c;
        try {
            gl = new Init(new BufferedReader(new InputStreamReader(System.in)));
        } catch (Exception e) { e.printStackTrace(); }
        System.out.println("Alfabet: " + gl.alfabet);
        System.out.println();
        System.out.println("Harta grupurilor de celule: ");
        for (i = 0; i < gl.n; i++) {
            for (j = 0; j < gl.n; j++) {
                System.out.print(gl.ag.charAt(gl.ig[i][j]));
            }
            System.out.println();
        }
        ThrSud.prinT(gl.initial, gl, "Problema initiala"); // Tipareste enuntul initial al problemei.
        gl.listIm.put(0, gl.initial);
        new ThrSud(gl, 0); // Lanseaza primul thread
        // Se asteapta terminarea tuturor partialelor active.
        synchronized (gl) {
            if (gl.partiale > 0) 
                try { gl.wait();} catch (Exception e) { ; }
        }
        System.out.println("Partiale "+(gl.curent + 1)+", esecuri "+gl.esecuri+", solutii "+gl.solutii+"\n");
    }
    public static void main(String a[]) {
        new Sudoku();
    }
}
