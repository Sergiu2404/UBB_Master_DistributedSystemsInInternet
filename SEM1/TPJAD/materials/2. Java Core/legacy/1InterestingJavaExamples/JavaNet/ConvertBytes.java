import java.io.*;
public class ConvertBytes {
   
   private static final int lengthJava[] = {1, 2, 4, 8, 4, 8};
   
   private static int toByteArray(int t, Object source, byte[] dest, int start) {
      if ((source==null)||(dest==null)||(start<0)||(dest.length<start+lengthJava[t]))
         return 0;
      byte b[];
      ByteArrayOutputStream ob = new ByteArrayOutputStream();
      DataOutputStream o = new DataOutputStream(ob);
      try {
         switch (t) {
            case 0: o.writeByte(((Byte)source).byteValue()); break;
            case 1: o.writeShort(((Short)source).shortValue()); break;
            case 2: o.writeInt(((Integer)source).intValue()); break;
            case 3: o.writeLong(((Long)source).longValue()); break;
            case 4: o.writeFloat(((Float)source).floatValue()); break;
            case 5: o.writeDouble(((Double)source).doubleValue()); break;
         }//switch
      }//try
      catch (IOException e) {
         e.printStackTrace();
      }//catch
      b = ob.toByteArray();
      System.arraycopy(b, 0, dest, start, lengthJava[t]);
      return lengthJava[t];
   }//ConvertBytes.toByteArray

   private static int fromByteArray(int t, byte[] source, int start, Object[] dest) {
      if ((source==null)||(dest==null)||(start<0)||(source.length<start+lengthJava[t])||(dest.length<1))
         return 0;
      ByteArrayInputStream ib = new ByteArrayInputStream(source, start, lengthJava[t]);
      DataInputStream i = new DataInputStream(ib);
      Object ob = null;
      try {
         switch (t) {
            case 0: ob = new Byte(i.readByte()); break;
            case 1: ob = new Short(i.readShort()); break;
            case 2: ob = new Integer(i.readInt()); break;
            case 3: ob = new Long(i.readLong()); break;
            case 4: ob = new Float(i.readFloat()); break;
            case 5: ob = new Double(i.readDouble()); break;
         }//switch
      }//try
      catch (IOException e) {
         e.printStackTrace();
      }//catch
      dest[0] = ob;
      return lengthJava[t];
   }//ConvertBytes.fromByteArray
   
   public static int addByte(byte source, byte[] dest, int start) {
      return toByteArray(0, new Byte(source), dest, start);
   }//ConvertBytes.addByte
   public static int addShort(short source, byte[] dest, int start) {
      return toByteArray(1, new Short(source), dest, start);
   }//ConvertBytes.addShort
   public static int addInt(int source, byte[] dest, int start) {
      return toByteArray(2, new Integer(source), dest, start);
   }//ConvertBytes.addInt
   public static int addLong(long source, byte[] dest, int start) {
      return toByteArray(3, new Long(source), dest, start);
   }//ConvertBytes.addLong
   public static int addFloat(float source, byte[] dest, int start) {
      return toByteArray(4, new Float(source), dest, start);
   }//ConvertBytes.addFloat
   public static int addDouble(double source, byte[] dest, int start) {
      return toByteArray(5, new Double(source), dest, start);
   }//ConvertBytes.addFloat
   public static int getByte(byte[] source, int start, Byte[] dest) {
      return fromByteArray(0, source, start, dest);
   }//ConvertBytes.getByte
   public static int getShort(byte[] source, int start, Short[] dest) {
      return fromByteArray(1, source, start, dest);
   }//ConvertBytes.getShort
   public static int getInt(byte[] source, int start, Integer[] dest) {
      return fromByteArray(2, source, start, dest);
   }//ConvertBytes.getInt
   public static int getLong(byte[] source, int start, Long[] dest) {
      return fromByteArray(3, source, start, dest);
   }//ConvertBytes.getLong
   public static int getFloat(byte[] source, int start, Float[] dest) {
      return fromByteArray(4, source, start, dest);
   }//ConvertBytes.getFloat
   public static int getDouble(byte[] source, int start, Double[] dest) {
      return fromByteArray(5, source, start, dest);
   }//ConvertBytes.getDouble
   public static int getLength(int t) {
      if ((t<0)||(t>5))
         return 0;
      return lengthJava[t];
   }//ConvertBytes.getLength
}//ConvertBytes

