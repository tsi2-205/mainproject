package entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "specificproduct")
public class SpecificProduct extends Product {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
    @JoinColumn(name = "idStore", referencedColumnName = "id", nullable = false)
    private Store store;
	
	@OneToMany(mappedBy = "product")
    private List<Stock> stocks = new LinkedList<Stock>();
    
    @OneToMany(mappedBy = "product")
    private List<HistoricPrecioCompra> historicsPreciosCompra = new LinkedList<HistoricPrecioCompra>();
    
    @OneToMany(mappedBy = "product")
    private List<HistoricPrecioVenta> historicsPreciosVenta = new LinkedList<HistoricPrecioVenta>();
    
    @OneToMany(mappedBy = "product")
    private List<HistoricStock> historicStock = new LinkedList<HistoricStock>();

	public SpecificProduct() {
		super();
	}

	public SpecificProduct(String name, String description, Store store) {
		super(name, description);
		this.store = store;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public List<HistoricPrecioCompra> getHistoricsPreciosCompra() {
		return historicsPreciosCompra;
	}

	public void setHistoricsPreciosCompra(
			List<HistoricPrecioCompra> historicsPreciosCompra) {
		this.historicsPreciosCompra = historicsPreciosCompra;
	}

	public List<HistoricPrecioVenta> getHistoricsPreciosVenta() {
		return historicsPreciosVenta;
	}

	public void setHistoricsPreciosVenta(
			List<HistoricPrecioVenta> historicsPreciosVenta) {
		this.historicsPreciosVenta = historicsPreciosVenta;
	}

	public List<HistoricStock> getHistoricStock() {
		return historicStock;
	}

	public void setHistoricStock(List<HistoricStock> historicStock) {
		this.historicStock = historicStock;
	}
	
	
}
