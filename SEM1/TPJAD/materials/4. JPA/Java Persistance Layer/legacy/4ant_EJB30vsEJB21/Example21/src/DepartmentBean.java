package p.entities;
import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import p.interfaces.*;
public abstract class DepartmentBean implements EntityBean {
  private EntityContext context;
  public abstract Long getId();
  public abstract void setId(Long id);
  public abstract String getName();
  public abstract void setName(String name);
  public abstract Collection getEmployees();
  public abstract void setEmployees(Collection employees);
  public void addEmployee(LocalEmployee employee) {
    try {
      Collection employees = getEmployees();
      employees.add(employee);
    } catch (Exception ex) {
      throw new EJBException(ex.getMessage());
    }
  }
  public Long ejbCreate(Long id, String name) throws CreateException {
    setId(id);
    setName(name);
    return null;
  }
  public void ejbPostCreate(Long id, String name) throws CreateException { }
  public void setEntityContext(EntityContext ctx) {
    context = ctx;
  }
  public void unsetEntityContext() {
    context = null;
  }
  public void ejbRemove() { }
  public void ejbLoad() { }
  public void ejbStore() { }
  public void ejbPassivate() { }
  public void ejbActivate() { }}
