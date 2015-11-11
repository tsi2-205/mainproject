package datatypes;

import java.io.Serializable;
import java.util.Calendar;

import entities.HistoricStock;

public class DataHistoricStock implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private Calendar fecha;
	
	private int cantChange;
	
	private int precio;

	private int stock;
	
	private String nameProduct;
	
	//cambiar por enum - 0: Baja; 1: Sube;
	private int tipo;
	

	public DataHistoricStock(int id, Calendar fecha, int stock, int tipo, String nameProduct) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.stock = stock;
		this.tipo = tipo;
		this.nameProduct = nameProduct;
	}
	
	public DataHistoricStock(HistoricStock historic) {
		super();
		this.id = historic.getId();
		this.fecha = historic.getFecha();
		this.stock = historic.getStock();
		this.tipo = historic.getTipo();
		this.nameProduct = historic.getProduct().getName();
		this.cantChange = historic.getCantChange();
		this.precio = historic.getPrecio();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

	public int getCantChange() {
		return cantChange;
	}

	public void setCantChange(int cantChange) {
		this.cantChange = cantChange;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
}
