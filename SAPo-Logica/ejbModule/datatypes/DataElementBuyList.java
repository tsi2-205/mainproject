package datatypes;

import java.io.Serializable;

import entities.ElementBuyList;


public class DataElementBuyList implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private int quantity;
	
	private int price;
    
    private boolean checked;
	
    private DataProduct product;
    
	public DataElementBuyList() {
		super();
	}

	public DataElementBuyList(int id, int quantity, DataProduct product) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.product = product;
		this.checked = false;
		this.price = 0;
	}
	
	public DataElementBuyList(int quantity, DataProduct product) {
		super();
		this.quantity = quantity;
		this.product = product;
		this.checked = false;
		this.price = 0;
	}
	
	public DataElementBuyList(ElementBuyList element) {
		super();
		this.id = element.getId();
		this.quantity = element.getQuantity();
		this.product = new DataProduct(element.getProduct());
		this.checked = element.isChecked();
		this.price = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public DataProduct getProduct() {
		return product;
	}

	public void setProduct(DataProduct product) {
		this.product = product;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
