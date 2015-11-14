package managedBeans;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import comunication.Comunicacion;
import datatypes.DataBuyList;
import datatypes.DataElementBuyList;
import datatypes.DataStore;

@ManagedBean
@ViewScoped
public class BuyListDetailBB {
	
	private DataStore store;
	
	private DataBuyList buyListSelected; 
	
	private DataElementBuyList elemSelected;
	
	private int precio;
	
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
	
	public void checkElementBuyList() {
		try {
			Comunicacion.getInstance().getIBuyListController().checkElementBuyList(this.elemSelected.getId(), this.store.getId(), precio);
			this.buyListSelected = Comunicacion.getInstance().getIBuyListController().findBuyList(this.buyListSelected.getId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Bien de bien"));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
}
