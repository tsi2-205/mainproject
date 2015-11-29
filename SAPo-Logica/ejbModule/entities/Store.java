package entities;


import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "stores")
public class Store implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;	

    private String name;
	
    private String address;
    
    @Temporal(TemporalType.DATE)
    private Calendar dateToCreate;
	
    private String telephone;
	
    private String city;
    
    @Version
    private int version;

//    @ManyToMany(fetch = FetchType.LAZY, mappedBy="stores")
//    private List<GenericProduct> genericsProducts = new LinkedList<GenericProduct>();
    
    @OneToMany(mappedBy="store", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<SpecificProduct> specificsProducts = new LinkedList<SpecificProduct>();
    
    @ManyToOne
    @JoinColumn(name="IdOwner")
    private Registered owner;
    
    @ManyToMany
    @JoinTable (name = "strore_registered", joinColumns = @JoinColumn(name = "idStore"), inverseJoinColumns = @JoinColumn(name = "idRegistered"))
    private List<Registered> guests = new LinkedList<Registered>();
    
    @OneToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;
    
    @OneToMany(mappedBy="store")
    private List<BuyList> buylists = new LinkedList<BuyList>();
    
    @ManyToMany(mappedBy = "stores")
    private List<Category> categories = new LinkedList<Category>();
    
    @OneToMany(mappedBy = "store")
    private List<Stock> stocks = new LinkedList<Stock>();
    
    @OneToMany(mappedBy = "store")
    private List<HistoricPrecioCompra> historicsPrecios = new LinkedList<HistoricPrecioCompra>();
    
    @OneToMany(mappedBy = "store")
    private List<HistoricStock> historicStock = new LinkedList<HistoricStock>();
    
    @OneToMany(mappedBy = "store")
    private List<Notification> notifications = new LinkedList<Notification>();
   
    public Store() {
		super();
	}
	public Store(String name, String addr, String tel, String city, Calendar dateToCreate, Registered user) {
		super();
		this.name = name;
		this.address = addr;
		this.telephone = tel;
		this.city = city;
		this.dateToCreate = dateToCreate;
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
	
	public Registered getOwner() {
		return owner;
	}
	
//	public List<GenericProduct> getGenericsProducts() {
//		return genericsProducts;
//	}
//	
//	public void setGenericsProducts(List<GenericProduct> genericsProducts) {
//		this.genericsProducts = genericsProducts;
//	}
	
	public List<SpecificProduct> getSpecificsProducts() {
		return specificsProducts;
	}
	
	public void setSpecificsProducts(List<SpecificProduct> specificsProducts) {
		this.specificsProducts = specificsProducts;
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
	
	public List<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public List<Stock> getStocks() {
		return stocks;
	}
	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}
	public List<HistoricPrecioCompra> getHistoricsPrecios() {
		return historicsPrecios;
	}
	public void setHistoricsPrecios(List<HistoricPrecioCompra> historicsPrecios) {
		this.historicsPrecios = historicsPrecios;
	}
	public List<HistoricStock> getHistoricStock() {
		return historicStock;
	}
	public void setHistoricStock(List<HistoricStock> historicStock) {
		this.historicStock = historicStock;
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
	public Calendar getDateToCreate() {
		return dateToCreate;
	}
	public void setDateToCreate(Calendar dateToCreate) {
		this.dateToCreate = dateToCreate;
	}
	
}