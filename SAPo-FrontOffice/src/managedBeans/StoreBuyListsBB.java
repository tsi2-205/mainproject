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
import javax.naming.NamingException;

import comunication.Comunicacion;
import datatypes.DataBuyList;
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

		try {
			this.buyLists = Comunicacion.getInstance().getIStoreController().findBuyListsStore(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}
	
	public String createBuyList() {
		return "/pages/NewBuyList.xhtml?faces-redirect=true";
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
