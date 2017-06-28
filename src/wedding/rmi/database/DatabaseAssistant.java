package wedding.rmi.database;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import wedding.database.SQLAssistant;
import wedding.models.Person;
import wedding.models.Request;

public class DatabaseAssistant extends UnicastRemoteObject implements DatabaseAssistantI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SQLAssistant sqlAssistant;
	
	public DatabaseAssistant(int port) throws JSONException, ClassNotFoundException, IOException, SQLException {
		super(port);
		sqlAssistant = new SQLAssistant();
	}

	public List read(Request request) throws JSONException, SQLException{
		return sqlAssistant.selectPeople(request.getStorageName());
	}

	public void delete(Request request) throws SQLException{
		sqlAssistant.deletePerson(request.getStorageName(), (Person) request.getParams()[0]);
	}

	public void create(Request request) throws SQLException{
		sqlAssistant.insertPerson(request.getStorageName(), (Person) request.getParams()[0]);
	}
}
