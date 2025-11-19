package p.beans;
import javax.ejb.*;
import p.interfaces.Counter;
import p.interfaces.CounterR;
@Stateful
@Local(Counter.class)
@Remote(CounterR.class)
public class CounterBean implements Counter, CounterR {
    private int count = 0;
    public void inc() { this.count++; }
    public void dec() { this.count--; }
    public int getCount() { return this.count; }
}
