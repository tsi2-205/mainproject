package datatypes;

import java.io.Serializable;

import entities.GenericProduct;
import entities.Product;

public class DataProduct implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
    private String description;
    
    private boolean isGeneric;
    
    private DataStock stock;
    
	public DataProduct() {
		super();
	}
	
	public DataProduct(int id, String name, String description, boolean isGeneric) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isGeneric = isGeneric;
	}

	public DataProduct(Product prod) {
		super();
		this.id = prod.getId();
		this.name = prod.getName();
		this.description = prod.getDescription();
		this.isGeneric = (prod instanceof GenericProduct) ? true : false;
		this.stock = new DataStock(prod.getStock());
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

	public boolean isGeneric() {
		return isGeneric;
	}

	public void setGeneric(boolean isGeneric) {
		this.isGeneric = isGeneric;
	}

	public DataStock getStock() {
		return stock;
	}

	public void setStock(DataStock stock) {
		this.stock = stock;
	}
	
}
