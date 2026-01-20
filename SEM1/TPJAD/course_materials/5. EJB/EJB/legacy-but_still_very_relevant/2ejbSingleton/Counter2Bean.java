package p.beans;
import javax.ejb.*;
import p.interfaces.Counter2;
@Singleton
@Remote(Counter2.class)
public class Counter2Bean implements Counter2 {
    private int count = 0;
    public void inc() { this.count++; }
    public void dec() { this.count--; }
    public int getCount() { return this.count; }
}
