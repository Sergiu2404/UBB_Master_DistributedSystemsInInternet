package p.interfaces;
import javax.ejb.*;
import p.entities.*;
@Remote
public interface FacadeRemote {
  public void createDepartment(String deptName);
  public Department searchDepartment(Long id);
  public void assignEmployeeToDepartment(Long id, String name, int age, String sex);
}
