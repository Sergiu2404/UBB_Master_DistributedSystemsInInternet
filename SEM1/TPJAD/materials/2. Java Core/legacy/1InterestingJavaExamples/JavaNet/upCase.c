#include <string.h>
#include <stdio.h>
   main () {
      char s[1000];
      int i;
      for(i=0; ;i++) {
         s[i] = getchar();
         if (s[i]==EOF)
            break;
      }//for
      s[i] = 0;
      for (i=0; i<strlen(s); i++)
         if((s[i]>='a')&&(s[i]<='z'))
            s[i]+='A'-'a';
      printf("Content-type: text/plain\n\n");
      printf ("%s\n\n",s);
   }//main

