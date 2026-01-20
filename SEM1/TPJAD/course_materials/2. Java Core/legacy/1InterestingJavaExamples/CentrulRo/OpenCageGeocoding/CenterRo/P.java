public class P {
    static final double Np = Double.POSITIVE_INFINITY; // Not a point in area
    public double y = Np, x = Np;
    public P() { }
    public P(double y, double x) { this.y = y; this.x = x; }
    public P(String y, String x) {
        this.y = s2d(y);
        this.x = s2d(x);
    }
    public boolean isNp() {
        if (y == Np || x == Np) return true;
        return false;
    }
    public String toString() { return "("+y+","+x+")"; }
    public static boolean isNp(P p) {
        if (p.y == Np || p.x == Np) return true;
        return false;
    }
    public static boolean isNp(double a) {
        if (a == Np) return true;
        return false;
    }
    public static double s2d(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return Np; }
    }
}
