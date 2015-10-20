package datatypes;

import java.io.Serializable;
import java.util.Calendar;

import entities.HistoricPrecioCompra;

public class DataHistoricPrecioCompra implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private Calendar fecha;

	private int precioCompra;
	
	private String nameProduct;

	// cambiar por enum - 0:Baja; 1:Sube;
	private int tipo;
	
	
	public DataHistoricPrecioCompra(int id, Calendar fecha, int precioCompra,
			int tipo, String nameProduct) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.precioCompra = precioCompra;
		this.tipo = tipo;
		this.nameProduct = nameProduct;
	}
	
	public DataHistoricPrecioCompra(HistoricPrecioCompra historic) {
		super();
		this.id = historic.getId();
		this.fecha = historic.getFecha();
		this.precioCompra = historic.getPrecioCompra();
		this.tipo = historic.getTipo();
		this.nameProduct = historic.getProduct().getName();
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

	public int getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(int precioCompra) {
		this.precioCompra = precioCompra;
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
	
}
