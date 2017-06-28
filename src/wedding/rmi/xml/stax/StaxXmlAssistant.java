package wedding.rmi.xml.stax;

import wedding.models.Person;
import wedding.models.Request;
import wedding.xml.StaxParser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Antonina on 6/27/2017.
 */
public class StaxXmlAssistant extends UnicastRemoteObject implements StaxXmlAssistantI {
    StaxParser staxParser = new StaxParser();

    public StaxXmlAssistant() throws RemoteException {}

    public List read(Request request){
        return staxParser.read(request.getStorageName() + "s.xml");
    }

    public void delete(Request request){
        staxParser.delete(request.getStorageName() + "s.xml", ((Person) request.getParams()[0]).getId() + "");
    }

    public void create(Request request){
        staxParser.create(request.getStorageName() + "s.xml", (Person) request.getParams()[0]);
    }
}
