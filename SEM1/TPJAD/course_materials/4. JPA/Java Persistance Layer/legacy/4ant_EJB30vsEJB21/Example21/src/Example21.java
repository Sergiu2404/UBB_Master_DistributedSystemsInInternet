package p.clients;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import p.interfaces.*;
public class Example21 {
  public static void main(String[] args) {
    try {
      Context initial = new InitialContext();
      Object objref = initial.lookup("ejb/Facade");
      FacadeHome home = (FacadeHome) PortableRemoteObject.narrow(objref,FacadeHome.class);
      Facade lFacade = home.create();
      lFacade.createDepartment(new Long(1),"Finance");
      lFacade.createDepartment(new Long(2),"Administration");
      lFacade.createDepartment(new Long(3),"Sales");
      lFacade.assignEmployeeToDepartment(new Long(1),new Long(1),"Ionescu",31,"M");
      lFacade.assignEmployeeToDepartment(new Long(1),new Long(2),"Popescu",32,"F");
      lFacade.assignEmployeeToDepartment(new Long(2),new Long(3),"Vasile",33,"M");
    } catch (Exception ex) {
      System.err.println("Caught an exception:");
      ex.printStackTrace();
    }
  }
}
