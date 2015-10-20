package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "ProductAdditionalAttribute")
public class ProductAdditionalAttribute implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Version
    private int version;
	
	private String nameAttribute;
	
	private String valueAttribute;

	public ProductAdditionalAttribute() {
		super();
	}

	public ProductAdditionalAttribute(String nameAttribute,
			String valueAttribute) {
		super();
		this.nameAttribute = nameAttribute;
		this.valueAttribute = valueAttribute;
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
