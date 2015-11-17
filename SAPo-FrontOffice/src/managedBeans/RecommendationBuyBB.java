package managedBeans;

import java.io.Serializable;
import java.util.LinkedList;
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

import comunication.Comunicacion;

import datatypes.DataBuyList;
import datatypes.DataElementBuyList;
import datatypes.DataStock;
import datatypes.DataStore;
import exceptions.ProductNotExistException;



@ManagedBean
@ViewScoped
public class RecommendationBuyBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private DataStore store;
	private List<DataStock> listRec= new LinkedList<DataStock>();
	private DataBuyList buyList;

	public RecommendationBuyBB() {
		super();
	}
	
	@PostConstruct
	private void init(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.store = session.getStoreSelected();
		this.buyList=session.getBuyListSelected();
		try {
		System.out.print("llego");
		this.listRec= Comunicacion.getInstance().getIBuyListController().buyRecommendation(this.store.getId(), this.buyList);
		System.out.print("llego2");
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

	public List<DataStock> getListRec() {
		return listRec;
	}

	public void setListRec(List<DataStock> listRec) {
		this.listRec = listRec;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void guardar(){
		List<DataStock> list=this.listRec;
		List<DataElementBuyList> elements = new LinkedList<DataElementBuyList>();
		for (DataStock ds: list) {
			if (ds.getCantidad()>0){
				DataElementBuyList element = new DataElementBuyList(this.store.getId(), ds.getCantidad(), ds.getProduct());
				elements.add(element);
			}
		}
		try {
			Comunicacion.getInstance().getIBuyListController().addBuyListStore(this.store.getId(), elements, this.buyList.getName(), this.buyList.getDescription(), this.buyList);
			FacesMessage msg = new FacesMessage("Los productos han sido agregados a la lista de compras");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
			configurableNavigationHandler.performNavigation("/pages/EditBuyList.xhtml?faces-redirect=true");
		} catch (ProductNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
