package wedding.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Request<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String storageName;
	private String requestType;
	private String technologyType;
	private T[] params;
	
	public Request(String requestType, String technologyType, String storageName, T... params) {
		this.requestType = requestType;
		this.technologyType = technologyType;
		this.storageName = storageName;
		this.params = params;
	}
	
	public String getStorageName() {
		return this.storageName;
	}
	
	public T[] getParams() {
		return this.params;
	}
	
	public String getRequestType() {
		return this.requestType;
	}
	
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public void setParams(T[] params) {
		this.params = params;
	}

	public void setTechnologyType(String technologyType) {
		this.technologyType = technologyType;
	}

	public String getTechnologyType() {
		return technologyType;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.defaultWriteObject();
		out.writeObject(this.storageName);
		out.writeObject(this.requestType);
		out.writeObject(this.params);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.storageName = (String) in.readObject();
		this.requestType = (String) in.readObject();
		this.params = (T[]) in.readObject();
	}
	
	public String toString() {
		return "table: " +  this.storageName + "; requestType: " + this.requestType + "; params: " + this.params;
	}
}
