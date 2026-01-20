import com.byteowls.jopencage.*;
import com.byteowls.jopencage.model.*;
import org.slf4j.*;
import java.io.*;
public class CenterRo {
    public static String[] parIn = {"east=", "west=", "south=", "north=", "step=", "country="};
    public static void main(String[] argv) throws Exception {
        String country="", s = "";
        int i, j, m, n, tOTAL=0;
        double east=P.Np, west=P.Np, south=P.Np, north=P.Np, step=P.Np, lat=0.0, lon=0.0, xg=0.0, yg=0.0, aREA=0.0;
        double[] t = new double[3], susy, susx, josy, josx;
        P nw=new P(), ne=new P(), sw=new P(), se=new P();
        JOpenCageGeocoder jOCG;
        JOpenCageReverseRequest req;
        JOpenCageResponse res;
        DataOutputStream fw; DataInputStream fr;
        for (i = 0; i < argv.length; i++) {
            for (j = 0; j < parIn.length; j++) {
                if (argv[i].startsWith(parIn[j])) break;
            }
            if       (j == 0) east  = P.s2d(argv[i].substring(5));
            else if  (j == 1) west  = P.s2d(argv[i].substring(5));
            else if  (j == 2) south = P.s2d(argv[i].substring(6));
            else if  (j == 3) north = P.s2d(argv[i].substring(6));
            else if  (j == 4) step  = P.s2d(argv[i].substring(5));
            else if  (j == 5) country = argv[i].substring(8);
            else continue;
        }
        if (P.isNp(east)||P.isNp(west)||P.isNp(south)||P.isNp(north)||P.isNp(step)||country.isEmpty()) {
            System.out.println("argv ERR or Incorrect!");
            System.exit(1);
        }
        for (m = 0, lat = north; lat >= south; m++, lat -= step);
        for (n = 0, lon = west;  lon <= east;  n++, lon += step);
        System.out.println("m "+m+" "+((north-south)/step)+" n "+n+" "+((east-west)/step));
        // http://api.opencagedata.com/geocode/v1
        jOCG = new JOpenCageGeocoder("4769666a5a524068ae426602be567766"); //ACEASTA CHEIE ESTE DEPASITA AZI PENTRU MINE
        fw = new DataOutputStream( new FileOutputStream("_points_"));
        for (i = 0, lat = north; i < m; i++, lat -= step) {
            for (j = 0, lon = west; j < n; j++, lon += step) {
                req = new JOpenCageReverseRequest(lat, lon);
                req.setNoAnnotations(true);
                s = jOCG.reverse(req).getResults().get(0).getFormatted();
                System.out.println("("+i+","+j+") lat="+lat+", lon="+lon+" "+s);
                if (s.endsWith(", "+country)) { fw.writeDouble(lat);  fw.writeDouble(lon);  }
                else                          { fw.writeDouble(P.Np); fw.writeDouble(P.Np); }
            }
        }
        fw.close();
        fr = new DataInputStream( new FileInputStream("_points_"));
        fw = new DataOutputStream( new FileOutputStream("_areas_"));
        susy = new double[n]; susx = new double[n]; josy = new double[n]; josx = new double[n];
        for (j = 0; j < n; j++) { susy[j] = fr.readDouble(); susx[j] = fr.readDouble(); }
        for (i = 0; i < m - 1; i++) {
            for (j = 0; j < n; j++) { josy[j] = fr.readDouble(); josx[j] = fr.readDouble(); }
            for (j = 0; j < n - 1; j++) {
                nw.y = susy[j]; nw.x = susx[j];
                ne.y = susy[j]; ne.x = susx[j + 1];
                se.y = josy[j]; se.x = josx[j + 1];
                sw.y = josy[j]; sw.x = josx[j];
                t = HaversineDistance.area(nw, ne, se, sw);
                if (P.isNp(t[0])) continue;
                aREA += t[0];
                tOTAL++;
                fw.writeDouble(t[0]); fw.writeDouble(t[1]); fw.writeDouble(t[2]);
            }
            for (j = 0; j < n; j++) { susy[j] = josy[j]; susx[j] = josx[j]; }
        }
        fw.close(); fr.close();
        System.out.println(tOTAL+" partial areas. Total area: "+aREA);
        fr = new DataInputStream( new FileInputStream("_areas_"));
        for (i = 0; i < tOTAL; i++) {
            t[0] = fr.readDouble();
            t[1] = fr.readDouble();
            t[2] = fr.readDouble();
            yg += t[0] / aREA * t[1];
            xg += t[0] / aREA * t[2];
        }
        fr.close();
        (new File("_points_")).delete(); (new File("_areas_")).delete();
        req = new JOpenCageReverseRequest(yg, xg);
        req.setNoAnnotations(true);
        s = (jOCG.reverse(req)).getResults().get(0).getFormatted();
        System.out.println("Center: lat="+yg+", lon="+xg+" "+s);
    }
}
