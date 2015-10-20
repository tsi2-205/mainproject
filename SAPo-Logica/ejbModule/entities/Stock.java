package entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "Stock")
public class Stock implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    private int cantidad;
	
    private int precioVenta;
    
    private int precioCompra;
	
	@Version
    private int version;
	
	@ManyToOne
    @JoinColumn(name = "idStore", referencedColumnName = "id", nullable = false)
    private Store store;
    
    @ManyToOne
    @JoinColumn(name = "idProduct", referencedColumnName = "id", nullable = false)
    private Product product;
    
    
	public Stock() {
		super();
	}

	public Stock(int cantidad, int precioVenta, int precioCompra,
			Store store, Product product) {
		super();
		this.cantidad = cantidad;
		this.precioVenta = precioVenta;
		this.precioCompra = precioCompra;
		this.store = store;
		this.product = product;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
    
}
