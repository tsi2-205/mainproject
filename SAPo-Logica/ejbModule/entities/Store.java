package entities;


import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Stores")
public class Store implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;	

    private String name;
	
    private String address;
	
    private String telephone;
	
    private String city;

    
    @ManyToMany
    @JoinTable (name = "Strore_Product", joinColumns = @JoinColumn(name = "idStore"), inverseJoinColumns = @JoinColumn(name = "idProduct"))
    private List<Product> products;
    
    @ManyToOne
    @JoinColumn(name="IdOwner")
    private Registered owner;
    
    @ManyToMany
    @JoinTable (name = "Strore_Registered", joinColumns = @JoinColumn(name = "idStore"), inverseJoinColumns = @JoinColumn(name = "idRegistered"))
    private List<Registered> guests;
    
    
    @OneToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;
    
    
    @OneToMany(mappedBy="store")
    private List<BuyList> buylists;
    
    @OneToMany(mappedBy="store")
    private List<Comment> comments;
    
    @ManyToMany(mappedBy = "stores")
    private List<Category> categories;
    
    @OneToOne(mappedBy = "store")
    private Stock stock;
    
    
   
    public Store() {
		super();
	}
	public Store(String name, String addr, String tel, String city, User user) {
		super();
		this.name = name;
		this.address = addr;
		this.telephone = tel;
		this.city = city;
	}
	
	public int getId() {
		return id;
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
	
	public User getOwner() {
		return owner;
	}
	
	
}