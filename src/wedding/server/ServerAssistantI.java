package wedding.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import wedding.models.Couple;
import wedding.models.Person;
import wedding.models.Request;

public interface ServerAssistantI extends Remote {

	public ArrayList<Couple> getCouples(ArrayList<Person> brides, ArrayList<Person> grooms) throws RemoteException;
	public ArrayList doRequest(Request request) throws RemoteException;
}
