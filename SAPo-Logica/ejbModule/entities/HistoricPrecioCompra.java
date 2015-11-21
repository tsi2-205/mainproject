package entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "historicpreciocompra")
public class HistoricPrecioCompra implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Version
    private int version;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fecha;
	
	private int precioCompra;
	
	//cambiar por enum - 0:Baja; 1:Sube;
	private int tipo;
	
	@ManyToOne
    @JoinColumn(name = "idProducto", referencedColumnName = "id", nullable = false)
    private SpecificProduct product;
	
	@ManyToOne
    @JoinColumn(name = "idStore", referencedColumnName = "id", nullable = false)
    private Store store;
	
	public HistoricPrecioCompra() {
		super();
	}
	
	public HistoricPrecioCompra(Calendar fecha, int precioCompra,
			int tipo, SpecificProduct product, Store store) {
		super();
		this.fecha = fecha;
		this.precioCompra = precioCompra;
		this.tipo = tipo;
		this.product = product;
		this.store = store;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public SpecificProduct getProduct() {
		return product;
	}

	public void setProduct(SpecificProduct product) {
		this.product = product;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}
