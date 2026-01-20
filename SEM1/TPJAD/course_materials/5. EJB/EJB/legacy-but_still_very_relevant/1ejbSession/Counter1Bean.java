package p.beans;
import javax.ejb.*;
import p.interfaces.Counter1;
@Stateful
@Remote(Counter1.class)
public class Counter1Bean implements Counter1 {
    private int count = 0;
    public void inc() { this.count++; }
    public void dec() { this.count--; }
    public int getCount() { return this.count; }
}
