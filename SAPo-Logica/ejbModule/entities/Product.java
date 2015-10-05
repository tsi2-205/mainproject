package entities;

import java.io.Serializable;
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
	
	@Version
    private int version;
	
    @ManyToMany(mappedBy = "products")
    private List<Store> stores;
    
    
    @ManyToMany(mappedBy = "products")
    private List<BuyList> buyLists;
    
    @ManyToMany(mappedBy = "products")
    private List<Category> categories;
    
    @OneToOne(mappedBy = "product")
    private Stock stock;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
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
    
}
