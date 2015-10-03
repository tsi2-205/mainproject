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
	private int identifier;	

    private String name;
	
    private String address;
	
    private String telephone;
	
    private String city;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codigo")
    private Usuario owner;
    
    // LISTA DE LOS USUARIOS A LOS CUALES SE LES COMPATIO EL ALMACEN (EL DIENIO NO ESTA ACA)
    @OneToMany
    private List<Usuario> users;
    
   
    public Store() {
		super();
	}
	public Store(String name, String addr, String tel, String city, Usuario user) {
		super();
		this.name = name;
		this.address = addr;
		this.telephone = tel;
		this.city = city;
		this.owner = user;
		this.users = new java.util.LinkedList<Usuario>();
	}
	
	public int getIdentifier() {
		return identifier;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public Usuario getOwner() {
		return owner;
	}
	
	public void setOwner(Usuario owner) {
		this.owner = owner;
	}
	
	public List<Usuario> getUsers() {
		return users;
	}
	
	public void setUsers(List<Usuario> users) {
		this.users = users;
	}
	
}