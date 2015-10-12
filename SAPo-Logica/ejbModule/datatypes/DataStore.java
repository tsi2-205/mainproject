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
    
    public DataStore() {
		super();
	}
    
    public DataStore(int id, String name, String address, String telephone, String city) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.telephone = telephone;
		this.city = city;
    }
    
    public DataStore(Store stor) {
		super();
		this.id = stor.getId();
		this.name = stor.getName();
		this.address = stor.getAddress();
		this.telephone = stor.getTelephone();
		this.city = stor.getCity();
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
	

}
