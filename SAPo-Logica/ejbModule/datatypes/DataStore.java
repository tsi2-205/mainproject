package datatypes;

import java.io.Serializable;

import entities.Store;

public class DataStore implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;	

    private String name;
	
    private String address;
	
    private String telephone;
	
    private String city;
    
    private DataUser owner;
    
    public DataStore() {
		super();
	}
    
    public DataStore(int id, String name, String address, String telephone, String city, DataUser owner) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.city = city;
		this.owner = owner;
    }
    
    public DataStore(Store store) {
		super();
		this.id = store.getId();
		this.name = store.getName();
		this.address = store.getAddress();
		this.telephone = store.getTelephone();
		this.city = store.getCity();
		this.owner = new DataUser(store.getOwner());
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public DataUser getOwner() {
		return owner;
	}

	public void setOwner(DataUser owner) {
		this.owner = owner;
	}
}
