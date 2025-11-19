package p.beans;
import javax.ejb.*;
import p.interfaces.Abac2;
@Singleton
@Remote(Abac2.class)
public class Abac2Bean implements Abac2 {
    public int add(int a, int b) { return a + b; }
    public int sub(int a, int b) { return a - b; }
}
