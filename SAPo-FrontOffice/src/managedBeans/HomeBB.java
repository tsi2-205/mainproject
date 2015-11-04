package managedBeans;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.primefaces.event.SelectEvent;

import comunication.Comunicacion;
import datatypes.DataProduct;
import datatypes.DataStore;


@ManagedBean
@ViewScoped
public class HomeBB {
	private int id;
	private String name;
	private String idEmail;
	private String idFace;
	private List<DataStore> storesOwner;
	private List<DataStore> storesGuest;
	private DataStore storeSelected;
	

	@PostConstruct
	private void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.id=session.getLoggedUser().getId();
//		session.setStoreSelected(null);
		try{
			this.storesOwner = Comunicacion.getInstance().getIUserController().getStoresOwner(this.id);
			this.storesGuest= Comunicacion.getInstance().getIUserController().getStoresGuest(this.id);
				
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public HomeBB() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdEmail() {
		return idEmail;
	}

	public void setIdEmail(String id) {
		this.idEmail = id;
	}
	
	public String getIdFace() {
		return idFace;
	}

	public void setIdFace(String f) {
		this.idFace = f;
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

	public void showStore(SelectEvent event) {
		this.storeSelected = (DataStore) event.getObject();
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		session.setStoreSelected(this.storeSelected);		
		FacesContext faces = FacesContext.getCurrentInstance();
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");
	}
	
	public void createStore() throws NamingException {
//		FacesContext faces = FacesContext.getCurrentInstance();
//		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
//		configurableNavigationHandler.performNavigation("/pages/NewStore.xhtml?faces-redirect=true");
		if (Comunicacion.getInstance().getIUserController().createMoreStores(this.id)){
			ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
			configurableNavigationHandler.performNavigation("/pages/NewStore.xhtml?faces-redirect=true");
		}
		else{
			FacesMessage msg = new FacesMessage("Su cuenta debe ser premium");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		
	}
	
	
	
}