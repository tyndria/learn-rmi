package wedding.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable {

	private static final long serialVersionUID = 12358903454875L;

	String birthYear;
	String name;
	String surname;
	ArrayList<String> personalDescription;
	ArrayList<String> demands;
	int id;

	public Person(String birthYear, String name, String surname, ArrayList<String> personalDescription,
			ArrayList<String> demands, int id) {
		this.birthYear = birthYear;
		this.name = name;
		this.surname = surname;
		this.personalDescription = personalDescription;
		this.demands = demands;
		this.id = id;
	}

	public Person() {
		this.birthYear = "";
		this.name = "";
		this.surname = "";
		this.personalDescription = new ArrayList<>();
		this.demands = new ArrayList<>();
		this.id = -1;
	}

	private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.defaultWriteObject();
		out.writeObject(birthYear);
		out.writeObject(name);
		out.writeObject(surname);
		out.writeInt(this.personalDescription.size());
		for (String string : this.personalDescription) {
			out.writeObject(string);
		}
		out.writeInt(this.demands.size());
		for (String string : this.demands) {
			out.writeObject(string);
		}
		out.writeInt(id);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.birthYear = (String) in.readObject();
		this.name = (String) in.readObject();
		this.surname = (String) in.readObject();
		int count = in.readInt();
		this.personalDescription = new ArrayList<>();
		while (count > 0) {
			this.personalDescription.add((String) in.readObject());
			count--;
		}
		count = in.readInt();
		this.demands = new ArrayList<>();
		while (count > 0) {
			this.demands.add((String) in.readObject());
			count--;
		}
		this.id = in.readInt();
	}

	public String getBirthYear() {
		return birthYear;
	}

	public String getFullName() {
		return name + " " + surname;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public int getId() {
		return id;
	}

	public ArrayList<String> getPersonalDescription() {
		return personalDescription;
	}

	public ArrayList<String> getDemands() {
		return demands;
	}

	@Override
	public String toString() {
		return "name: " + this.name + "; surname: " + this.surname + "; birthYear: " + this.birthYear + "; demands: "
				+ this.demands.toString() + "; supplies: " + this.personalDescription.toString();
	}

	public boolean equals(Person p) {
		return this.birthYear.equals(p.birthYear) && this.name.equals(p.name) && this.surname.equals(p.surname)
				&& this.demands.equals(p.demands) && this.personalDescription.equals(p.personalDescription)
				&& this.id == p.id;
	}
}
