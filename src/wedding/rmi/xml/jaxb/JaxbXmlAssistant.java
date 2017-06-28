package wedding.rmi.xml.jaxb;

import wedding.models.Person;
import wedding.models.Request;
import wedding.xml.JaxbParser;

import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Antonina on 6/28/2017.
 */
public class JaxbXmlAssistant extends UnicastRemoteObject implements JaxbXmlAssistantI {
    JaxbParser jaxbParser = new JaxbParser();

    public JaxbXmlAssistant() throws RemoteException {}

    public List read(Request request) throws JAXBException {
        return jaxbParser.read(request.getStorageName() + "s.xml");
    }

    public void delete(Request request) throws JAXBException{
        jaxbParser.delete(request.getStorageName() + "s.xml", ((Person) request.getParams()[0]).getId());
    }

    public void create(Request request) throws JAXBException{
        jaxbParser.create(request.getStorageName() + "s.xml", (Person) request.getParams()[0]);
    }
}
