package managedBeans;

import java.io.Serializable;
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

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;

import datatypes.DataCategory;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreProductsBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DataStore store;
	private DataUser user;
	private int categorySelected = -1;
	private DataStock productSelected;
	private List<DataStock> stocks = new LinkedList<DataStock>();
	private List<DataCategory> categories = new LinkedList<DataCategory>();
//	private List<DataProduct> gemericProducts = null;
	private List<DataCategory> gemericCategories = new LinkedList<DataCategory>();
	private TreeNode root;
	private TreeNode selectedNode;
	private boolean hayCategorias;
	private boolean isStoreOwner;
	
	
	public StoreProductsBB() {
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
    
    public void onCategorySelect(NodeSelectEvent event) {
    	try {
			this.stocks = Comunicacion.getInstance().getIStoreController().findStockProductsStore(store.getId(), ((DataCategory)selectedNode.getData()).getId());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String showProduct() {
    	String ret = "/pages/ShowProduct.xhtml?faces-redirect=true";
    	if(this.productSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		session.setStockSelected(this.productSelected);
        } else {
        	ret = "";
        }
    	
		return ret;
	}
    
	public String createProduct() {
		return "/pages/NewProduct.xhtml?faces-redirect=true";
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

	public List<DataCategory> getGemericCategories() {
		return gemericCategories;
	}

	public void setGemericCategories(List<DataCategory> gemericCategories) {
		this.gemericCategories = gemericCategories;
	}

	public DataStock getProductSelected() {
		return productSelected;
	}

	public void setProductSelected(DataStock productSelected) {
		this.productSelected = productSelected;
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
