package entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Version
    private int version;
	
	private String message;
	
	private boolean isRead;
	
	private Calendar date;
	
	@ManyToOne
	@JoinColumn(name = "IdUser")
	private Registered user;

	@ManyToOne
	@JoinColumn(name = "IdStore")
	private Store store;

	public Notification() {
		super();
	}
	
	public Notification(String message, boolean isRead, Registered user, Store store, Calendar date) {
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public Registered getUser() {
		return user;
	}

	public void setUser(Registered user) {
		this.user = user;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}
