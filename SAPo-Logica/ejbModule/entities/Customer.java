package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCustomer;
	
	private byte[] css;
	
	@Version
    private int version;
	

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Customer(byte[] css) {
		Customer c= new Customer();
		c.css=css;
	}

	public int getId() {
		return idCustomer;
	}

	public void setId(int id) {
		this.idCustomer = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public byte[] getCss() {
		return this.css;
	}

	public void setCss(byte[] c) {
		this.css = c;
	}
	

}
