package entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Registered")
public class Registered extends User {
    
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="registered")
    private List<Comment> comments;
    
    @OneToMany(mappedBy="owner")
    private List<Store> storeOwner;
	
    @ManyToMany(mappedBy = "guests")
    private List<Store> storesGuest;
    
    @OneToMany
    @JoinColumn(name="idRegistered", referencedColumnName="id")
    private List<Message> messages;
    
    @ManyToMany(mappedBy = "participants")
    private List<Chat> chats;
    
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
