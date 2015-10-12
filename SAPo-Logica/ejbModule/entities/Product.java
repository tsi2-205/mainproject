package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Product")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
    private String description;
	
	@Version
    private int version;
	
    @ManyToMany(mappedBy = "products")
    private List<Store> stores = new LinkedList<Store>();
    
    
    @ManyToMany(mappedBy = "products")
    private List<BuyList> buyLists = new LinkedList<BuyList>();
    
    @ManyToMany(mappedBy = "products")
    private List<Category> categories = new LinkedList<Category>();
    
    @OneToOne(mappedBy = "product")
    private Stock stock;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Product(String name, String description) {
		super();
		this.name = name;
		this.description = description;
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

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	public List<BuyList> getBuyLists() {
		return buyLists;
	}

	public void setBuyLists(List<BuyList> buyLists) {
		this.buyLists = buyLists;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
