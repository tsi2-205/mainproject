package managedBeans.admin;

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
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.NamingException;

import managedBeans.SessionBB;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataStock;
import datatypes.DataStore;

@ManagedBean
@ViewScoped
public class AdminStoreProductsBB {

	private DataStore store;
	private int categorySelected = -1;
	private DataStock stockSelected = null;
	private List<DataStock> stocks = new LinkedList<DataStock>();
	private List<DataCategory> categories = new LinkedList<DataCategory>();
	private TreeNode root;
	private TreeNode selectedNode;
	private boolean hayCategorias;
	
	// Filtro
	private List<DataStock> stocksFiltered = new LinkedList<DataStock>();
	private String textFilter = "";
	
	
	public AdminStoreProductsBB() {
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
		try{
			this.stocks = Comunicacion.getInstance().getIProductController().findStockProductsStore(store.getId(), null);
			this.stocksFiltered =this.stocks;
			this.constructCategoryTree();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void filtrar() {
		this.stocksFiltered = new LinkedList<DataStock>();
		for (DataStock ds: this.stocks) {
			String name = ds.getProduct().getName();
			String t = this.textFilter;
			if (name.toLowerCase().startsWith(t.toLowerCase())) {
				this.stocksFiltered.add(ds);
			}
		}
	}
	
	public void filtered(AjaxBehaviorEvent event) {
		filtrar();
	}
    
    public void constructCategoryTree() {
    	try {
			this.categories = Comunicacion.getInstance().getICategoryController().findSpecificCategoriesStore(store.getId());
			if (this.categories.isEmpty()) {
				this.hayCategorias = false;
			} else {
				this.hayCategorias = true;
			}
			filtrar();
		} catch (NamingException e) {
			e.printStackTrace();
		}
    	this.root = new DefaultTreeNode(new DataCategory(-2, "root", "", false), null);
    	this.root.setExpanded(true);
    	TreeNode raiz = new DefaultTreeNode(new DataCategory(-1, "CATEGORÍAS", "", false), this.root);
    	raiz.setExpanded(true);
    	for (DataCategory dCat: this.categories) {
    		constructNodeTree(dCat, raiz);
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
    		if (((DataCategory)selectedNode.getData()).getId() == -1) {
    			this.stocks = Comunicacion.getInstance().getIProductController().findStockProductsStore(store.getId(), null);
    		} else {
    			this.stocks = Comunicacion.getInstance().getIProductController().findStockProductsStore(store.getId(), ((DataCategory)selectedNode.getData()).getId());
    		}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String showProduct() {
    	FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		session.setStockSelected(this.stockSelected);
		return "/pages/AdminShowProduct.xhtml?faces-redirect=true";
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
	
	public DataStock getStockSelected() {
		return stockSelected;
	}

	public void setStockSelected(DataStock stockSelected) {
		this.stockSelected = stockSelected;
	}

	public boolean isHayCategorias() {
		return hayCategorias;
	}

	public void setHayCategorias(boolean hayCategorias) {
		this.hayCategorias = hayCategorias;
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

	public List<DataStock> getStocksFiltered() {
		return stocksFiltered;
	}

	public void setStocksFiltered(List<DataStock> stocksFiltered) {
		this.stocksFiltered = stocksFiltered;
	}

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}

}
