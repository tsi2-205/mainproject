package datatypes;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import entities.GenericProduct;
import entities.Product;
import entities.ProductAdditionalAttribute;

public class DataProduct implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
    private String description;
    
    private boolean isGeneric;
    
    private String image;
    
    private List<DataProductAdditionalAttribute> additionalAttributes = new LinkedList<DataProductAdditionalAttribute>();
    
	public DataProduct() {
		super();
	}
	
	
	public DataProduct(int id, String name, String description, boolean isGeneric, List<DataProductAdditionalAttribute> additionalAttributes ) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isGeneric = isGeneric;
		this.additionalAttributes = additionalAttributes;
	}
	
	public DataProduct(int id, String name, String description, boolean isGeneric, List<DataProductAdditionalAttribute> additionalAttributes , String img) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isGeneric = isGeneric;
		this.additionalAttributes = additionalAttributes;
		this.image = img;
	}

	public DataProduct(Product prod) {
		super();
		this.id = prod.getId();
		this.name = prod.getName();
		this.description = prod.getDescription();
		this.isGeneric = (prod instanceof GenericProduct) ? true : false;
		for (ProductAdditionalAttribute ad: prod.getAdditionalAttributes()) {
			this.additionalAttributes.add(new DataProductAdditionalAttribute(ad));
		}
	}

	public String getImage(){
		return this.image;
	}
	
	public void setImage( String img){
		this.image = img;
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

	public List<DataProductAdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(
			List<DataProductAdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}
	
	@Override
    public String toString() {
        return this.name;
    }
	
}
