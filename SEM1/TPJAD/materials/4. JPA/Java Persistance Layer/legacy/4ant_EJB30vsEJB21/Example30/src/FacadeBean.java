package p.sessions;
import javax.ejb.*;
import javax.persistence.*;
import p.interfaces.*;
import p.entities.*;
@Stateless
public class FacadeBean implements FacadeRemote {
  @PersistenceContext(unitName="contextE30") private EntityManager manager;
  public void createDepartment(String deptName) {
    Department department = new Department();
    department.setName(deptName);
    manager.persist(department);
  }
  public void assignEmployeeToDepartment(Long id, String name, int age, String sex) {
    Department department = searchDepartment(id);
    department.addEmployee(name, age, sex);
    manager.persist(department);
  }
  public Department searchDepartment(Long id) {
    return manager.find(Department.class, id);
  }
}
