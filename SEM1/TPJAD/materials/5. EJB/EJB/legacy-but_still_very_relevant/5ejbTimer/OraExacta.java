package p;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class OraExacta {
    @Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "*/5", year = "*")
    public void backgroundProcessing() { System.out.println("\n\n\t Este ora: " + new Date()); }
}
