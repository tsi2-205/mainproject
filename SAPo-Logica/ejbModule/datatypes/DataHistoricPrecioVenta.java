package datatypes;

import java.io.Serializable;
import java.util.Calendar;

import entities.HistoricPrecioVenta;

public class DataHistoricPrecioVenta implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private Calendar fecha;

	private int precioVenta;
	
	private String nameProduct;

	// cambiar por enum - 0:Baja; 1:Sube;
	private int tipo;

	public DataHistoricPrecioVenta(int id, Calendar fecha, int precioVenta,
			int tipo, String nameProduct) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.precioVenta = precioVenta;
		this.tipo = tipo;
		this.nameProduct = nameProduct;
	}
	
	public DataHistoricPrecioVenta(HistoricPrecioVenta historic) {
		super();
		this.id = historic.getId();
		this.fecha = historic.getFecha();
		this.precioVenta = historic.getPrecioVenta();
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

	public int getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(int precioVenta) {
		this.precioVenta = precioVenta;
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
