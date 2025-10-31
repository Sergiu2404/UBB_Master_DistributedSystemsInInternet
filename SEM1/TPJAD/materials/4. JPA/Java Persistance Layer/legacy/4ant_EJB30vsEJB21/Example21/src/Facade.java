package p.interfaces;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
public interface Facade extends EJBObject {
  public void createDepartment(Long id, String name) throws RemoteException;
  public void assignEmployeeToDepartment(Long deptID, Long empID, String empName, int empAge, String empSex) throws RemoteException;
}
