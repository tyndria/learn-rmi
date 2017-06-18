package wedding.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import wedding.FileAssistant;
import wedding.database.SQLAssistant;
import wedding.models.Couple;
import wedding.models.Person;
import wedding.models.Request;
import wedding.xml.StaxXmlAssistant;

public class ServerAssistance extends UnicastRemoteObject implements ServerAssistantI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SQLAssistant sqlAssistant;
	private StaxXmlAssistant staxXmlAssistant;
	private Analyser analyser;
	
	public ServerAssistance (int port) throws JSONException, ClassNotFoundException, IOException, SQLException {
		super(port);
		sqlAssistant = new SQLAssistant();
		analyser = new Analyser();
		staxXmlAssistant = new StaxXmlAssistant();
	}

	@Override
	public ArrayList<Couple> getBestCouples(ArrayList<Person> brides, ArrayList<Person> grooms) 
			throws RemoteException{
		ArrayList<Couple> bestCouples = null;
		try {
			bestCouples = analyser.analyseBridesAndGrooms(brides, grooms);
		} catch (NullPointerException e) {
			System.out.println("No couples");
		}
		return bestCouples;
	}

	@Override
	public ArrayList doRequest(Request request) throws RemoteException, JSONException {
		final String DATABASE = "database";
		final String XML = "xml";
		ArrayList list = null;
		try {
			switch(request.getRequestType()) {
				case "create":
					if (request.getTechnologyType().equals(DATABASE)) {
						sqlAssistant.insertPerson(request.getStorageName(), (Person) request.getParams()[0]);
					} else if(request.getTechnologyType().equals(XML)) {
						staxXmlAssistant.create(request.getStorageName() + "s.xml", (Person) request.getParams()[0]);
					}
					break;
				case "delete":
					if (request.getTechnologyType().equals(DATABASE)) {
						sqlAssistant.deletePerson(request.getStorageName(), (Person) request.getParams()[0]);
					} else if(request.getTechnologyType().equals(XML)) {
						staxXmlAssistant.delete(request.getStorageName() + "s.xml", ((Person) request.getParams()[0]).getId() + "");
					}
					break;
				case "read":
					if (request.getTechnologyType().equals(DATABASE)) {
						list = sqlAssistant.selectPeople(request.getStorageName());
					} else if (request.getTechnologyType().equals(XML)){
						list = staxXmlAssistant.read(request.getStorageName() + "s.xml");
					}
					break;
			}
		} catch(SQLException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
		}
		return list;
	}
	
	
}
