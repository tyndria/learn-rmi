package wedding.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Couple implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Person bride;
	private Person groom;

	public Couple() {
		this.setBride(new Person());
		this.setGroom(new Person());
	}

	public Couple(Person bride, Person groom) {
		this.setBride(bride);
		this.setGroom(groom);
	}

	public Person getBride() {
		return bride;
	}

	public void setBride(Person bride) {
		this.bride = bride;
	}

	public Person getGroom() {
		return groom;
	}

	public void setGroom(Person groom) {
		this.groom = groom;
	}

	@Override
	public String toString() {
		return this.bride.getFullName() + " + " + this.groom.getFullName();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.defaultWriteObject();
		out.writeObject(this.groom);
		out.writeObject(this.bride);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.groom = (Person) in.readObject();
		this.bride = (Person) in.readObject();
	}

	public boolean equals(Couple couple) {
		return couple.groom.equals(this.groom) && couple.bride.equals(this.bride);
	}

}
