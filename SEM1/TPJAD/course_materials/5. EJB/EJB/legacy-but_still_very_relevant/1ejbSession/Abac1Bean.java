package p.beans;
import javax.ejb.*;
import p.interfaces.Abac1;
@Stateless
@Remote(Abac1.class)
public class Abac1Bean implements Abac1 {
    public int add(int a, int b) { return a + b; }
    public int sub(int a, int b) { return a - b; }
}
