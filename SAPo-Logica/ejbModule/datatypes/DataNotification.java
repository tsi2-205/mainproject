package datatypes;

import java.util.Calendar;

public class DataNotification {

	private int id;
	
	private String message;
	
	private boolean isRead;
	
	private DataUser user;

	private DataStore store;
	
	private Calendar date;

	public DataNotification() {
		super();
	}
	
	public DataNotification(String message, boolean isRead, DataUser user, DataStore store, Calendar date) {
		super();
		this.message = message;
		this.isRead = isRead;
		this.user = user;
		this.store = store;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
