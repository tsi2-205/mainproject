package managedBeans;

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

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;

import datatypes.DataCategory;
import datatypes.DataProduct;
import datatypes.DataStock;

@ManagedBean
@ViewScoped
public class ListGenericProductsBB {
	
	private DataProduct productSelected = null;
	private List<DataProduct> products = new LinkedList<DataProduct>();
	private List<DataProduct> productsFiltered = new LinkedList<DataProduct>();
	private List<DataCategory> categories = new LinkedList<DataCategory>();
	private TreeNode root;
	private TreeNode selectedNode;
	private boolean hayCategorias;
	private String textFilter = "";
	
	public ListGenericProductsBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
//		FacesContext context = FacesContext.getCurrentInstance();
//		ELContext contextoEL = context.getELContext( );
//		Application apli  = context.getApplication( );	
//		ExpressionFactory ef = apli.getExpressionFactory( );
//		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
//		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		try {
			this.products = Comunicacion.getInstance().getIStoreController().findGenericsProducts(null);
			this.productsFiltered =this.products; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.constructCategoryTree();
	}
	
	public void filtrar() {
		this.productsFiltered = new LinkedList<DataProduct>();
		for (DataProduct dp: this.products) {
			String name = dp.getName();
			String t = this.textFilter;
			if (name.toLowerCase().contains(t.toLowerCase())) {
				this.productsFiltered.add(dp);
			}
		}
	}
	
	public void filtered(AjaxBehaviorEvent event) {
		filtrar();
	}
    
    public void constructCategoryTree() {
    	try {
			this.categories= Comunicacion.getInstance().getIStoreController().findGenericCategories();
			if (this.categories.isEmpty()) {
				this.hayCategorias = false;
			} else {
				this.hayCategorias = true;
			}
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
			this.products = Comunicacion.getInstance().getIStoreController().findGenericsProducts(((DataCategory)selectedNode.getData()).getId());
			filtrar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String chooseProduct() {
    	String ret = "/pages/NewProduct.xhtml?faces-redirect=true";
    	if(this.productSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		DataStock ds = new DataStock(null, null, this.productSelected);
    		session.setStockSelected(ds);
//    		try {
//				Comunicacion.getInstance().getIStoreController().addGenericProductStore(this.productSelected.getId(), session.getStoreSelected().getId());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        } else {
        	ret = "";
        }
    	
		return ret;
	}
    
    public String showProduct() {
    	String ret = "/pages/GenericProduct.xhtml?faces-redirect=true";
    	if(this.productSelected != null) {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ELContext contextoEL = context.getELContext( );
    		Application apli  = context.getApplication( );	
    		ExpressionFactory ef = apli.getExpressionFactory( );
    		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
    		SessionBB session = (SessionBB) ve.getValue(contextoEL);
    		session.setProductSelected(this.productSelected);
        } else {
        	ret = "";
        }
    	
		return ret;
	}
    
	public String volver() {
		return "/pages/NewProduct.xhtml?faces-redirect=true";
	}

	public DataProduct getProductSelected() {
		return productSelected;
	}

	public void setProductSelected(DataProduct productSelected) {
		this.productSelected = productSelected;
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

	public boolean isHayCategorias() {
		return hayCategorias;
	}

	public void setHayCategorias(boolean hayCategorias) {
		this.hayCategorias = hayCategorias;
	}

	public List<DataProduct> getProductsFiltered() {
		return productsFiltered;
	}

	public void setProductsFiltered(List<DataProduct> productsFiltered) {
		this.productsFiltered = productsFiltered;
	}

	public String getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}
	
}
