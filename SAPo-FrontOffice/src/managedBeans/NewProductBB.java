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

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;

import datatypes.DataCategory;
import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class NewProductBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String description;
	private DataUser user;
	private DataStore store;
	private int stockIni;
	private int precioCompra;
	private int precioVenta;
	
	private DataStock stockSelected;
	private boolean isEdition = false;
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	private List<DataProductAdditionalAttribute> additionalAttributes = new LinkedList<DataProductAdditionalAttribute>();
	
	private String additionalAttributeName = null;
	private String additionalAttributeValue = null;
	
	public NewProductBB() {
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
		this.stockSelected = session.getStockSelected();
		if (this.stockSelected != null) {
			this.isEdition = true;
			this.name = this.stockSelected.getProduct().getName();
			this.description = this.stockSelected.getProduct().getDescription();
			this.stockIni = this.stockSelected.getCantidad();
			this.precioCompra = this.stockSelected.getPrecioCompra();
			this.precioVenta = this.stockSelected.getPrecioVenta();
			this.additionalAttributes = this.stockSelected.getProduct().getAdditionalAttributes(); 
		}
		this.constructCategoryTree();
	}
	
	public String addAttribute() {
		String ret = "OkAddAttribute";
		DataProductAdditionalAttribute dpaa = new DataProductAdditionalAttribute(this.additionalAttributeName, this.additionalAttributeValue);
		this.additionalAttributeName = null;
		this.additionalAttributeValue = null;
		this.additionalAttributes.add(dpaa);
		return ret;
	}
	
	public String create() {
		String ret = "OkNewProduct";
		
		try {
			if (!isEdition) {
				Comunicacion
						.getInstance()
						.getIStoreController()
						.createSpecificProduct(this.name, this.description,
								this.stockIni, this.precioCompra,
								this.precioVenta, this.store,
								this.additionalAttributes,
								((DataCategory) selectedNode.getData()).getId());
			} else {
				this.stockSelected.setCantidad(this.stockIni);
				this.stockSelected.setPrecioCompra(this.precioCompra);
				this.stockSelected.setPrecioVenta(this.precioVenta);
				Comunicacion
						.getInstance()
						.getIStoreController()
						.editProductStore(this.stockSelected, store.getId(),
								((DataCategory) selectedNode.getData()).getId());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void constructCategoryTree() {
		List<DataCategory> categories = new LinkedList<DataCategory>();
		try {
    		categories = Comunicacion.getInstance().getIStoreController().findSpecificCategoriesStore(store.getId());
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
    	for (DataCategory dCatSon: dCat.getSonsCategories()) {
    		constructNodeTree(dCatSon, nodo);
    	}
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

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public int getStockIni() {
		return stockIni;
	}

	public void setStockIni(int stockIni) {
		this.stockIni = stockIni;
	}

	public int getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(int precioCompra) {
		this.precioCompra = precioCompra;
	}

	public int getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(int precioVenta) {
		this.precioVenta = precioVenta;
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

	public List<DataProductAdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(
			List<DataProductAdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
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

	public boolean isEdition() {
		return isEdition;
	}

	public void setEdition(boolean isEdition) {
		this.isEdition = isEdition;
	}
	
}