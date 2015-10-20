package datatypes;

import java.io.Serializable;

import entities.Stock;

public class DataStock implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;

    private int cantidad;
	
    private int precioVenta;
    
    private int precioCompra;
    
    private DataProduct product;
    
    private DataStore store;
    
    public DataStock() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public DataStock(int id, int cantidad, int precioVenta, int precioCompra) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.precioVenta = precioVenta;
		this.precioCompra = precioCompra;
	}

	public DataStock(Stock st) {
		super();
		this.id = st.getId();
		this.cantidad = st.getCantidad();
		this.precioVenta = st.getPrecioVenta();
		this.precioCompra = st.getPrecioCompra();
		this.product = new DataProduct(st.getProduct());
		this.store = new DataStore(st.getStore());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(int precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(int precioCompra) {
		this.precioCompra = precioCompra;
	}

	public DataProduct getProduct() {
		return product;
	}

	public void setProduct(DataProduct product) {
		this.product = product;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}
	
}
