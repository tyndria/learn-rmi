package wedding.rmi.xml.stax;

import wedding.models.Request;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Antonina on 6/27/2017.
 */
public interface StaxXmlAssistantI extends Remote {
    void create(Request request) throws RemoteException;
    List read(Request request) throws RemoteException;
    void delete(Request request) throws RemoteException;
}
