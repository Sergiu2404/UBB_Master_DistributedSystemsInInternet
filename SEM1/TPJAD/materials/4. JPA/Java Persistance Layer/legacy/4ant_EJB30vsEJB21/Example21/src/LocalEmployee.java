package p.interfaces;
import javax.ejb.*;
public interface LocalEmployee extends EJBLocalObject {
  public Long getId();
  public String getName();
  public int getAge();
  public String getSex();
  public LocalDepartment getDepartment();
}
