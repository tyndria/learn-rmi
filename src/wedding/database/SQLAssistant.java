package wedding.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;

import wedding.models.Person;

public class SQLAssistant implements Constants{
	
    private Connection connection;
    
    public SQLAssistant() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("---" + e.getClass() + ": " +  e.getMessage());
        } catch (SQLException e) {
            System.out.println("---" + e.getClass() + ": " +  e.getMessage());
        }
    }
    
    public void insertPerson(String tableName, Person person) throws SQLException{
        String insertSql = "INSERT INTO " + tableName + " " +
                     "(NAME, SURNAME, BIRTH_YEAR, PERSONAL_DESCRIPTION, DEMANDS)VALUES" +
                     "(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setString(1, person.name);
        preparedStatement.setString(2, person.surname);
        preparedStatement.setString(3, person.birthYear);
        preparedStatement.setString(4, convertArrayToJSON(person.personalDescription));
        preparedStatement.setString(5, convertArrayToJSON(person.demands));     
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public long completeUpdate(String cityNameToFind, String cityNameToReplace) throws SQLException{
        String updateSql = "UPDATE Citymap SET city_name = ? WHERE city_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
        preparedStatement.setString(1, cityNameToReplace);
        preparedStatement.setString(2, cityNameToFind);
        long beforeInsert = System.currentTimeMillis();
        preparedStatement.executeUpdate();
        long afterInsert = System.currentTimeMillis();
        return afterInsert - beforeInsert;
    }

    public void deletePerson(String tableName, int id) throws SQLException {
    	String deleteSql = "DELETE FROM " + tableName + " where id_" + tableName + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public ArrayList<Person> selectPeople(String tableName) throws SQLException {
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
    
    public ArrayList<String> convertJSONToArray(String json) {
    	JSONArray jsonArray = new JSONArray(json);
    	ArrayList<String> arrayList = new ArrayList<>();
    	for(int i = 0; i < jsonArray.length(); i ++) {
    		arrayList.add(jsonArray.getString(i));
    	}
    	return arrayList;
    }
}