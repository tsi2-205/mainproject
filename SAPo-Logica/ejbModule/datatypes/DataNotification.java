package datatypes;

import java.sql.Date;
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
	
	public String getTimeDesc() {
		
		Date date = new Date(this.date.getTime().getTime());
		Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
		
		long diff = currentDate.getTime() - date.getTime();
		
		String end = ".";
		
		long diffSeconds = diff / 1000;
		if (diffSeconds < 1L)
			return "En este momento.";
		if (diffSeconds >= 1L)
			end = "s.";
		if (diffSeconds < 60)
			return "Hace " + diffSeconds + " segundo" + end; 
		
		long diffMinutes = diff / (60 * 1000);
		if (diffMinutes > 1L)
			end = "s.";
		if (diffMinutes < 60)
			return "Hace " + diffMinutes + " minuto" + end;
		
		long diffHours = diff / (60 * 60 * 1000);
		if (diffHours > 1L)
			end = "s.";
		if (diffHours < 24)
			return "Hace " + diffHours + " hora" + end;
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if (diffDays > 1L)
			end = "s.";
		return "Hace " + diffDays + " dia" + end;
		
	}
}
