public class ThrSud extends Thread {
    Init gl = null;
    int curent = -1;
    public ThrSud(Init gl, int curent) {
        this.gl = gl;
        this.curent = curent;
        this.start();
    }
    public void run() {
        int i, j, k, iMin, jMin;
        Set temp = null, minim = null;
        char c;
        Impur im = null;
        synchronized (gl) { im = gl.listIm.get(new Integer(curent)); }
        for (;;) {
            // Determina celula libera cu minimul posibilitatilor de completare:
            // Daca nu exista celule libere ==> SOLUTIE
            // Daca exista o celula libera cu 0 posibilitati ==> ESEC
            // Completeaza toate celulele pentru care exista exact o
            // posibilitate.
            // Se iese din ciclu daca in toate celulele libere exista >= 2
            // posibilitati.
            minim = new Set(gl.n); minim.complement();
            iMin = -1;
            jMin = -1;
            for (i = 0; i < gl.n; i++) {
                for (j = 0; j < gl.n; j++) {
                    if (im.table[i][j] != Set.GOL) continue;
                    temp = new Set(gl.n);
                    temp.union_(im.ml[i]);
                    temp.intersection(im.mc[j]);
                    temp.intersection(im.mg[gl.ig[i][j]]);
                    if (temp.len() < minim.len()) {
                        iMin = i;
                        jMin = j;
                        minim = temp;
                    }
                }
            }
            if (iMin == -1 && im.table[0][0] == Set.GOL) {
                // Caz limita tabel complet gol
                iMin = 0;
                jMin = 0;
            }
            // Verifica daca exista solutie. Daca da, se semnaleaza si se
            // termina partialul.
            if (iMin == -1) {
                // Incrementarea numarului de solutii trebuie facuta sincronizat
                synchronized (gl) {
                    gl.solutii++;
                    prinT(im, gl, "SOLUTIA nr: "+gl.solutii);
                    gl.partiale--;
                    gl.listIm.remove(im);
                    if (gl.partiale == 0) gl.notify();
                }
                return; // Se termina partialul.
            }
            // Verifica inexistenta solutiei. Daca da, se semnaleaza si se
            // termina partialul.
            if (minim.len() == 0) {
                // Incrementarea numarului de esecuri trebuie facuta sincronizat
                synchronized (gl) {
                    gl.esecuri++;
                    prinT(im, gl, "ESEC nr: " + gl.esecuri);
                    gl.partiale--;
                    gl.listIm.remove(im);
                    if (gl.partiale == 0) gl.notify();
                }
                return;// Se termina threadul.
            }
            if (minim.len() == 1) {
                // Celula cu completare unica. Dupa fixarea valorii, se reiau
                // controalele in tabel.
                c = minim.getChar(0, gl.alfabet);
                im.table[iMin][jMin] = c;
                im.ml[iMin].delChar(c, gl.alfabet);
                im.mc[jMin].delChar(c, gl.alfabet);
                im.mg[gl.ig[iMin][jMin]].delChar(c, gl.alfabet);
                continue; // Cauta din nou in tabel dupa completare
            }
            // Paraseste ciclul deoarece toate celulele libere au macar 2
            // posibilitati de completare.
            break;
        }
        // Celula (iMin,jMin) poate contine oricare valoare din minim.
        // Se vor crea minim.size() noi partiale, punand in celula cate una
        // dintre valori.
        // Dupa crearea partialelor, partialul curent se termina.
        synchronized (gl) { gl.partiale += minim.len(); }
        for (k = 0; k < minim.len(); k++) {
            c = minim.getChar(k, gl.alfabet);
            // c este valoarea curenta care urmeaza a fi completata.
            // Creaza un obiect impur pentru un nou thread.
            Impur newIm = new Impur(gl.n);
            // Copiaza in noul obiect tabelul si multimile threadului curent.
            for (i = 0; i < gl.n; i++) {
                newIm.ml[i].union_(im.ml[i]);
                newIm.mc[i].union_(im.mc[i]);
                newIm.mg[i].union_(im.mg[i]);
                for (j = 0; j < gl.n; j++) newIm.table[i][j] = im.table[i][j];
            }
            // Fixeaza valoarea c in tabel si o sterge din multimile asociate.
            newIm.table[iMin][jMin] = c;
            newIm.ml[iMin].delChar(c, gl.alfabet);
            newIm.mc[jMin].delChar(c, gl.alfabet);
            newIm.mg[gl.ig[iMin][jMin]].delChar(c, gl.alfabet);
            // Creaza un nou partial.
            newIm.parinte = im.curent;
            synchronized (gl) {
                gl.curent++;
                newIm.curent = gl.curent;
                gl.listIm.put(new Integer(newIm.curent), newIm);
            }
            new ThrSud(gl, newIm.curent);
        }
        synchronized (gl) {
            prinT(im, gl, "Ramificare in ("+iMin+","+jMin+") cu "+minim.toStr(gl.alfabet));
            gl.partiale--;
            gl.listIm.remove(im);
            if (gl.partiale == 0) gl.notify();
        }
        // S-au creat cele minim.size() threaduri si se termina threadul curent.
    }
    // Tipareste, in regim sincronizat, continutul unui obiect impur.
    public static void prinT(Impur im, Init gl, String s) {
        int i, j;
        System.out.println();
        System.out.println("Partial " + im.curent + ", parinte " + im.parinte
                + ", total " + gl.curent + ", active " + gl.partiale + ". " + s);
        // Tipareste tabelul.
        for (i = 0; i < gl.n; i++) {
            for (j = 0; j < gl.n; j++) {
                System.out.print(im.table[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        for (i = 0; i < gl.n; i++) {
            System.out.println("ml[" + i + "]= " + im.ml[i].toStr(gl.alfabet));
        }
        System.out.println();
        for (i = 0; i < gl.n; i++) {
            System.out.println("mc[" + i + "]= " + im.mc[i].toStr(gl.alfabet));
        }
        System.out.println();
        for (i = 0; i < gl.n; i++) {
            System.out.println("mg[" + gl.ag.charAt(i) + "]= " + im.mg[i].toStr(gl.alfabet));
        }
        System.out.println();
    }
}
