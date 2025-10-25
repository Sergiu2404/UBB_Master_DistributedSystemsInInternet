#include <stdio.h>
#include <string.h>
static int lengthJava[] = {1,2,4,8,4,8};
static int lengthC[] = {sizeof(char),sizeof(short),sizeof(int),
sizeof(long),sizeof(float),sizeof(double)};
static int first=1;

   int getLength(int t) {
   // Intoarce 0 la eroare de apel
   // >0 lungime reprezentare bigendian
   // <0 -lungime reprezentare littleendian
      if ((t<0)||(t>5))
         return 0;
      if (first) {
         int i;
          union {
            char c;
            int i;
         } x;
         first = 0;
         x.i = 1;
         if (x.c == 1)
            for (i=1; i<6; i++)
               lengthC[i] = -lengthC[i];
      }//if
      return lengthC[t];
   }//ConvertBytes.lengthOrd

   int C2Java(int t, char *source, char *dest) {
   // Intoarce 0 la eroare de apel
   // >0 lungimea transferata in dest
      int i, j, k;
      char c, copy[32];
      k = getLength(t);
      if ((k==0)||(source==NULL)||(dest==NULL))
         return 0;
      if (t==0) {
         dest[0] = source[0];
         return 1;
      }//if
      if (k<0) {
         k = -k;
         for (i=0, j=k-1; i<k; i++,j--)
            copy[i] = source[j];
      } 
      else
         memcpy(copy, source, k);
      if (k==lengthJava[t])
         memcpy(dest, copy, k);
      if (k>lengthJava[t]) {
         if (t>3)
            memcpy(dest, copy, lengthJava[t]);
         else
            memcpy(dest, copy+k-lengthJava[t], lengthJava[t]);
      }//if
      if (k<lengthJava[t]) {
         c = 0;
         if ((t<4)&&(copy[0]<0))
            c = 0xff;
         memset(dest, c, lengthJava[t]);
         if (t>3)
            memcpy(dest, copy, lengthJava[t]);
         else
            memcpy(dest+lengthJava[t]-k, copy, lengthJava[t]);
      }//if
      return lengthJava[t];
   }//ConvertBytes.C2Java

   int Java2C(int t, char *source, char *dest) {
   // Intoarce 0 la eroare de apel
   // >0 lungimea transferata in dest
      int i, j, k, x;
      char c, copy[32];
      k = getLength(t);
      if ((k==0)||(source==NULL)||(dest==NULL))
         return 0;
      if (t==0) {
         dest[0] = source[0];
         return 1;
      }//if
      x = 1;
      if (k<0) {
         x = -1;
         k = -k;
      }//if
      if (k==lengthJava[t])
         memcpy(copy, source, k);
      if (k<lengthJava[t]) {
         if (t>3)
            memcpy(copy, source, k);
         else
            memcpy(copy, source+lengthJava[t]-k, k);
      }//if
      if (k>lengthJava[t]) {
         c = 0;
         if ((t<4)&&(source[0]<0))
            c = 0xff;
         memset(copy, c, k);
         if (t>3)
            memcpy(copy, source, lengthJava[t]);
         else
            memcpy(copy, source+k-lengthJava[t], lengthJava[t]);
      }//if
      if (x>0)
         memcpy(dest, copy, k);
      else
         for (i=0, j=k-1; i<k; i++,j--)
            dest[i] = copy[j];
      return k;
   }//ConvertBytes.Java2C

    int addByte(char source, char *dest, int start) {
      return C2Java(0, (char *)&source, dest+start);
   }//ConvertBytes.addByte
    int addShort(short source, char *dest, int start) {
      return C2Java(1, (char *)&source, dest+start);
   }//ConvertBytes.addShort
    int addInt(int source, char *dest, int start) {
      return C2Java(2, (char *)&source, dest+start);
   }//ConvertBytes.addInt
    int addLong(long source, char *dest, int start) {
      return C2Java(3, (char *)&source, dest+start);
   }//ConvertBytes.addLong
    int addFloat(float source, char *dest, int start) {
      return C2Java(4, (char *)&source, dest+start);
   }//ConvertBytes.addFloat
    int addDouble(double source, char *dest, int start) {
      return C2Java(5, (char *)&source, dest+start);
   }//ConvertBytes.addDouble
    int getByte(char *source, int start, char *dest) {
      return Java2C(0, source+start, (char *)dest);
   }//ConvertBytes.getByte
    int getShort(char *source, int start, short *dest) {
      return Java2C(1, source+start, (char *)dest);
   }//ConvertBytes.getShort
    int getInt(char *source, int start, int *dest) {
      return Java2C(2, source+start, (char *)dest);
   }//ConvertBytes.getInt
    int getLong(char *source, int start, long *dest) {
      return Java2C(3, source+start, (char *)dest);
   }//ConvertBytes.getLong
    int getFloat(char *source, int start, float *dest) {
      return Java2C(4, source+start, (char *)dest);
   }//ConvertBytes.getFloat
    int getDouble(char *source, int start, double *dest) {
      return Java2C(5, source+start, (char *)dest);
   }//ConvertBytes.getDouble

