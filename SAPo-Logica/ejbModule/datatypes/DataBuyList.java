package datatypes;

import java.io.Serializable;
import java.util.List;

import entities.BuyList;
import entities.ElementBuyList;

public class DataBuyList implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;

    private String name;
	
    private String description;
    
    private List<DataElementBuyList> elements;

	public DataBuyList() {
		super();
	}

	public DataBuyList(int id, String name, String description,
			List<DataElementBuyList> elements) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.elements = elements;
	}
	
	public DataBuyList(BuyList buyList) {
		super();
		this.id = buyList.getId();
		this.name = buyList.getName();
		this.description = buyList.getDescription();
		for (ElementBuyList elem: buyList.getElements()) {
			this.elements.add(new DataElementBuyList(elem));
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

	public List<DataElementBuyList> getElements() {
		return elements;
	}

	public void setElements(List<DataElementBuyList> elements) {
		this.elements = elements;
	}

}
