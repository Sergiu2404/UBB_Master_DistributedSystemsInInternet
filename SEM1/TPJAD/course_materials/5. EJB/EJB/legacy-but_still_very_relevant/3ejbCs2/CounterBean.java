package p.beans;
import javax.ejb.*;
@Stateful
public class CounterBean {
    private int count = 0;
    public void inc() { this.count++; }
    public void dec() { this.count--; }
    public int getCount() { return this.count; }
}
