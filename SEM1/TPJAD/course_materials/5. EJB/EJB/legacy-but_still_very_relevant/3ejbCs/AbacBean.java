package p.beans;
import javax.ejb.*;
import p.interfaces.Abac;
import p.interfaces.AbacR;
@Stateless
@Local(Abac.class)
@Remote(AbacR.class)
public class AbacBean implements Abac, AbacR {
    public int add(int a, int b) { return a + b; }
    public int sub(int a, int b) { return a - b; }
}
