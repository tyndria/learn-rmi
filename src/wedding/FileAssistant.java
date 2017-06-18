package wedding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wedding.database.SQLAssistant;
import wedding.models.Person;

public class FileAssistant {

	SQLAssistant sqlAssistant;
	PrintWriter printWriter;
	Scanner scanner;
	ArrayList<Person> brides;
	ArrayList<Person> grooms;
	
	
	public FileAssistant() {
		sqlAssistant = new SQLAssistant();
		
//		try {
//			//brides = sqlAssistant.selectPeople("bride");
//			//grooms = sqlAssistant.selectPeople("groom");
//		} catch (SQLException e) {
//			System.out.println(e.getClass() + ": " + e.getMessage());
//		}
	}
	
	public void saveRecords() {
		try {
			writeRecordsToFile("brides.txt", brides);
			writeRecordsToFile("grooms.txt", grooms);			
		} catch (FileNotFoundException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
		}
	}
	
	public void writeRecordsToFile(String fileName, ArrayList list) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(fileName);
		JSONArray jsonArray = new JSONArray(list);
		printWriter.println(jsonArray.toString());
		printWriter.close();
	}

	public ArrayList getRecordsFromFile(String fileName, String tableName) throws FileNotFoundException, JSONException {
		StringBuilder stringBuilder = new StringBuilder();
		scanner = new Scanner(new File(fileName));
		while(scanner.hasNext()) {
			stringBuilder.append(scanner.next());
		}
		scanner.close();
		String json = stringBuilder.toString();
		JSONArray jsonArray = new JSONArray(json);
    	ArrayList<Person> arrayList = new ArrayList<>();
    	for(int i = 0; i < jsonArray.length(); i ++) {
    		JSONObject jsonObject = (JSONObject)jsonArray.get(i);
    		JSONArray personalDescription = (JSONArray)jsonObject.get("propositions");
    		JSONArray demands = (JSONArray)jsonObject.get("demands");
    		arrayList.add(new Person(
    				jsonObject.getString("birthYear"), 
    				jsonObject.getString("name"),
    				jsonObject.getString("surname"),
    				sqlAssistant.convertJSONToArray(personalDescription.toString()),
    				sqlAssistant.convertJSONToArray(demands.toString()),
    				-1
    			)
    		);
    	}
    	return arrayList;
	}
}
