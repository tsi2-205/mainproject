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
import javax.faces.event.AjaxBehaviorEvent;

import managedBeans.SessionBB;

import comunication.Comunicacion;

import datatypes.DataStore;

@ManagedBean
@ViewScoped
public class AdminStoresBB {
	
	private List<DataStore> stores = new LinkedList<DataStore>();
	private List<DataStore> storesFiltered = new LinkedList<DataStore>();
	private String textFilter = "";
	private DataStore storeSelected;
	
	public AdminStoresBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		
		try {
			this.stores = Comunicacion.getInstance().getIStoreController().getStores();
			this.storesFiltered = this.stores;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void filtered(AjaxBehaviorEvent event) {
		this.storesFiltered = new LinkedList<DataStore>();
		for (DataStore ds: this.stores) {
			String name = ds.getName();
			String t = this.textFilter;
			if (name.toLowerCase().startsWith(t.toLowerCase())) {
				this.storesFiltered.add(ds);
			}
		}
	}
	
	public String seeStore() {
		String ret = "/pages/AdminStoreDetail.xhtml?faces-redirect=true";
    	if(this.storeSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		session.setStoreSelected(this.storeSelected);
        } else {
        	ret = "";
        }
    	
		return ret;
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

	public DataStore getStoreSelected() {
		return storeSelected;
	}

	public void setStoreSelected(DataStore storeSelected) {
		this.storeSelected = storeSelected;
	}
	
}
