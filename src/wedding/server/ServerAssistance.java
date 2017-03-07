package wedding.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import wedding.database.SQLAssistant;
import wedding.models.Couple;
import wedding.models.Person;
import wedding.models.Request;

public class ServerAssistance extends UnicastRemoteObject implements ServerAssistantI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SQLAssistant sqlAssistant;
	Analyser analyser;
	
	public ServerAssistance () throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		sqlAssistant = new SQLAssistant();
		analyser = new Analyser();
	}

	@Override
	public ArrayList<Couple> getCouples(ArrayList<Person> brides, ArrayList<Person> grooms) throws RemoteException {
		return analyser.analyseBridesAndGrooms(brides, grooms);
	}

	@Override
	public ArrayList doRequest(Request request) throws RemoteException {
		ArrayList list = null;
		try {
			switch(request.type) {
				case "post":
					sqlAssistant.insertPerson(request.tableName, request.person);
					break;
				case "delete":
					sqlAssistant.deletePerson(request.tableName, request.person.id);
					break;
				case "get":
					list = sqlAssistant.selectPeople(request.tableName);
					break;
			}
		} catch(SQLException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
		}
		return list;
	}
	
	
}
