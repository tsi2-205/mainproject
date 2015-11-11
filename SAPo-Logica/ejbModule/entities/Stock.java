package entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "stock")
public class Stock implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    private int cantidad;
    
    private Integer cantidadMin;
    
    private Integer cantidadMax;
	
    private int precioVenta;
    
    private int precioCompra;
	
	@Version
    private int version;
	
	@ManyToOne
    @JoinColumn(name = "idStore", referencedColumnName = "id", nullable = false)
    private Store store;
    
    @OneToOne
    @JoinColumn(name = "idProduct", referencedColumnName = "id", nullable = false)
    private SpecificProduct product;
    
    
	public Stock() {
		super();
	}

	@Deprecated
	public Stock(int cantidad, int precioVenta, int precioCompra, 
			Store store, SpecificProduct product) {
		super();
		this.cantidad = cantidad;
		this.precioVenta = precioVenta;
		this.precioCompra = precioCompra;
		this.store = store;
		this.product = product;
	}
	
	

	public Stock(int cantidad, Integer cantidadMin, Integer cantidadMax,
			Store store, SpecificProduct product) {
		super();
		this.cantidad = cantidad;
		this.cantidadMin = cantidadMin;
		this.cantidadMax = cantidadMax;
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

	public SpecificProduct getProduct() {
		return product;
	}

	public void setProduct(SpecificProduct product) {
		this.product = product;
	}

	public Integer getCantidadMin() {
		return cantidadMin;
	}

	public void setCantidadMin(Integer cantidadMin) {
		this.cantidadMin = cantidadMin;
	}

	public Integer getCantidadMax() {
		return cantidadMax;
	}

	public void setCantidadMax(Integer cantidadMax) {
		this.cantidadMax = cantidadMax;
	}
    
}
