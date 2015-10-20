package datatypes;

import java.io.Serializable;

import entities.ProductAdditionalAttribute;

public class DataProductAdditionalAttribute implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;

	private int version;

	private String nameAttribute;

	private String valueAttribute;
	
	public DataProductAdditionalAttribute(String nameAttribute, String valueAttribute) {
		super();
		this.nameAttribute = nameAttribute;
		this.valueAttribute = valueAttribute;
	}
	
	public DataProductAdditionalAttribute(ProductAdditionalAttribute attribute) {
		super();
		this.id = attribute.getId();
		this.version = attribute.getVersion();
		this.nameAttribute = attribute.getNameAttribute();
		this.valueAttribute = attribute.getValueAttribute();
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

	public String getNameAttribute() {
		return nameAttribute;
	}

	public void setNameAttribute(String nameAttribute) {
		this.nameAttribute = nameAttribute;
	}

	public String getValueAttribute() {
		return valueAttribute;
	}

	public void setValueAttribute(String valueAttribute) {
		this.valueAttribute = valueAttribute;
	}
	
}
