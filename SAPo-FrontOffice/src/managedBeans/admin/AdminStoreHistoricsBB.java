package managedBeans.admin;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import managedBeans.SessionBB;

import comunication.Comunicacion;

import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProduct;
import datatypes.DataStore;

@ManagedBean
@ViewScoped
public class AdminStoreHistoricsBB {
	
	private DataStore store;
	
	private DataProduct product = null;
	
	private List<DataHistoricStock> histStock = new LinkedList<DataHistoricStock>();
	
	private List<DataHistoricStock> filteredHistStock = new LinkedList<DataHistoricStock>();
	
	private List<DataHistoricPrecioCompra> histPrecioCompra = new LinkedList<DataHistoricPrecioCompra>();
	
	private List<DataHistoricPrecioCompra> filteredHistPrecioCompra = new LinkedList<DataHistoricPrecioCompra>();
	
	private List<DataHistoricPrecioVenta> histPrecioVenta = new LinkedList<DataHistoricPrecioVenta>();
	
	private List<DataHistoricPrecioVenta> filteredHistPrecioVenta = new LinkedList<DataHistoricPrecioVenta>();
	
	public AdminStoreHistoricsBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.store = session.getStoreSelected();
		try {
			this.histStock = Comunicacion.getInstance().getIStoreController().findHistoricStock(store.getId());
			this.histPrecioCompra = Comunicacion.getInstance().getIStoreController().findHistoricPrecioCompra(store.getId());
			this.histPrecioVenta = Comunicacion.getInstance().getIStoreController().findHistoricPrecioVenta(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataProduct getProduct() {
		return product;
	}

	public void setProduct(DataProduct product) {
		this.product = product;
	}

	public List<DataHistoricStock> getHistStock() {
		return histStock;
	}

	public void setHistStock(List<DataHistoricStock> histStock) {
		this.histStock = histStock;
	}

	public List<DataHistoricPrecioCompra> getHistPrecioCompra() {
		return histPrecioCompra;
	}

	public void setHistPrecioCompra(List<DataHistoricPrecioCompra> histPrecioCompra) {
		this.histPrecioCompra = histPrecioCompra;
	}

	public List<DataHistoricPrecioVenta> getHistPrecioVenta() {
		return histPrecioVenta;
	}

	public void setHistPrecioVenta(List<DataHistoricPrecioVenta> histPrecioVenta) {
		this.histPrecioVenta = histPrecioVenta;
	}

	public List<DataHistoricStock> getFilteredHistStock() {
		return filteredHistStock;
	}

	public void setFilteredHistStock(List<DataHistoricStock> filteredHistStock) {
		this.filteredHistStock = filteredHistStock;
	}

	public List<DataHistoricPrecioCompra> getFilteredHistPrecioCompra() {
		return filteredHistPrecioCompra;
	}

	public void setFilteredHistPrecioCompra(
			List<DataHistoricPrecioCompra> filteredHistPrecioCompra) {
		this.filteredHistPrecioCompra = filteredHistPrecioCompra;
	}

	public List<DataHistoricPrecioVenta> getFilteredHistPrecioVenta() {
		return filteredHistPrecioVenta;
	}

	public void setFilteredHistPrecioVenta(
			List<DataHistoricPrecioVenta> filteredHistPrecioVenta) {
		this.filteredHistPrecioVenta = filteredHistPrecioVenta;
	}
	
}
