package wedding.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import wedding.FileAssintant;
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
	
	public ServerAssistance (int port) throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		super(port);
		sqlAssistant = new SQLAssistant();
		analyser = new Analyser();
		
		//TEMPORARY FUNCTION CALLS
		//loadRecordsToDatabase("brides.txt", "bride");
		//loadRecordsToDatabase("grooms.txt", "groom");
	}
	
	//TEMPORARY FUNCTION
	public void loadRecordsToDatabase(String fileName, String tableName) throws SQLException, FileNotFoundException {
		sqlAssistant.createTable(tableName);
		
		FileAssintant fileAssintant = new FileAssintant();
		ArrayList<Person> list = fileAssintant.getRecordsFromFile(fileName, tableName);
		
		for(Person person: list) {
			sqlAssistant.insertPerson(tableName, person);
		}
	}

	@Override
	public ArrayList<Couple> getCouples(ArrayList<Person> brides, ArrayList<Person> grooms) throws RemoteException {
		return analyser.analyseBridesAndGrooms(brides, grooms);
	}

	@Override
	public ArrayList doRequest(Request request) throws RemoteException {
		ArrayList list = null;
		try {
			switch(request.getType()) {
				case "post":
					sqlAssistant.insertPerson(request.getTableName(), request.getData());
					break;
				case "delete":
					sqlAssistant.deletePerson(request.getTableName(), request.getData());
					break;
				case "get":
					list = sqlAssistant.selectPeople(request.getTableName());
					break;
			}
		} catch(SQLException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
		}
		return list;
	}
	
	
}
