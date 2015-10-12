package datatypes;

import java.io.Serializable;

import entities.Category;
import entities.GenericCategory;

public class DataCategory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String description;
	
	private boolean isGeneric;
	
	public DataCategory() {
		super();
	}

	public DataCategory(int id, String name, String description, boolean isGeneric) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isGeneric = isGeneric;
	}
	
	public DataCategory(Category categ) {
		super();
		this.id = categ.getId();
		this.name = categ.getName();
		this.description = categ.getDescription();
		this.isGeneric = (categ instanceof GenericCategory) ? true : false;
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
	
}
