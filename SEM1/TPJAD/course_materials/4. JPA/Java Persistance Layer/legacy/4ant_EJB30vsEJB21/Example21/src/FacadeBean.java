package p.sessions;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import p.interfaces.*;
public class FacadeBean implements SessionBean {
  private LocalDepartmentHome departmentHome = null;
  private LocalEmployeeHome employeeHome = null;
  public FacadeBean() {
  }
  public void ejbCreate() throws CreateException {
    try {
      departmentHome = lookupDepartment();
      employeeHome = lookupEmployee();
    } catch (NamingException ex) {
      throw new CreateException(ex.getMessage());
    }
  }
  public void ejbActivate() {
    try {
      departmentHome = lookupDepartment();
      employeeHome = lookupEmployee();
    } catch (NamingException ex) {
      throw new EJBException(ex.getMessage());
    }
  }
  public void ejbPassivate() {
    departmentHome = null;
    employeeHome = null;
  }
  public void ejbRemove() { }
  public void setSessionContext(SessionContext sc) { }
  public void createDepartment(Long id, String name) {
    try {
      LocalDepartment department = departmentHome.create(id,name);
    } catch (Exception ex) {
      throw new EJBException(ex.getMessage());
    }
  }
  public void assignEmployeeToDepartment(Long deptID, Long empID, String empName, int empAge, String empSex) {
    try {
      LocalDepartment department = departmentHome.findByPrimaryKey(deptID);
      LocalEmployee employee = employeeHome.create(empID, empName, empAge, empSex);
      department.addEmployee(employee);
    } catch (Exception ex) {
      throw new EJBException(ex.getMessage());
    }
  }
  private LocalDepartmentHome lookupDepartment() throws NamingException {
    Context initial = new InitialContext();
    Object objref = initial.lookup("java:comp/env/ejb/Department");
    return (LocalDepartmentHome) objref;
  }
  
  private LocalEmployeeHome lookupEmployee() throws NamingException  {
    Context initial = new InitialContext();
    Object objref = initial.lookup("java:comp/env/ejb/Employee");
    return (LocalEmployeeHome) objref;
  }
}
