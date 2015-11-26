package datatypes;

import java.io.Serializable;
import java.util.Date;

public class DataUserLogged  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date fecha;
	private int count;
	
	public DataUserLogged() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public DataUserLogged(Date f, int c) {
		// TODO Auto-generated constructor stub
		super();
		this.count=c;
		this.fecha=f;
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
