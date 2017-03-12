package wedding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import wedding.server.ServerAssistance;

public class MainServer implements NetworkConstants{

	public static void main(String[] args) {
		try {
			System.setProperty("java.rmi.server.hostname", HOST);
			ServerAssistance serverAssistance = new ServerAssistance(APP_PORT);
			Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
		    registry.rebind("rmi://" + HOST + ":" + APP_PORT + "/ServerAssistantI", serverAssistance);
		    System.out.println("Server started");
		} catch (RemoteException e) {
		    System.out.println(e.getClass() + ": " + e.getMessage());
		} catch (MalformedURLException e) {
			 System.out.println(e.getClass() + ": " + e.getMessage());
		} catch (FileNotFoundException e) {
			 System.out.println(e.getClass() + ": " + e.getMessage());
		} catch (ClassNotFoundException e) {
			 System.out.println(e.getClass() + ": " + e.getMessage());
		} catch (IOException e) {
			 System.out.println(e.getClass() + ": " + e.getMessage());
		} catch (SQLException e) {
			 System.out.println(e.getClass() + ": " + e.getMessage());
		}
	}
}
