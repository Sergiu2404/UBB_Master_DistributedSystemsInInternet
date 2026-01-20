#include <stdio.h>
#include <fcntl.h>
   main (int c, char *a[]) {
      int i, f;
      if (c != 3) {
        printf("Apel: counterInit numeFisier valoareInitialaContor");
        exit(1);
      }//if
      f = open(a[1], O_CREAT|O_WRONLY|O_TRUNC, 0666);
      i = atoi(a[2]);
      write(f, &i, sizeof(int));
      close(f);
   }//main

