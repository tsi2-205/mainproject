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
import javax.naming.NamingException;

import managedBeans.SessionBB;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataProduct;
import datatypes.DataProductAdditionalAttribute;

@ManagedBean
@ViewScoped
public class GenericProductBB {
	
	private DataProduct product;
	private DataCategory category;
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	private DataProductAdditionalAttribute attributeSelected = null;
	
	private String additionalAttributeName = null;
	private String additionalAttributeValue = null;
	
	public GenericProductBB() {
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
		this.product = session.getProductSelected();
		try {
			this.category = Comunicacion.getInstance().getIStoreController().findCategoryProduct(this.product.getId());
			this.constructCategoryTree();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String addAttribute() {
		String ret = "OkAddAttribute";
		DataProductAdditionalAttribute dpaa = new DataProductAdditionalAttribute(this.additionalAttributeName, this.additionalAttributeValue);
		this.additionalAttributeName = null;
		this.additionalAttributeValue = null;
		this.product.getAdditionalAttributes().add(dpaa);
		return ret;
	}
	
//	public void onAttributeEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//        
//        
//        
//    }
	
	public void deleteAttributeSelected() {
		try {
			if (this.attributeSelected.getId() > 0) {
				Comunicacion.getInstance().getIProductController().deleteAttributeProduct(this.product.getId(), this.attributeSelected.getId());
			}
			this.product.getAdditionalAttributes().remove(this.attributeSelected);
			this.attributeSelected = null;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String saveChanges() {
		String ret = "OkEditProduct";
		
		try {
			if (this.selectedNode == null) {
				Comunicacion.getInstance().getIProductController().editGenericProduct(this.product, this.category.getId());
			} else {
				Comunicacion.getInstance().getIProductController().editGenericProduct(this.product, ((DataCategory)selectedNode.getData()).getId());
			}
			
		} catch (Exception e) {
			ret = "FailEditProduct";
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void constructCategoryTree() {
		List<DataCategory> categories = new LinkedList<DataCategory>();
		try {
    		categories = Comunicacion.getInstance().getICategoryController().findGenericCategories();
    		
//			this.gemericCategories = Comunicacion.getInstance().getIStoreController().findGenericCategoriesStore(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
    	this.root = new DefaultTreeNode(new DataCategory(51, "root", "", false), null);
    	for (DataCategory dCat: categories) {
    		constructNodeTree(dCat, this.root);
    	}
    	
    }
    
    public void constructNodeTree(DataCategory dCat, TreeNode nodoPadre) {
    	TreeNode nodo = new DefaultTreeNode(dCat, nodoPadre);
    	if (dCat.getId() == this.category.getId()) {
    		this.selectedNode = nodo;
    	}
    	for (DataCategory dCatSon: dCat.getSonsCategories()) {
    		constructNodeTree(dCatSon, nodo);
    	}
    }
	
	public String editProduct() {
		return "/pages/EditGenericProduct.xhtml?faces-redirect=true";
	}
	
	public String volver() {
		return "/pages/AdminProducts.xhtml?faces-redirect=true";
	}

	public DataProduct getProduct() {
		return product;
	}

	public void setProduct(DataProduct product) {
		this.product = product;
	}

	public DataCategory getCategory() {
		return category;
	}

	public void setCategory(DataCategory category) {
		this.category = category;
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

	public String getAdditionalAttributeName() {
		return additionalAttributeName;
	}

	public void setAdditionalAttributeName(String additionalAttributeName) {
		this.additionalAttributeName = additionalAttributeName;
	}

	public String getAdditionalAttributeValue() {
		return additionalAttributeValue;
	}

	public void setAdditionalAttributeValue(String additionalAttributeValue) {
		this.additionalAttributeValue = additionalAttributeValue;
	}

	public DataProductAdditionalAttribute getAttributeSelected() {
		return attributeSelected;
	}

	public void setAttributeSelected(
			DataProductAdditionalAttribute attributeSelected) {
		this.attributeSelected = attributeSelected;
	}
	
}
