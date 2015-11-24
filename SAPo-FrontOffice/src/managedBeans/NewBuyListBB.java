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

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataStock;
import datatypes.DataStore;
import exceptions.ProductNotExistException;

@ManagedBean
@ViewScoped
public class NewBuyListBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private DataStore store;
	private SessionBB session;
	
	private List<DataStock> productsSelected = new LinkedList<DataStock>();
	private List<DataStock> productsNoSelected = new LinkedList<DataStock>();
	private DataStock selectedProduct;
	
	private TreeNode root;
	private TreeNode selectedNode;
	private List<DataCategory> categories = new LinkedList<DataCategory>();
    
	public NewBuyListBB() {
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
		this.constructCategoryTree();
		try {
			this.productsNoSelected = Comunicacion.getInstance().getIProductController().findStockProductsStore(store.getId(), null);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void constructCategoryTree() {
    	try {
			this.categories= Comunicacion.getInstance().getICategoryController().findSpecificCategoriesStore(store.getId());
//			this.gemericCategories = Comunicacion.getInstance().getIStoreController().findGenericCategoriesStore(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}

    	this.root = new DefaultTreeNode(new DataCategory(-2, "root", "", false), null);
    	this.root.setExpanded(true);
    	TreeNode raiz = new DefaultTreeNode(new DataCategory(-1, "CATEGOR�AS", "", false), this.root);
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
				this.productsNoSelected = Comunicacion.getInstance().getIProductController().findStockProductsStore(store.getId(), null);
    		} else {
    			this.productsNoSelected = Comunicacion.getInstance().getIProductController().findStockProductsStore(store.getId(), ((DataCategory)this.selectedNode.getData()).getId());
    		}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void onProductDrop(DragDropEvent ddEvent) {
		DataStock p = ((DataStock) ddEvent.getData());
		boolean add=true;
		for (DataStock ds:productsSelected){
			if (ds.getProduct().getId()==p.getProduct().getId()){
				add= false;
			}
		}
		if (add) {
			p.setCantidad(1);
			productsSelected.add(p);
//			productsNoSelected.remove(p);
		} else {
			FacesMessage msg = new FacesMessage("Ya agreg� el producto ", p.getProduct().getName());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
    }
	
	public void remove() {
		productsSelected.remove(this.selectedProduct);
    }
	
	public String createBuyList() {
		String ret = "okNewBuyList";
		try {
			Comunicacion.getInstance().getIBuyListController().createBuyListStore(store.getId(), this.productsSelected, this.name, this.description);
		} catch (ProductNotExistException enep) {
			ret = "failNewBuyList";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", enep.getMessage()));
		} catch (Exception e) {
			ret = "failNewBuyList";
			e.printStackTrace();
		}
		return ret;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public SessionBB getSession() {
		return session;
	}

	public void setSession(SessionBB session) {
		this.session = session;
	}

	public List<DataStock> getProductsSelected() {
		return productsSelected;
	}

	public void setProductsSelected(List<DataStock> productsSelected) {
		this.productsSelected = productsSelected;
	}

	public List<DataStock> getProductsNoSelected() {
		return productsNoSelected;
	}

	public void setProductsNoSelected(List<DataStock> productsNoSelected) {
		this.productsNoSelected = productsNoSelected;
	}

	public DataStock getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(DataStock selectedProduct) {
		this.selectedProduct = selectedProduct;
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
	
	public void buyRecommendation() throws NamingException{
		this.productsSelected= Comunicacion.getInstance().getIBuyListController().buyRecommendation(this.store.getId());
	}
	
}
