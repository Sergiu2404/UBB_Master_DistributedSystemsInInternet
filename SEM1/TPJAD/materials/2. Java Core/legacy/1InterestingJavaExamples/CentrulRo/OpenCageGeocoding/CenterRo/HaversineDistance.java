public class HaversineDistance {
    static final double R = 6371; // Radious of the earth (km)
    public static double dist(double lat1, double lon1, double lat2, double lon2) {
        // https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
        if (P.isNp(lat1) || P.isNp(lon1) || P.isNp(lat2) || P.isNp(lon2)) return P.Np;
        Double dlat = Math.toRadians(lat2 - lat1);
        Double dlon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dlat/2), 2) + Math.pow(Math.sin(dlon/2), 2) * Math.cos(lat1) * Math.cos(lat2);
        return R * 2 * Math.asin(Math.sqrt(a));
    }
    public static double dist(P p1, P p2) { return dist(p1.y, p1.x, p2.y, p2.x); }
    public static  double[] area(P A, P B, P C) {
        // Return area, yg, xg
        double a = dist(B, C);
        double b = dist(A, C);
        double c = dist(B, A);
        double[] t = {P.Np, 0.0, 0.0};
        if (P.isNp(a) || P.isNp(b) || P.isNp(c) ) return t;
        double p = (a + b + c) / 2;
        t[0] = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        t[1] = (A.y + B.y + C.y) / 3;
        t[2] = (A.x + B.x + C.x) / 3;
        return t;
    }
    public static double[] area(P nw, P ne, P se, P sw) {
        // Mandatory convex quadrilateral!
        //   nw - ne
        //   |     |
        //   sw - se
        int nNp = 0;
        if (nw.isNp()) nNp++;
        if (ne.isNp()) nNp++;
        if (se.isNp()) nNp++;
        if (sw.isNp()) nNp++;
        double[] t = {P.Np, 0.0, 0.0};
        if (nNp >= 2) return t;
        if (nw.isNp()) return area(ne, se, sw);
        if (ne.isNp()) return area(se, sw, nw);
        if (se.isNp()) return area(sw, nw, ne);
        if (sw.isNp()) return area(nw, ne, se);
        double[] ts = area(nw, ne, se);
        double[] tj = area(nw, se, sw);
        t[0] = ts[0] + tj[0];
        t[1] = (ts[0] * ts[1] + tj[0] * tj[1]) / t[0];
        t[2] = (ts[0] * ts[2] + tj[0] * tj[2]) / t[0];
        return t;
    }
    public static void main(String[] args) {
        System.out.println("cluj west - est "+dist(46.75523088431756, 23.534692549301866, 46.78233271918741, 23.639663480324792));
        System.out.println("cluj west - est "+dist(new P(46.75523088431756, 23.534692549301866), new P(46.78233271918741, 23.639663480324792)));
        P cluj = new P(46.771546528572, 23.593744063483868);
        P turda = new P(46.564693576564935, 23.780168312696325);
        System.out.println("cluj - turda "+dist(cluj, turda));
        System.out.println("(0,3) - (0,4) "+dist(new P(0.0, 3.0), new P(0.0, 4.0)));
        System.out.println("(3,0) - (4,0) "+dist(new P(3.0, 0.0), new P(4.0, 0.0)));
        System.out.println("(0,0) - (0,1) "+dist(new P(0.0, 0.0), new P(0.0, 1.0)));
        System.out.println("(1,0) - (0,0) "+dist(new P(1.0, 0.0), new P(0.0, 0.0)));
        double[] t = area(new P(0.0, 0.0), new P(0.0, 3.0), new P(4.0, 0.0));
        System.out.println("area yg xg "+t[0]+" "+t[1]+" "+t[2]);
        t = area(new P(3.0, 0.0), new P(3.0, 4.0), new P(0.0, 4.0), new P(0.0, 0.0));
        System.out.println("area yg xg "+t[0]+" "+t[1]+" "+t[2]);
    }
}
