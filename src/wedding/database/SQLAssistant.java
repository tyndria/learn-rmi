package wedding.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;

import org.json.JSONException;
import wedding.models.Person;

public class SQLAssistant implements Constants{
	
    private Connection connection;
    
    public SQLAssistant() {
        try {
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException e) {
            System.out.println("---" + e.getClass() + ": " +  e.getMessage());
        }
    }
    
    public void createTable(String tableName) throws SQLException {
    	String idTitle = "id_" + tableName;
    	String createTableSql = "CREATE TABLE " + tableName +
                "(" + idTitle + " INTEGER UNSIGNED NOT NULL AUTO_INCREMENT, " +
                " name VARCHAR(45), " + 
                " surname VARCHAR(45), " + 
                " birth_year VARCHAR(45), " + 
                " personal_description VARCHAR(255), " + 
                " demands VARCHAR(255), " + 
                " PRIMARY KEY ( " + idTitle + " ))"; 
    	PreparedStatement preparedStatement = connection.prepareStatement(createTableSql);
        preparedStatement.execute();
        preparedStatement.close();
    }
    
    public void insertPerson(String tableName, Person person) throws SQLException{
        String insertSql = "INSERT INTO " + tableName + " " +
                     "(NAME, SURNAME, BIRTH_YEAR, PERSONAL_DESCRIPTION, DEMANDS)VALUES" +
                     "(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, person.getName());
        preparedStatement.setString(2, person.getSurname());
        preparedStatement.setString(3, person.getBirthYear());
        preparedStatement.setString(4, convertArrayToJSON(person.getPropositions()));
        preparedStatement.setString(5, convertArrayToJSON(person.getDemands()));     
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deletePerson(String tableName, Person person) throws SQLException {
    	String deleteSql = "DELETE FROM " + tableName + " where id_" + tableName + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
        preparedStatement.setInt(1, person.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public ArrayList<Person> selectPeople(String tableName) throws SQLException, JSONException {
        String selectSql = "SELECT * FROM " + tableName;
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        ArrayList<Person> arrayList = new ArrayList<>();
        while(resultSet.next()){
        	Person person = new Person(resultSet.getString("birth_year"), 
        					resultSet.getString("name"), resultSet.getString("surname"), 
        					convertJSONToArray(resultSet.getString("personal_description")), 
        					convertJSONToArray(resultSet.getString("demands")),
        					resultSet.getInt("id_" + tableName));       
            arrayList.add(person);
        }
        return arrayList;
    }
    
    public String convertArrayToJSON(ArrayList<String> arrayList) {
    	JSONArray jsonArray = new JSONArray(arrayList);
    	return jsonArray.toString();
    }
    
    public ArrayList<String> convertJSONToArray(String json) throws JSONException{
    	JSONArray jsonArray = new JSONArray(json);
    	ArrayList<String> arrayList = new ArrayList<>();
    	for(int i = 0; i < jsonArray.length(); i ++) {
    		arrayList.add(jsonArray.getString(i));
    	}
    	return arrayList;
    }
}
