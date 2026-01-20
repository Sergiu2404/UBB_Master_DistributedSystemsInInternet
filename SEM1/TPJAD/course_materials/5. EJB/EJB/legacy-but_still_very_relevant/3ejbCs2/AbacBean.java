package p.beans;
import javax.ejb.*;
@Stateless
public class AbacBean {
    public int add(int a, int b) { return a + b; }
    public int sub(int a, int b) { return a - b; }
}
