package wedding.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class MainServer {

	public static void main(String[] args) {
		try {
		   ServerAssistance serverAssistance = new ServerAssistance();
		   System.setProperty("java.rmi.server.hostname","158.129.226.122");
		   Naming.rebind("rmi://158.129.226.122:5000/ServerAssistantI", serverAssistance);
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
