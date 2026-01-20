% =========================================================================
% ANTRENAMENT AVANSAT - SCM (Stil Examen: Subpuncte a) si b))
% =========================================================================
% Acest script contine 10 probleme complexe, care combina distributiile
% din Seminarele 1 si 2. Fiecare problema are doua cerinte (probabilitati
% si valori asteptate/medii), exact ca in modelul de examen.
%
% INCLUDE:
% - Explicatii "Babesti" (Analogii).
% - LOGICA ALGORITMULUI (Descriere in limbaj natural).
% - Rezolvare Monte Carlo pas cu pas.
% - Verificare cu Formule Exacte.
% =========================================================================

clear all; clc;
format long g;

% --- CONFIGURARE GENERALA (Standard Examen) ---
err = 0.005;      % Eroare mai mica pentru precizie mai mare
alpha_sig = 0.05; % 95% incredere
% Calculam N (Dimensiunea esantionului)
N = ceil(0.25 * (norminv(alpha_sig/2, 0, 1)/err)^2);
fprintf('Dimensiunea studiului Monte Carlo: N = %d\n', N);
fprintf('Apasati ENTER pentru a parcurge problemele una cate una.\n\n');
pause; clc;

%% ========================================================================
% PROBLEMA 1: Binomiala - Test Grila "La Ghici"
% ========================================================================
% SCENARIU:
% Un student nepregatit da un examen grila cu n = 20 de intrebari.
% Fiecare intrebare are 4 variante, deci probabilitatea de succes (ghicire)
% este p = 0.25. Pentru a trece, trebuie sa raspunda corect la macar 10.
%
% CERINTE:
% a) Probabilitatea ca studentul sa TREACA examenul (>= 10 corecte).
% b) Numarul asteptat (mediu) de raspunsuri corecte.
%
% ANALOGIE:
% E ca si cum ai da cu un zar cu 4 fete de 20 de ori. "1" e succes, restul esec.
% Numaram de cate ori iese "1".
%
% LOGICA ALGORITMULUI:
% 1. Vom rula N simulari (studenti virtuali).
% 2. Pentru fiecare student, generam 20 de numere aleatoare (intrebari).
% 3. Daca un numar generat e < 0.25, consideram raspuns corect.
% 4. Numaram cate raspunsuri corecte are fiecare student si salvam in vectorul X1.
% 5. Pentru a), numaram cati studenti au >= 10 si impartim la N.
% 6. Pentru b), facem media tuturor notelor obtinute.
% =========================================================================
fprintf('--- PROBLEMA 1: Binomiala (Examen Grila) ---\n');

n_intrebari = 20;
p_corect = 0.25;
X1 = zeros(1, N);

% --- SIMULARE ---
for i = 1:N
    % Simulam 20 de incercari Bernoulli si le insumam
    % (Rand < 0.25 genereaza 1 cu prob 25%, 0 in rest)
    intrebari_ghicite = sum(rand(1, n_intrebari) < p_corect);
    X1(i) = intrebari_ghicite;
end

% --- REZOLVARE a) ---
% Probabilitatea X >= 10
prob_a_sim = mean(X1 >= 10);
prob_a_teo = 1 - binocdf(9, n_intrebari, p_corect); % 1 - P(X <= 9)

fprintf('a) Probabilitate de promovare (>=10 corecte):\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% Valoarea asteptata E[X]
mean_b_sim = mean(X1);
mean_b_teo = n_intrebari * p_corect; % Formula n*p

fprintf('b) Numar asteptat de raspunsuri corecte:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', mean_b_sim, mean_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 2: Geometrica - Hackerul Persistent
% ========================================================================
% SCENARIU:
% Un hacker incearca sa sparga o parola. Are o sansa p = 0.05 sa reuseasca
% la fiecare incercare (independent). Se opreste cand reuseste.
%
% CERINTE:
% a) Probabilitatea sa reuseasca EXACT la a 10-a incercare.
% b) Probabilitatea sa aiba nevoie de MAI MULT de 20 de incercari.
%
% ANALOGIE:
% Cauti cheia potrivita intr-un manunchi infinit. O incerci pe prima, pe a doua...
% Geometrica numara cate esecuri ai avut pana ai gasit-o.
%
% LOGICA ALGORITMULUI:
% 1. Nu stim cate incercari face hackerul, deci nu putem folosi un "for" fix
%    pentru procesul in sine, dar stim distributia (Geometrica).
% 2. Folosim Metoda Transformarii Inverse (ITM) pentru a genera direct rezultatul.
% 3. Generam un numar U uniform (0,1).
% 4. Aplicam formula ITM: Nr_esecuri = ceil(log(1-U)/log(1-p)) - 1.
% 5. Nr_total_incercari = Nr_esecuri + 1 (succesul final).
% 6. Repetam de N ori si analizam rezultatele.
% =========================================================================
fprintf('--- PROBLEMA 2: Geometrica (Hacker) ---\n');

p_hack = 0.05;
X2 = zeros(1, N); % Va stoca numarul de incercari NECESARE (succesul inclus)

% --- SIMULARE (Metoda ITM) ---
for i = 1:N
    U = rand;
    % Formula Geometrica (numar esecuri inainte de succes): ceil(ln(1-U)/ln(1-p)) - 1
    esecuri = ceil(log(1-U) / log(1-p_hack)) - 1;
    % Numarul total de incercari = esecuri + 1 (succesul)
    X2(i) = esecuri + 1;
end

% --- REZOLVARE a) ---
% P(X = 10)
prob_a_sim = mean(X2 == 10);
prob_a_teo = geopdf(9, p_hack); % Matlab numara esecurile (9 esecuri = succes la a 10-a)

fprintf('a) Probabilitatea succesului exact la incercarea 10:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% P(X > 20)
prob_b_sim = mean(X2 > 20);
prob_b_teo = 1 - geocdf(19, p_hack); % 1 - P(esecuri <= 19)

fprintf('b) Probabilitatea sa fie nevoie de > 20 incercari:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', prob_b_sim, prob_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 3: Binomiala Negativa - Recrutare Angajati (Stil Examen)
% ========================================================================
% SCENARIU:
% O companie vrea sa angajeze k = 3 experti. Doar 20% (p=0.2) dintre
% candidatii intervievati sunt calificati.
%
% CERINTE:
% a) Probabilitatea ca HR-ul sa intervieveze in total 10 persoane pentru
%    a-i gasi pe cei 3. (Adica 7 respinsi + 3 admisi).
% b) Numarul asteptat de candidati RESPINSI pana se completeaza echipa.

% --- PARAMETRI ---
k_target = 3;     % Numarul de experti de angajat (Succese dorite)
p_qual = 0.2;     % Probabilitatea de a fi expert (Succes)
err = 0.005; alpha_sig = 0.05;
N = ceil(0.25 * (norminv(alpha_sig/2, 0, 1)/err)^2);

fprintf('Dimensiune MC: N = %d\n', N);

X3_respinsi = zeros(1, N); % Variabila stocheaza ESSECURILE (Respinsii)

% --- LOGICA ALGORITMULUI (WHILE LOOP) ---
% 1. Rulam N simulari (o simulare = o campanie de recrutare).
% 2. In fiecare simulare, initializam: experti_gasiti = 0 si respinsi = 0.
% 3. Incepem o bucla WHILE care ruleaza ATAT TIMP cat experti_gasiti < 3.
% 4. In interiorul buclei, generam un rand (simulam un interviu).
% 5. Daca rand < p (0.2), expertul este gasit (incrementam experti_gasiti).
% 6. Altfel, expertul este respins (incrementam respinsi).
% 7. Cand bucla se termina, variabila 'respinsi' contine rezultatul Monte Carlo.
% -------------------------------------------

for i = 1:N
    experti_gasiti = 0; % Contor pentru Succese
    respinsi = 0;       % Contor pentru Esecuri (Variabila X)

    while experti_gasiti < k_target
        if rand < p_qual
            % Succes: Am gasit un expert
            experti_gasiti = experti_gasiti + 1;
        else
            % Esec: Candidat respins
            respinsi = respinsi + 1;
        end
    end

    X3_respinsi(i) = respinsi;
end

% --- REZOLVARE a) ---
% CERINTA: Probabilitatea sa intervievam TOTAL 10 persoane.
% Asta inseamna: 3 Admisi + 7 Respinsi. Cautam P(X_respinsi == 7).
prob_a_sim = mean(X3_respinsi == 7);
prob_a_teo = nbinpdf(7, k_target, p_qual);

fprintf('a) Probabilitate total 10 interviuri (7 respinsi):\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% Numarul mediu de respinsi (E[X])
mean_b_sim = mean(X3_respinsi);
mean_b_teo = k_target * (1 - p_qual) / p_qual;

fprintf('b) Numar asteptat de respinsi:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', mean_b_sim, mean_b_teo);

%% ========================================================================
% PROBLEMA 4: Poisson - Defecte pe Cablu
% ========================================================================
% SCENARIU:
% Un cablu de fibra optica are defecte care apar aleatoriu cu o medie de
% lambda = 0.5 defecte pe metru. Analizam o bucata de L = 10 metri.
% Media totala lambda_total = 0.5 * 10 = 5.
%
% CERINTE:
% a) Probabilitatea sa gasim EXACT 3 defecte pe cei 10 metri.
% b) Probabilitatea sa gasim CEL PUTIN 2 defecte.
%
% ANALOGIE:
% Ploaia. Nu stii cand cade o picatura, dar stii cat se uda pamantul intr-o ora.
% Aici "ploua" cu defecte pe lungimea cablului.
%
% LOGICA ALGORITMULUI:
% 1. Folosim "Metoda Speciala" (produsul uniformelor) pentru generare Poisson.
% 2. Conceptul: Inmultim numere aleatoare subunitare (rand) intre ele.
%    Produsul scade treptat.
% 3. Continuam inmultirea cat timp produsul este >= exp(-lambda).
% 4. Numarul de inmultiri necesare pentru a scadea sub limita este variabila Poisson.
% 5. Repetam de N ori pentru a crea esantionul.
% =========================================================================
fprintf('--- PROBLEMA 4: Poisson (Defecte Cablu) ---\n');

lam_per_m = 0.5;
Len = 10;
lam_tot = lam_per_m * Len;
X4 = zeros(1, N);

% --- SIMULARE (Metoda Speciala / Inmultire Uniforme) ---
Limit = exp(-lam_tot);
for i = 1:N
    produs = 1;
    k = 0;
    while produs >= Limit
        produs = produs * rand;
        k = k + 1;
    end
    X4(i) = k - 1;
end

% --- REZOLVARE a) ---
% P(X = 3)
prob_a_sim = mean(X4 == 3);
prob_a_teo = poisspdf(3, lam_tot);

fprintf('a) Probabilitate exact 3 defecte:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% P(X >= 2) = 1 - P(X <= 1)
prob_b_sim = mean(X4 >= 2);
prob_b_teo = 1 - poisscdf(1, lam_tot);

fprintf('b) Probabilitate cel putin 2 defecte:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', prob_b_sim, prob_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 5: Exponentiala - Durata de Viata a unui Bec
% ========================================================================
% SCENARIU:
% Un bec LED are o durata de viata exponentiala cu media de 5000 ore.
% (Deci lambda = 1/5000).
%
% CERINTE:
% a) Probabilitatea ca becul sa se arda in primele 2000 de ore.
% b) Daca becul a functionat deja 1000 de ore, care e probabilitatea sa mai
%    functioneze INCA 2000 de ore? (Proprietatea Memoryless).
%
% ANALOGIE:
% Exponentiala e singura distributie continua care "nu imbatraneste".
% Un bec vechi are aceleasi sanse sa se arda maine ca unul nou.
%
% LOGICA ALGORITMULUI:
% 1. Este o variabila continua. Folosim ITM: X = -Media * ln(U).
% 2. Generam N durate de viata.
% 3. Pentru a), calculam proportia valorilor < 2000.
% 4. Pentru b), selectam doar becurile care au supravietuit > 1000 ore (subset).
% 5. In acest subset, calculam proportia celor care traiesc > 3000 (1000+2000).
% =========================================================================
fprintf('--- PROBLEMA 5: Exponentiala (Bec LED) ---\n');

mu_bec = 5000; % Media
X5 = zeros(1, N);

% --- SIMULARE (ITM) ---
for i = 1:N
    X5(i) = -mu_bec * log(rand);
end

% --- REZOLVARE a) ---
% P(X < 2000)
prob_a_sim = mean(X5 < 2000);
prob_a_teo = expcdf(2000, mu_bec);

fprintf('a) Probabilitate defectare < 2000 ore:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% P(X > 1000 + 2000 | X > 1000). Conform teoriei, asta e P(X > 2000).
% Verificam prin simulare conditionata.
supravietuitori_1000 = X5(X5 > 1000); % Filtram becurile care au atins 1000h
% Din acestia, cati trec de 3000h (1000 + 2000)?
prob_b_sim = mean(supravietuitori_1000 > 3000);
prob_b_teo = 1 - expcdf(2000, mu_bec); % P(X > 2000)

fprintf('b) Probabilitate conditionata (Memoryless):\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', prob_b_sim, prob_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 6: Gamma - Timp Procesare Server (Erlang)
% ========================================================================
% SCENARIU:
% O cerere web trece prin 3 servere (Database, App, Frontend) secvential.
% Fiecare server proceseaza cererea intr-un timp Exp(media = 2 secunde).
% Timpul total este suma a 3 Exponetiale => Gamma(3, 2).
%
% CERINTE:
% a) Probabilitatea ca cererea sa fie gata in SUB 4 secunde.
% b) Timpul mediu TOTAL de procesare.
%
% LOGICA ALGORITMULUI:
% 1. "Secvential" inseamna ca timpii se aduna.
% 2. Generam 3 variabile independente Exponetiale (folosind ITM ca la prob 5).
% 3. Timpul total al sistemului = T1 + T2 + T3.
% 4. Facem asta de N ori. Analizam vectorul de sume rezultate.
% =========================================================================
fprintf('--- PROBLEMA 6: Gamma (Procesare Secventiala) ---\n');

etape = 3;
mu_server = 2;
X6 = zeros(1, N);

% --- SIMULARE ---
for i = 1:N
    % Suma a 3 variabile exponentiale independente
    % (Generare eficienta: -mu * log(produs_uniforme))
    t1 = -mu_server * log(rand);
    t2 = -mu_server * log(rand);
    t3 = -mu_server * log(rand);
    X6(i) = t1 + t2 + t3;
end

% --- REZOLVARE a) ---
% P(Total < 4)
prob_a_sim = mean(X6 < 4);
prob_a_teo = gamcdf(4, etape, mu_server); % In Matlab: shape=etape, scale=mu

fprintf('a) Probabilitate timp total < 4 sec:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% E[Total]
mean_b_sim = mean(X6);
mean_b_teo = etape * mu_server; % Suma mediilor

fprintf('b) Timp mediu total:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', mean_b_sim, mean_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 7: Fiabilitate Sistem Paralel (Min/Max)
% ========================================================================
% SCENARIU:
% Un avion are 2 motoare. Avionul zboara daca merge MACAR un motor.
% Motorul A: Exp(media = 1000h).
% Motorul B: Exp(media = 1200h).
% Timpul de viata al avionului = max(TA, TB).
%
% CERINTE:
% a) Probabilitatea ca avionul sa zboare mai mult de 1500 ore.
% b) Probabilitatea ca AMBELE motoare sa cedeze inainte de 800 ore.
%
% LOGICA ALGORITMULUI:
% 1. Sistem Paralel inseamna redundanta. Sistemul cade cand ULTIMA componenta cade.
% 2. Generam durata de viata pentru A (ITM Exp).
% 3. Generam durata de viata pentru B (ITM Exp).
% 4. Durata sistem = max(Ta, Tb).
% 5. Pentru punctul b), verificam o conditie logica: (Ta < 800) AND (Tb < 800).
% =========================================================================
fprintf('--- PROBLEMA 7: Sistem Paralel (Motoare Avion) ---\n');

muA = 1000;
muB = 1200;
T_avion = zeros(1, N);
T_ambele_cedate = zeros(1, N); % Variabila indicator pentru b)

% --- SIMULARE ---
for i = 1:N
    tA = -muA * log(rand);
    tB = -muB * log(rand);

    T_avion(i) = max(tA, tB); % Paralel

    % Pentru b) verificam daca ambele sunt < 800
    if (tA < 800) && (tB < 800)
        T_ambele_cedate(i) = 1;
    else
        T_ambele_cedate(i) = 0;
    end
end

% --- REZOLVARE a) ---
% P(Avion > 1500)
prob_a_sim = mean(T_avion > 1500);
% Teoretic: P(max > t) = 1 - P(max <= t) = 1 - P(A<=t)*P(B<=t)
pA_fail = expcdf(1500, muA);
pB_fail = expcdf(1500, muB);
prob_a_teo = 1 - (pA_fail * pB_fail);

fprintf('a) Probabilitate avion > 1500h:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% P(A < 800 AND B < 800)
prob_b_sim = mean(T_ambele_cedate);
prob_b_teo = expcdf(800, muA) * expcdf(800, muB);

fprintf('b) Probabilitate ambele motoare < 800h:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', prob_b_sim, prob_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 8: Probabilitati Totale - Fabrica de Piese
% ========================================================================
% SCENARIU:
% O fabrica are 2 linii de productie.
% Linia 1: Produce 60% din piese, rata defecte 1%.
% Linia 2: Produce 40% din piese, rata defecte 5%.
%
% CERINTE:
% a) Care este probabilitatea ca o piesa aleasa aleator sa fie DEFECTA?
% b) (Bayes Simulat) Daca o piesa e defecta, care e sansa sa vina de la Linia 2?
%
% LOGICA ALGORITMULUI:
% 1. Simulam procesul decizional (Arborele de probabilitati).
% 2. Pasul 1: Generam un rand. Daca e < 0.6, piesa vine de la Linia 1. Altfel Linia 2.
% 3. Pasul 2: In functie de Linie, generam alt rand. Daca e < rata_defect (0.01 sau 0.05), marcam ca defecta.
% 4. Salvam starea (Defect/Bun) si Sursa (L1/L2) pentru fiecare piesa.
% 5. Pentru b), analizam doar piesele marcate ca "Defect". Calculam cate sunt de la L2.
% =========================================================================
fprintf('--- PROBLEMA 8: Probabilitate Totala (Defecte) ---\n');

X8_defect = zeros(1, N);
X8_sursa = zeros(1, N); % 1 pentru Linia 1, 2 pentru Linia 2

% --- SIMULARE ---
for i = 1:N
    % Pas 1: Alegem linia
    if rand < 0.6
        sursa = 1;
        rata = 0.01;
    else
        sursa = 2;
        rata = 0.05;
    end
    X8_sursa(i) = sursa;

    % Pas 2: Verificam daca e defecta
    if rand < rata
        X8_defect(i) = 1;
    else
        X8_defect(i) = 0;
    end
end

% --- REZOLVARE a) ---
% P(Defect)
prob_a_sim = mean(X8_defect);
prob_a_teo = 0.6*0.01 + 0.4*0.05;

fprintf('a) Rata totala de defecte:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% P(Sursa=2 | Defect=1)
% Filtram doar cazurile defecte
indici_defecte = find(X8_defect == 1);
surse_defecte = X8_sursa(indici_defecte);
% Calculam cat la suta din acestea sunt de la Linia 2
prob_b_sim = mean(surse_defecte == 2);
% Teoretic Bayes: P(L2|D) = P(D|L2)P(L2) / P(D)
prob_b_teo = (0.05 * 0.4) / prob_a_teo;

fprintf('b) Probabilitate ca un defect sa vina de la Linia 2:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n\n', prob_b_sim, prob_b_teo);
pause; clc;

%% ========================================================================
% PROBLEMA 9: Poisson - Aproximarea Binomialei
% ========================================================================
% SCENARIU:
% O boala rara afecteaza 1 din 1000 de persoane (p=0.001).
% Intr-un oras cu n = 5000 de locuitori:
%
% CERINTE:
% a) Folosind distributia EXACTA (Binomiala), care e probabilitatea sa fie
%    exact 5 bolnavi?
% b) Verificati eroarea facuta daca am fi aproximat cu Poisson (lambda = n*p).
%
% ANALOGIE:
% Cand p e foarte mic si n foarte mare, Binomiala devine Poisson.
%
% LOGICA ALGORITMULUI:
% 1. Simulam variabila Binomiala "brut": generam 5000 de numere pentru fiecare oras.
% 2. Numaram cati oameni sunt bolnavi (rand < 0.001) in fiecare oras.
% 3. Repetam pentru N_mic orase (ca sa nu dureze prea mult).
% 4. Calculam probabilitatea simulata si o comparam cu valoarea teoretica Poisson.
% =========================================================================
fprintf('--- PROBLEMA 9: Poisson vs Binomiala (Boala Rara) ---\n');

n_pop = 5000;
p_boala = 0.001;
X9 = zeros(1, N);

% --- SIMULARE (Binomiala - suma de Bernoulli) ---
% Nota: Facem loop doar pentru un subset de N (ex N=10000) sa nu blocam PC-ul.
N_mic = 10000;
X9_mic = zeros(1, N_mic);
for i = 1:N_mic
   X9_mic(i) = sum(rand(1, n_pop) < p_boala);
end

% --- REZOLVARE a) ---
prob_a_sim = mean(X9_mic == 5);
prob_a_teo = binopdf(5, n_pop, p_boala);

fprintf('a) Probabilitate exact 5 bolnavi (Binomial Exact):\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% Comparatie cu Poisson(lambda = 5000 * 0.001 = 5)
val_poisson = poisspdf(5, 5);
eroare = abs(prob_a_teo - val_poisson);

fprintf('b) Valoare aproximata Poisson si Eroarea:\n');
fprintf('   Poisson: %1.5f | Eroare vs Binom: %e\n\n', val_poisson, eroare);
pause; clc;

%% ========================================================================
% PROBLEMA 10: Combinata - Joc de Noroc in Etape
% ========================================================================
% SCENARIU:
% Joci un joc unde mai intai dai cu zarul (1-6).
% Daca dai 6, intri in "Runda Bonus".
% In Runda Bonus, arunci o moneda pana iese pajura (Geometric p=0.5).
% Castigi 10$ pentru fiecare aruncare de moneda din bonus.
%
% CERINTE:
% a) Probabilitatea sa NU intri deloc in Runda Bonus.
% b) Castigul mediu asteptat al unui jucator.
%
% ANALOGIE:
% Probabilitate conditionata amestecata cu valori asteptate.
% Castig = 0 (daca nu intri) SAU 10 * X_geo (daca intri).
%
% LOGICA ALGORITMULUI:
% 1. Este o simulare conditionata (if/else).
% 2. Generam un rand (Zar). Daca e < 1/6 (echivalent cu a da 6), intram in Bonus.
% 3. Daca intram in Bonus: Simulam Geometrica (ITM) pentru moneda. Calculam castigul.
% 4. Daca nu intram: Castigul e 0.
% 5. Facem media castigurilor pe N jocuri.
% =========================================================================
fprintf('--- PROBLEMA 10: Joc Compus (Zar + Moneda) ---\n');

X10_castig = zeros(1, N);
p_intrare = 1/6; % Sansa sa dai 6 la zar
p_moneda = 0.5;

% --- SIMULARE ---
for i = 1:N
    % Pas 1: Zarul
    if rand < p_intrare
        % Am intrat in bonus!
        % Pas 2: Moneda (Geometric) - cate aruncari pana la pajura?
        % Atentie: Geometric in Matlab e numar esecuri. Noi vrem numar aruncari (esecuri+1).
        nr_aruncari = (ceil(log(1-rand)/log(1-p_moneda)) - 1) + 1;
        X10_castig(i) = 10 * nr_aruncari;
    else
        % Nu am intrat
        X10_castig(i) = 0;
    end
end

% --- REZOLVARE a) ---
% P(Castig = 0)
prob_a_sim = mean(X10_castig == 0);
prob_a_teo = 1 - p_intrare; % Sansa sa nu dai 6 (5/6)

fprintf('a) Probabilitate sa nu intri in bonus:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', prob_a_sim, prob_a_teo);

% --- REZOLVARE b) ---
% E[Castig] = P(Intrare) * E[Castig|Intrare] + P(Nu) * 0
% E[Castig|Intrare] = 10 * E[Geo(0.5)] = 10 * (1/0.5) = 10 * 2 = 20
mean_b_sim = mean(X10_castig);
mean_b_teo = (1/6) * 20 + (5/6) * 0;

fprintf('b) Castig mediu asteptat:\n');
fprintf('   Simulat: %1.5f | Teoretic: %1.5f\n', mean_b_sim, mean_b_teo);

fprintf('\n--- FELICITARI! Ai terminat setul avansat. ---\n');


