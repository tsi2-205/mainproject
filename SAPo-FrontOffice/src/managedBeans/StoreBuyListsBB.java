package managedBeans;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.primefaces.event.SelectEvent;

import comunication.Comunicacion;
import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreBuyListsBB {

	private DataStore store;

	private DataUser user;

	private List<DataBuyList> buyLists = new LinkedList<DataBuyList>();
	
	private DataBuyList buyListSelected = null;

	public StoreBuyListsBB() {
		super();
	}

	@PostConstruct
	private void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext();
		Application apli = context.getApplication();
		ExpressionFactory ef = apli.getExpressionFactory();
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}", SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.user = session.getLoggedUser();
		this.store = session.getStoreSelected();
//		categorySelected;
//		buyListSelected;
//		productSelected;
//		userSelected;
//		stockSelected;
		session.setBuyListSelected(null);

		try {
			this.buyLists = Comunicacion.getInstance().getIStoreController().findBuyListsStore(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}
	
	public String createBuyList() {
		return "/pages/NewBuyList.xhtml?faces-redirect=true";
	}
	
	public void onSelectBuyList() {
		if(this.buyListSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		session.setBuyListSelected(this.buyListSelected);		
    		FacesContext faces = FacesContext.getCurrentInstance();
    		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    		configurableNavigationHandler.performNavigation("/pages/BuyListDetail.xhtml?faces-redirect=true");
        }
        
    }
	
	public void deleteBuyList() {
		if(this.buyListSelected != null) {
			try {
				Comunicacion.getInstance().getIStoreController().deleteBuyListsStore(this.buyListSelected.getId(), store.getId());
				this.buyLists = Comunicacion.getInstance().getIStoreController().findBuyListsStore(store.getId());
			} catch (NamingException e) {
				e.printStackTrace();
			}
        }
        
    }
	
	public void seeBuyList() {
		if(this.buyListSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		session.setBuyListSelected(this.buyListSelected);		
    		FacesContext faces = FacesContext.getCurrentInstance();
    		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    		configurableNavigationHandler.performNavigation("/pages/BuyListDetail.xhtml?faces-redirect=true");
        }
        
    }

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	public List<DataBuyList> getBuyLists() {
		return buyLists;
	}

	public void setBuyLists(List<DataBuyList> buyLists) {
		this.buyLists = buyLists;
	}

	public DataBuyList getBuyListSelected() {
		return buyListSelected;
	}

	public void setBuyListSelected(DataBuyList buyListSelected) {
		this.buyListSelected = buyListSelected;
	}
	
}
