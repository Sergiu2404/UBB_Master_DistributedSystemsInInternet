#include <stdio.h>
#include <fcntl.h>
#include <errno.h>
   extern int errno;
   main (int c, char *a[]) {
      int i=-1, f;
      char s[256];
      fgets(s, 256, stdin);	// Citeste numele fisierului
      f = open(s, O_RDWR);		// Deschide fisierul
      if (f > 0) {
         lockf(f, F_LOCK, 0);		// Blocheaza fisierul pentru incrementare
         read(f, &i, sizeof(int));	// Citeste numarul
         i++;				// Incrementeaza
         lseek(f, 0, 0);		// Repozitioneaza la inceput
         write(f, &i, sizeof(int));	// Rescrie noul numar
         lockf(f, F_ULOCK, 0);	// Deblocheaza fisierul
         close(f);			// Inchide fisierul
      }//if
      else
         perror("");
      printf("Content-type: text/plain\n\n%d\n", i);	// Trimite numarul la client
   }//main

