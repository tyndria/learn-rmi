package wedding;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import wedding.rmi.database.DatabaseAssistant;
import wedding.rmi.xml.jaxb.JaxbXmlAssistant;
import wedding.rmi.xml.stax.StaxXmlAssistant;

public class MainServer implements NetworkConstants{

	public static void main(String[] args) {
		try {
			System.setProperty("java.rmi.server.hostname", HOST);

			DatabaseAssistant databaseAssistant = new DatabaseAssistant(APP_PORT);
			StaxXmlAssistant xmlAssistant = new StaxXmlAssistant();
			Analyzer analyzer = new Analyzer();
			JaxbXmlAssistant jaxbXmlAssistant = new JaxbXmlAssistant();

			Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
			String serverAddress = "rmi://" + HOST + ":" + APP_PORT;

		    registry.rebind(serverAddress + "/DatabaseAssistantI", databaseAssistant);
			registry.rebind(serverAddress + "/StaxXmlAssistantI", xmlAssistant);
			registry.rebind(serverAddress + "/AnalyzerI", analyzer);
			registry.rebind(serverAddress + "/JaxbXmlAssistantI", jaxbXmlAssistant);

		    System.out.println("Server started");
		} catch (ClassNotFoundException | IOException | SQLException | JSONException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
		}
	}
}
