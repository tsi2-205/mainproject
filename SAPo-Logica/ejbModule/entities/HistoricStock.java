package entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.*;

@Entity
@Table(name = "HistoricStock")
public class HistoricStock implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Version
	private int version;

	private Calendar fecha;

	private int stock;

	// cambiar por enum - 0: Baja; 1: Sube;
	private int tipo;

	@ManyToOne
	@JoinColumn(name = "idProducto", referencedColumnName = "id", nullable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "idStore", referencedColumnName = "id", nullable = false)
	private Store store;

	public HistoricStock() {
		super();
	}

	public HistoricStock(Calendar fecha, int stock, int tipo, Product product,
			Store store) {
		super();
		this.fecha = fecha;
		this.stock = stock;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}
