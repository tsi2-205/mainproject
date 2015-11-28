package managedBeans;

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
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.NamingException;

import comunication.Comunicacion;
import datatypes.DataHistoricPrecioCompra;
import datatypes.DataHistoricPrecioVenta;
import datatypes.DataHistoricStock;
import datatypes.DataProduct;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreHistoryBB {
	
	private DataStore store;
	
	private DataProduct product = null;
	
	private DataUser user;
	
	private List<DataHistoricStock> histStock = new LinkedList<DataHistoricStock>();
	
	private List<DataHistoricStock> filteredHistStock = new LinkedList<DataHistoricStock>();
	
	private String textFilter = "";
	
	public StoreHistoryBB() {
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
		this.user = session.getLoggedUser();
		this.store = session.getStoreSelected();
		
		try {
			this.histStock = Comunicacion.getInstance().getIStoreController().findHistoricStock(store.getId());
			this.filteredHistStock = this.histStock;
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void filtrar() {
		this.filteredHistStock = new LinkedList<DataHistoricStock>();
		for (DataHistoricStock dhs: this.histStock) {
			String name = dhs.getNameProduct();
			String t = this.textFilter;
			if (name.toLowerCase().startsWith(t.toLowerCase())) {
				this.filteredHistStock.add(dhs);
			}
		}
	}
	
	public void filtered(AjaxBehaviorEvent event) {
		filtrar();
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

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	public List<DataHistoricStock> getHistStock() {
		return histStock;
	}

	public void setHistStock(List<DataHistoricStock> histStock) {
		this.histStock = histStock;
	}

	public List<DataHistoricStock> getFilteredHistStock() {
		return filteredHistStock;
	}

	public void setFilteredHistStock(List<DataHistoricStock> filteredHistStock) {
		this.filteredHistStock = filteredHistStock;
	}

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}
	
}
