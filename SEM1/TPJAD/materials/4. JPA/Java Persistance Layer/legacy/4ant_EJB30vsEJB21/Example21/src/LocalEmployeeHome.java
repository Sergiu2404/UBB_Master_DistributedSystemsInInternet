package p.interfaces;
import javax.ejb.*;
public interface LocalEmployeeHome extends EJBLocalHome {
  public LocalEmployee create(Long id, String name, int age, String sex) throws CreateException;
  public LocalEmployee findByPrimaryKey(Long id) throws FinderException;
}
