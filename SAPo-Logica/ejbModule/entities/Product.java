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
    private List<Category> categories = new LinkedList<Category>();
    
    @OneToMany(mappedBy = "product")
    private List<Stock> stocks = new LinkedList<Stock>();
    
    @OneToMany(mappedBy = "product")
    private List<HistoricPrecioCompra> historicsPrecios = new LinkedList<HistoricPrecioCompra>();
    
    @OneToMany(mappedBy = "product")
    private List<HistoricStock> historicStock = new LinkedList<HistoricStock>();
    
    @OneToMany
    @JoinColumn (name = "idProduct", referencedColumnName="id")
    private List<ProductAdditionalAttribute> additionalAttributes = new LinkedList<ProductAdditionalAttribute>();

    
	public Product() {
		super();
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

	public List<ProductAdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(
			List<ProductAdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}
	
}
