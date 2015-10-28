package datatypes;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import entities.Category;
import entities.GenericCategory;

public class DataCategory implements Serializable, Comparable<DataCategory> {
	
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String description;
	
	private boolean isGeneric;
	
	private List<DataCategory> sonsCategories = new LinkedList<DataCategory>();
	
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
		for (Category cat: categ.getSonsCategories()) {
			this.sonsCategories.add(new DataCategory(cat));
		}
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

	public List<DataCategory> getSonsCategories() {
		return sonsCategories;
	}

	public void setSonsCategories(List<DataCategory> sonsCategories) {
		this.sonsCategories = sonsCategories;
	}
	
	@Override
    public String toString() {
        return this.name;
    }

	@Override
	public int compareTo(DataCategory o) {
		return this.getName().compareTo(o.getName());
	}

	
	
}
