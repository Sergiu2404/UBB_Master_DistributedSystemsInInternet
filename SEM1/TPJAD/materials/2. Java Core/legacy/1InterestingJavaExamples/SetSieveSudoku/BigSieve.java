// Ciurul lui Eratostene cu operatii folosind multimi foarte mari de biti
// Se da la linia de comanda numarul maxim pana la care se cer numerele prime
// 98222287 numere prime <= 2000000000, ultimul 1999999973, durata 1844sec.
public class BigSieve{
    public static void main(String[] a) throws Exception {
        long n = Long.parseLong(a[0]), nb, s, i, p, t;
        System.out.println("Nr. prime <= "+n);
        nb = (n - 1) / 2;
        Pagini pagini = new Pagini("Fisier", nb);
        t = 1;
        System.out.print("2 ");
        // Pentru fiecare numar din multime sterge multiplii lui
        for (s = 0; ;s++) {
            // Cauta, incepand cu pozitia s, primul numar nesters
            for (; pagini.GetBit(s) == 0; s++);
            p = 2 * s + 3; // Este urmatorul numar prim
            if (s + p >= nb) break;// Nu mai avem ce sterge
            t++;
            System.out.print(p+" ");
            // Se sterg din multime toti multiplii lui p
            for (i = s + p; i < nb; pagini.PutBit(i, 0), i += p);
        }
        // Tipareste restul numerelor prime
        for (; s < nb; s++) {
            if (pagini.GetBit(s) != 0) {
                t++;
                System.out.printf((2 * s + 3)+" ");
            }
        }
        pagini.Close();
        System.out.println("\nTotal "+t+" numere prime <= "+n);
    }
}
