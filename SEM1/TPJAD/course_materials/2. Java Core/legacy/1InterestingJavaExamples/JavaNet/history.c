#include <stdio.h>
main () {
   int c;
   FILE *f;
   f = fopen("history", "a");
   for( ; ;) {
     c = getchar();
     if (c==EOF)
       break;
     putc(c, f);
   }//for
   c = '\n';
   putc(c, f);
   fclose(f);
   printf("Content-type: text/plain\n\n<html><head></head><body></body></html>");
}//main

