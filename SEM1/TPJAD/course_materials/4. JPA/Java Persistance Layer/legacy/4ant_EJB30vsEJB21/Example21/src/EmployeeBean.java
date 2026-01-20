package p.entities;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import p.interfaces.*;
public abstract class EmployeeBean implements EntityBean {
  private EntityContext context;
  public abstract Long getId();
  public abstract void setId(Long id);
  public abstract String getName();
  public abstract void setName(String name);
  public abstract int getAge();
  public abstract void setAge(int age);
  public abstract String getSex();
  public abstract void setSex(String sex);
  public abstract LocalDepartment getDepartment();
  public abstract void setDepartment(LocalDepartment department);
  public Long ejbCreate(Long id, String name, int age, String sex) throws CreateException {
    setId(id);
    setName(name);
    setAge(age);
    setSex(sex);
    return null;
  }
  public void ejbPostCreate(Long id, String name, int age, String sex) throws CreateException { }
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
  public void ejbActivate() { }
}
