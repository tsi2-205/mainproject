package managedBeans;

import java.io.Serializable;
import java.util.List;

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

import org.primefaces.event.RowEditEvent;

import comunication.Comunicacion;

import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataElementBuyList;
import datatypes.DataProduct;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreDetailBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DataStore store;
	private DataUser user;
	private int categorySelected = -1;
	private int productSelected = -1;
	private List<DataStock> stocks = null;
	private List<DataCategory> categories = null;
//	private List<DataProduct> gemericProducts = null;
	private List<DataCategory> gemericCategories = null;
	private List<DataBuyList> buyLists = null;
	
	public StoreDetailBB() {
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
		try{
			this.stocks = Comunicacion.getInstance().getIStoreController().findStockProductsStore(store.getId());
			this.categories= Comunicacion.getInstance().getIStoreController().findSpecificCategoriesStore(store.getId());
//			this.gemericProducts = Comunicacion.getInstance().getIStoreController().findGenericProductsStore(store.getId());
			this.gemericCategories = Comunicacion.getInstance().getIStoreController().findGenericCategoriesStore(store.getId());
			this.buyLists = Comunicacion.getInstance().getIStoreController().findBuyListsStore(store.getId());
				
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void onRowProductEdit(RowEditEvent event) {
		DataStock stock = (DataStock) event.getObject();
		try {
			Comunicacion.getInstance().getIStoreController().editProductBasic(stock,store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
        FacesMessage msg = new FacesMessage("Producto editado", ((DataStock) event.getObject()).getProduct().getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowProductCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición cancelada", ((DataStock) event.getObject()).getProduct().getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
//    public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//         
//        if(newValue != null && !newValue.equals(oldValue)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celda cambiada", "Vieja: " + oldValue + ", Nueva:" + newValue);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }
    
    public void onRowBuyListEdit(RowEditEvent event) {
    	DataElementBuyList element = (DataElementBuyList) event.getObject();
		try {
			Comunicacion.getInstance().getIStoreController().editElementBuyList(element);
		} catch (NamingException e) {
			e.printStackTrace();
		}
        FacesMessage msg = new FacesMessage("Producto editado", ((DataProduct) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowBuyListCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición cancelada", ((DataElementBuyList) event.getObject()).getProduct().getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
//    public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//         
//        if(newValue != null && !newValue.equals(oldValue)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celda cambiada", "Vieja: " + oldValue + ", Nueva:" + newValue);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }
    
    public String showProduct() {
		return "/pages/ProductDetail.xhtml?faces-redirect=true";
	}
    
	public String createProduct() {
		return "/pages/NewProduct.xhtml?faces-redirect=true";
	}
	
	public String createCategory() {
		return "/pages/NewCategory.xhtml?faces-redirect=true";
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

	public int getCategorySelected() {
		return categorySelected;
	}

	public void setCategorySelected(int categorySelected) {
		this.categorySelected = categorySelected;
	}

	public List<DataStock> getStocks() {
		return stocks;
	}

	public void setStocks(List<DataStock> stocks) {
		this.stocks = stocks;
	}

	public List<DataCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<DataCategory> categories) {
		this.categories = categories;
	}

//	public List<DataProduct> getGemericProducts() {
//		return gemericProducts;
//	}
//
//	public void setGemericProducts(List<DataProduct> gemericProducts) {
//		this.gemericProducts = gemericProducts;
//	}

	public List<DataCategory> getGemericCategories() {
		return gemericCategories;
	}

	public void setGemericCategories(List<DataCategory> gemericCategories) {
		this.gemericCategories = gemericCategories;
	}

	public int getProductSelected() {
		return productSelected;
	}

	public void setProductSelected(int productSelected) {
		this.productSelected = productSelected;
	}

	public List<DataBuyList> getBuyLists() {
		return buyLists;
	}

	public void setBuyLists(List<DataBuyList> buyLists) {
		this.buyLists = buyLists;
	}
	
}
