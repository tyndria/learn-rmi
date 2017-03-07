package wedding.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String tableName;
	public String type;
	public Person person;
	
	public Request(String type, String tableName, Person person) {
		this.type = type;
		this.tableName = tableName;
		this.person = person;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.defaultWriteObject();
		out.writeObject(this.tableName);
		out.writeObject(this.type);
		out.writeObject(this.person);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.tableName = (String) in.readObject();
		this.type = (String) in.readObject();
		this.person = (Person) in.readObject();
	}
	
	public String toString() {
		return "table: " +  this.tableName + "; type: " + this.type + "; person: " + this.person;
	}
}
