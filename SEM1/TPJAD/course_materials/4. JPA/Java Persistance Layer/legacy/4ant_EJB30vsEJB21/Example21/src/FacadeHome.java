package p.interfaces;
import java.io.*;
import java.rmi.*;
import javax.ejb.*;
public interface FacadeHome extends EJBHome {
  Facade create() throws RemoteException, CreateException;
}
