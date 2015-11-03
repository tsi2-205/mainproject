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

import comunication.Comunicacion;
import managedBeans.SessionBB;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class AdminStoreDetailBB {
	
	private List<DataStore> stores = new LinkedList<DataStore>();
	private List<DataStore> storesFiltered = new LinkedList<DataStore>();
	private String textFilter = "";
	private DataStore store;
	private DataUser ownerStore;
	private List<DataUser> guestsStore = new LinkedList<DataUser>();
	private int valorizacion;
	
	public AdminStoreDetailBB() {
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
			this.ownerStore = Comunicacion.getInstance().getIStoreController().getOwnerStore(this.store.getId());
			this.guestsStore = Comunicacion.getInstance().getIStoreController().getGuestsStore(this.store.getId());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String showProducts() {
		return "/pages/AdminStoreProducts.xhtml?faces-redirect=true";
	}
	
	public String showHistorics() {
		return "/pages/AdminStoreHistorics.xhtml?faces-redirect=true";
	}

	public List<DataStore> getStores() {
		return stores;
	}

	public void setStores(List<DataStore> stores) {
		this.stores = stores;
	}

	public List<DataStore> getStoresFiltered() {
		return storesFiltered;
	}

	public void setStoresFiltered(List<DataStore> storesFiltered) {
		this.storesFiltered = storesFiltered;
	}

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataUser getOwnerStore() {
		return ownerStore;
	}

	public void setOwnerStore(DataUser ownerStore) {
		this.ownerStore = ownerStore;
	}

	public List<DataUser> getGuestsStore() {
		return guestsStore;
	}

	public void setGuestsStore(List<DataUser> guestsStore) {
		this.guestsStore = guestsStore;
	}

	public int getValorizacion() {
		return valorizacion;
	}

	public void setValorizacion(int valorizacion) {
		this.valorizacion = valorizacion;
	}
	
}
