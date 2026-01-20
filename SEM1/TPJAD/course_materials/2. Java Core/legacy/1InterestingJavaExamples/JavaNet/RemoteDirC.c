#include <stdio.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include "ConvertBytes.cpp" // Pentru conversiile lungimilor!
#define PORT_SERVER 8899
#define DIRSIZE 8192

   main (int c, char *a[]) {
      char dir[DIRSIZE];
      int sd, i, l;
      struct sockaddr_in serv_addr;
      struct hostent *hp;
      sd = socket (AF_INET, SOCK_STREAM, 0);
      hp = gethostbyname (a[1]);
      memset ((char *) &serv_addr, 0, sizeof (serv_addr));
      serv_addr.sin_family = AF_INET;
      memcpy((char *) &serv_addr.sin_addr.s_addr, hp->h_addr_list[0], 
hp->h_length);
      serv_addr.sin_port = htons (PORT_SERVER);
      connect (sd, (struct sockaddr *) &serv_addr,sizeof (serv_addr));

      addInt(strlen(a[2]), dir, 0); // Converteste la int Java
      send (sd, dir, 4, 0); // Trimite lungimea
      send (sd, a[2], strlen (a[2]), 0); // Trimite numele

      for (i=0; i<4; i++)
         recv (sd, &dir[i], 1, 0); // Citeste lungimea
      getInt(dir, 0, &l); // Converteste lungimea

      for (i=0; i<l; i++)
         recv (sd, &dir[i], 1, 0);
      printf ("%s\n", dir);
      close (sd);
   }//RemoteDirC.main
