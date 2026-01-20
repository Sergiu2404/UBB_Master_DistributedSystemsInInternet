package p.clients;
import javax.naming.*;
import p.interfaces.*;
public class Example30 {
  public static void main(String[] args) {
    try {
      InitialContext ctx = new InitialContext();
      FacadeRemote lFacadeRemote = (FacadeRemote) ctx.lookup("FacadeBean/remote");
      lFacadeRemote.createDepartment("Finance");
      lFacadeRemote.createDepartment("Administration");
      lFacadeRemote.createDepartment("Sales");
      lFacadeRemote.assignEmployeeToDepartment(new Long(1),"Ionescu",31,"M");
      lFacadeRemote.assignEmployeeToDepartment(new Long(1),"Popescu",32,"F");
      lFacadeRemote.assignEmployeeToDepartment(new Long(2),"Vasile",33,"M");
    } catch(Exception ex) {
      System.out.println(ex);
    }
  }
}
