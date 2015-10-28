package entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "registered")
public class Registered extends User {
    
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="registered")
    private List<Comment> comments = new LinkedList<Comment>();
    
    @OneToMany(mappedBy="owner")
    private List<Store> storeOwner = new LinkedList<Store>();
	
    @ManyToMany(mappedBy = "guests")
    private List<Store> storesGuest = new LinkedList<Store>();
    
    @OneToMany
    @JoinColumn(name="idRegistered", referencedColumnName="id")
    private List<Message> messages = new LinkedList<Message>();
    
    @ManyToMany(mappedBy = "participants")
    private List<Chat> chats = new LinkedList<Chat>();
    
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new LinkedList<Notification>();
    
	public Registered() {
		super();
	}

	public Registered(String email, String password, String fbId, String name) {
		super(email, password, fbId, name);
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Store> getStoreOwner() {
		return storeOwner;
	}

	public void setStoreOwner(List<Store> storeOwner) {
		this.storeOwner = storeOwner;
	}

	public List<Store> getStoresGuest() {
		return storesGuest;
	}

	public void setStoresGuest(List<Store> storesGuest) {
		this.storesGuest = storesGuest;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}
    
}
