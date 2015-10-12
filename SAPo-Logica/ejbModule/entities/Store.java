package entities;


import java.io.Serializable;
import java.util.LinkedList;
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
    
    @Version
    private int version;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable (name = "Strore_Product", joinColumns = @JoinColumn(name = "idStore"), inverseJoinColumns = @JoinColumn(name = "idProduct"))
    private List<Product> products = new LinkedList<Product>();
    
    @ManyToOne
    @JoinColumn(name="IdOwner")
    private Registered owner;
    
    @ManyToMany
    @JoinTable (name = "Strore_Registered", joinColumns = @JoinColumn(name = "idStore"), inverseJoinColumns = @JoinColumn(name = "idRegistered"))
    private List<Registered> guests = new LinkedList<Registered>();
    
    
    @OneToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;
    
    
    @OneToMany(mappedBy="store")
    private List<BuyList> buylists = new LinkedList<BuyList>();
    
    @OneToMany(mappedBy="store")
    private List<Comment> comments = new LinkedList<Comment>();
    
    @ManyToMany(mappedBy = "stores")
    private List<Category> categories = new LinkedList<Category>();
    
    @OneToOne(mappedBy = "store")
    private Stock stock;
    
    
   
    public Store() {
		super();
	}
	public Store(String name, String addr, String tel, String city, Registered user) {
		super();
		this.name = name;
		this.address = addr;
		this.telephone = tel;
		this.city = city;
		this.owner = user;
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
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public List<Registered> getGuests() {
		return guests;
	}
	
	public void setGuests(List<Registered> guests) {
		this.guests = guests;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public List<BuyList> getBuylists() {
		return buylists;
	}
	
	public void setBuylists(List<BuyList> buylists) {
		this.buylists = buylists;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public Stock getStock() {
		return stock;
	}
	
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public void setOwner(Registered owner) {
		this.owner = owner;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
}