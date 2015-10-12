package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Category")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Category implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    private String name;
	
    private String description;
    
    @Version
    private int version;
    
    @ManyToMany
    @JoinTable (name = "Category_Product", joinColumns = @JoinColumn(name = "idCategory"), inverseJoinColumns = @JoinColumn(name = "idProduct"))
    private List<Product> products = new LinkedList<Product>();
    
    @ManyToMany
    @JoinTable (name = "Category_Store", joinColumns = @JoinColumn(name = "idCategory"), inverseJoinColumns = @JoinColumn(name = "idStore"))
    private List<Store> stores = new LinkedList<Store>();
    
//  @ManyToOne(mappedBy="sons")
//  private Category father;
//  
//  @OneToMany
//  @JoinTable (name = "Categories_Tree", joinColumns = @JoinColumn(name = "idCategory_Father"), inverseJoinColumns = @JoinColumn(name = "idCategory_Son"), uniqueConstraints=true)
//  private List<Category> sons;


	public Category() {
		super();
	}

	public Category(String name, String description, int version) {
		super();
		this.name = name;
		this.description = description;
		this.version = version;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
	

}
