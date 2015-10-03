package entities;


import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "Stores")
public class Store implements Serializable {
	
	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String STORE_ID;	

    private String STORE_NAME;
	
    private String STORE_ADDRESS;
	
    private String STORE_TELEPHONE;
	
    private String STORE_CITY;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codigo")
    private Usuario STORE_OWNER;
    
    // LISTA DE LOS USUARIOS A LOS CUALES SE LES COMPATIO EL ALMACEN (EL DIENIO NO ESTA ACA)
    @OneToMany
    private List<Usuario> STORE_USERS;
    
   
    public Store() {
		super();
	}
	public Store(String name, String addr, String telph, String city, Usuario user) {
		super();
		this.STORE_NAME = name;
		this.STORE_ADDRESS = addr;
		this.STORE_TELEPHONE = telph;
		this.STORE_CITY = city;
		this.STORE_OWNER = user;
		this.STORE_USERS = new java.util.LinkedList<Usuario>();
	}
	
	
	public String getStoreName() {
		return STORE_NAME;
	}

	public void setStoreName(String name) {
		STORE_NAME = name;
	}

	public String getStoreAddress() {
		return STORE_ADDRESS;
	}

	public void setStoreAddress(String addr) {
		STORE_ADDRESS = addr;
	}

	public String getStoreTelephone() {
		return STORE_TELEPHONE;
	}

	public void setStoreTelephone(String telph) {
		STORE_TELEPHONE = telph;
	}

	public String getStoreCity() {
		return STORE_CITY;
	}

	public void setStoreCity(String city) {
		STORE_CITY = city;
	}

	public Usuario getStoreOwner() {
		return STORE_OWNER;
	}

	public void setStoreOwner(Usuario owner) {
		STORE_OWNER = owner;
	}

	public String getStoreID() {
		return STORE_ID;
	}
	
	
	public List<Usuario> getStoreUsers() {
		return STORE_USERS;
	}
	
	public void setStoreUsers(List<Usuario> sTORE_USERS) {
		STORE_USERS = sTORE_USERS;
	}

	public void addUserToStore( Usuario u ) {
		this.STORE_USERS.add(u);
	}
	
	
	
}