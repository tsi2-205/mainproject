package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@Table(name = "userlogcount")
public class UserLogCount implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Version
	private int version;
	
	@Id
	private Date fecha;

	private int count;

	public UserLogCount() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public UserLogCount(Date fecha) {
		// TODO Auto-generated constructor stub
		super();
		this.fecha=fecha;
		this.count=1;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	

}
