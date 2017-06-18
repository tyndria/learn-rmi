package wedding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import org.json.JSONException;
import wedding.server.ServerAssistance;

public class MainServer implements NetworkConstants{

	public static void main(String[] args) {
		try {
			System.setProperty("java.rmi.server.hostname", HOST);
			ServerAssistance serverAssistance = new ServerAssistance(APP_PORT);
			Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
		    registry.rebind("rmi://" + HOST + ":" + APP_PORT + "/ServerAssistantI", serverAssistance);
		    System.out.println("Server started");
		} catch (ClassNotFoundException | IOException | SQLException | JSONException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
		}
	}
}
