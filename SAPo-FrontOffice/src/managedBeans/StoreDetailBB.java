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

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

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
	private List<DataStock> stocks = new LinkedList<DataStock>();
	private List<DataCategory> categories = new LinkedList<DataCategory>();
//	private List<DataProduct> gemericProducts = null;
	private List<DataCategory> gemericCategories = new LinkedList<DataCategory>();
	private List<DataBuyList> buyLists = new LinkedList<DataBuyList>();
	private TreeNode root;
	private TreeNode selectedNode;
	private boolean hayCategorias;
	private boolean isStoreOwner;
	
	
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
		if (session.chequearAcceso(3)) {
			this.user = session.getLoggedUser();
			this.store = session.getStoreSelected();
			this.isStoreOwner = this.user.getId() == this.store.getOwner().getId();
			session.setCategorySelected(null);
			session.setBuyListSelected(null);
			session.setProductSelected(null);
			session.setStockSelected(null);
			try{
				this.stocks = Comunicacion.getInstance().getIStoreController().findStockProductsStore(store.getId(), null);
//				this.gemericProducts = Comunicacion.getInstance().getIStoreController().findGenericProductsStore(store.getId());
				this.constructCategoryTree();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}
	
    public void constructCategoryTree() {
    	try {
			this.categories= Comunicacion.getInstance().getIStoreController().findSpecificCategoriesStore(store.getId());
//			this.gemericCategories = Comunicacion.getInstance().getIStoreController().findGenericCategoriesStore(store.getId());
			if (this.categories.isEmpty()) {
				this.hayCategorias = false;
			} else {
				this.hayCategorias = true;
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

    	this.root = new DefaultTreeNode(new DataCategory(51, "root", "ADFAdf", false), null);
    	for (DataCategory dCat: this.categories) {
    		constructNodeTree(dCat, this.root);
    	}
    	
    }
    
    public void constructNodeTree(DataCategory dCat, TreeNode nodoPadre) {
    	TreeNode nodo = new DefaultTreeNode(dCat, nodoPadre);
    	for (DataCategory dCatSon: dCat.getSonsCategories()) {
    		constructNodeTree(dCatSon, nodo);
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
    
    public void editCategory() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
//		session.setCategorySelected((DataCategory) this.selectedNode.getData());		
		FacesContext faces = FacesContext.getCurrentInstance();
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/CategoryDetail.xhtml?faces-redirect=true");
    }
    
    public void onCategorySelect(NodeSelectEvent event) {
    	try {
			this.stocks = Comunicacion.getInstance().getIStoreController().findStockProductsStore(store.getId(), ((DataCategory)selectedNode.getData()).getId());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String showProduct() {
		return "/pages/ProductDetail.xhtml?faces-redirect=true";
	}
    
	public String createProduct() {
		return "/pages/ListGenericProducts.xhtml?faces-redirect=true";
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

	public boolean isHayCategorias() {
		return hayCategorias;
	}

	public void setHayCategorias(boolean hayCategorias) {
		this.hayCategorias = hayCategorias;
	}

	public boolean isStoreOwner() {
		return isStoreOwner;
	}

	public void setStoreOwner(boolean isStoreOwner) {
		this.isStoreOwner = isStoreOwner;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	public String customizeButton() {
		return "/pages/Customize.xhtml?faces-redirect=true";
	}
}
