package entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "registered")
public class Registered extends User {
    
	private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy="owner")
    private List<Store> storeOwner = new LinkedList<Store>();
	
    @ManyToMany(mappedBy = "guests")
    private List<Store> storesGuest = new LinkedList<Store>();
    
	public Registered() {
		super();
	}

	public Registered(String email, String password, String fbId, String name, String acc) {
		super(email, password, fbId, name, acc);
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
}
