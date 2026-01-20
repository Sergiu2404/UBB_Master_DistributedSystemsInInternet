package p.interfaces;
import javax.ejb.*;
public interface LocalDepartmentHome extends EJBLocalHome {
  public LocalDepartment create(Long id, String name) throws CreateException;
  public LocalDepartment findByPrimaryKey(Long id) throws FinderException;
}
