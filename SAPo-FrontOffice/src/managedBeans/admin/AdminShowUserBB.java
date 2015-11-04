package managedBeans.admin;

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
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class AdminShowUserBB {
	
	private DataUser userSelected;
	private List<DataStore> storesOwner;
	private List<DataStore> storesGuest;
	private DataStore storeSelected;
	

	public AdminShowUserBB() {
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
		this.userSelected = session.getUserSelected();
		try {
			this.storesOwner = Comunicacion.getInstance().getIUserController().getStoresOwner(this.userSelected.getId());
			this.storesGuest= Comunicacion.getInstance().getIUserController().getStoresGuest(this.userSelected.getId());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String showStore() {
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

	public DataUser getUserSelected() {
		return userSelected;
	}

	public void setUserSelected(DataUser userSelected) {
		this.userSelected = userSelected;
	}

	public List<DataStore> getStoresOwner() {
		return storesOwner;
	}

	public void setStoresOwner(List<DataStore> storesOwner) {
		this.storesOwner = storesOwner;
	}

	public List<DataStore> getStoresGuest() {
		return storesGuest;
	}

	public void setStoresGuest(List<DataStore> storesGuest) {
		this.storesGuest = storesGuest;
	}

	public DataStore getStoreSelected() {
		return storeSelected;
	}

	public void setStoreSelected(DataStore storeSelected) {
		this.storeSelected = storeSelected;
	}
	
}
