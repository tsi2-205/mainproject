package managedBeans;

import java.io.Serializable;
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

import org.primefaces.renderkit.DataRenderer;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreDetailBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DataStore store;
	private DataUser user;
	private int categorySelected;
	private List<DataProduct> products;
	private List<DataCategory> categories;
	private List<DataProduct> gemericProducts;
	private List<DataCategory> gemericCategories;
	
	
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
			this.products = Comunicacion.getInstance().getIStoreController().findSpecificProductsStore(store.getId());
			this.categories= Comunicacion.getInstance().getIStoreController().findSpecificCategoriesStore(store.getId());
			this.gemericProducts = Comunicacion.getInstance().getIStoreController().findGenericProductsStore(store.getId());
			this.gemericCategories= Comunicacion.getInstance().getIStoreController().findGenericCategoriesStore(store.getId());
				
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
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

	public List<DataProduct> getProducts() {
		return products;
	}

	public void setProducts(List<DataProduct> products) {
		this.products = products;
	}

	public List<DataCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<DataCategory> categories) {
		this.categories = categories;
	}

	public List<DataProduct> getGemericProducts() {
		return gemericProducts;
	}

	public void setGemericProducts(List<DataProduct> gemericProducts) {
		this.gemericProducts = gemericProducts;
	}

	public List<DataCategory> getGemericCategories() {
		return gemericCategories;
	}

	public void setGemericCategories(List<DataCategory> gemericCategories) {
		this.gemericCategories = gemericCategories;
	}
	
}
