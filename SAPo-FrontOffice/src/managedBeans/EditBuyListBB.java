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
import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataElementBuyList;
import datatypes.DataStock;
import datatypes.DataStore;
import exceptions.ProductNotExistException;

@ManagedBean
@ViewScoped
public class EditBuyListBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private DataStore store;
	private DataBuyList buyListSelected;
	private SessionBB session;
	
	private List<DataElementBuyList> productsSelected = new LinkedList<DataElementBuyList>();
	private List<DataStock> productsNoSelected = new LinkedList<DataStock>();
	private DataElementBuyList selectedProduct;
	
	private TreeNode root;
	private TreeNode selectedNode;
	private List<DataCategory> categories = new LinkedList<DataCategory>();
    
	public EditBuyListBB() {
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
		this.constructCategoryTree();
		try {
			this.productsNoSelected = Comunicacion.getInstance().getIStoreController().findStockProductsStore(store.getId(), null);
			this.productsSelected = this.buyListSelected.getElements();
			this.name = this.buyListSelected.getName();
			this.description = this.buyListSelected.getDescription();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void constructCategoryTree() {
    	try {
			this.categories= Comunicacion.getInstance().getIStoreController().findSpecificCategoriesStore(store.getId());
//			this.gemericCategories = Comunicacion.getInstance().getIStoreController().findGenericCategoriesStore(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}

    	this.root = new DefaultTreeNode(new DataCategory(51, "root", "", false), null);
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
			this.productsNoSelected = Comunicacion.getInstance().getIStoreController().findStockProductsStore(store.getId(), ((DataCategory)this.selectedNode.getData()).getId());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void onProductDrop(DragDropEvent ddEvent) {
		DataStock p = ((DataStock) ddEvent.getData());
		boolean error = false;
		for (DataElementBuyList elem: this.productsSelected) {
			if (elem.getProduct().getId() == p.getProduct().getId()) {
				error = true;
				break;
			}
		}
		if (!error) {
			productsSelected.add(new DataElementBuyList(1, p.getProduct()));
//			productsNoSelected.remove(p);
		} else {
			FacesMessage msg = new FacesMessage("Ya agregó el producto ", p.getProduct().getName());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
    }
	
	public void remove() {
		productsSelected.remove(this.selectedProduct);
    }
	
	public String saveBuyList() {
		String ret = "okEditBuyList";
		try {
			Comunicacion.getInstance().getIStoreController().editBuyListStore(store.getId(), this.productsSelected, this.name, this.description, this.buyListSelected);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Lista editada con éxito"));
		} catch (ProductNotExistException enep) {
			ret = "failEditBuyList";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", enep.getMessage()));
		} catch (Exception e) {
			ret = "failEditBuyList";
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

	public List<DataElementBuyList> getProductsSelected() {
		return productsSelected;
	}

	public void setProductsSelected(List<DataElementBuyList> productsSelected) {
		this.productsSelected = productsSelected;
	}

	public List<DataStock> getProductsNoSelected() {
		return productsNoSelected;
	}

	public void setProductsNoSelected(List<DataStock> productsNoSelected) {
		this.productsNoSelected = productsNoSelected;
	}

	public DataElementBuyList getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(DataElementBuyList selectedProduct) {
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

	public DataBuyList getBuyListSelected() {
		return buyListSelected;
	}

	public void setBuyListSelected(DataBuyList buyListSelected) {
		this.buyListSelected = buyListSelected;
	}

	public List<DataCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<DataCategory> categories) {
		this.categories = categories;
	}
	
	public void buyRecommendation() throws NamingException{
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
		configurableNavigationHandler.performNavigation("/pages/RecommendationBuy.xhtml?faces-redirect=true");
	}
	
}
