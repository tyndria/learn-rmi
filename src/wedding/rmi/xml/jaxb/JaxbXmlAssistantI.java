package wedding.rmi.xml.jaxb;

import wedding.models.Request;

import javax.xml.bind.JAXBException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Antonina on 6/28/2017.
 */
public interface JaxbXmlAssistantI extends Remote {
    void create(Request request) throws RemoteException, JAXBException;
    List read(Request request) throws RemoteException, JAXBException;
    void delete(Request request) throws RemoteException, JAXBException;
}
