package managedBeans;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import datatypes.DataBuyList;
import datatypes.DataElementBuyList;
import datatypes.DataStore;

@ManagedBean
@ViewScoped
public class BuyListDetailBB {
	
	private DataStore store;
	
	private DataBuyList buyListSelected; 
	
	private DataElementBuyList elemSelected;
	
	public BuyListDetailBB() {
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
		this.buyListSelected = session.getBuyListSelected();
		
		
	}
	
	public String editBuyList() {
		return "/pages/EditBuyList.xhtml?faces-redirect=true";
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataBuyList getBuyListSelected() {
		return buyListSelected;
	}

	public void setBuyListSelected(DataBuyList buyListSelected) {
		this.buyListSelected = buyListSelected;
	}

	public DataElementBuyList getElemSelected() {
		return elemSelected;
	}

	public void setElemSelected(DataElementBuyList elemSelected) {
		this.elemSelected = elemSelected;
	}
	
}
