package wedding.rmi.database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import wedding.models.Couple;
import wedding.models.Person;
import wedding.models.Request;

public interface DatabaseAssistantI extends Remote {
	void create(Request request) throws RemoteException, SQLException;
	List read(Request request) throws RemoteException, JSONException, SQLException;
	void delete(Request request) throws RemoteException, SQLException;
}
