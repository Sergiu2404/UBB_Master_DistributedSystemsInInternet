package p.interfaces;
import javax.ejb.*;
import java.util.*;
public interface LocalDepartment extends EJBLocalObject {
  public Long getId();
  public String getName();
  public Collection getEmployees();
  public void addEmployee(LocalEmployee employee);
}
