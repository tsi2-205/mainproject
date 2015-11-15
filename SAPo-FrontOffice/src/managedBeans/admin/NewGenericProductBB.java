package managedBeans.admin;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataProductAdditionalAttribute;

@ManagedBean
@ViewScoped
public class NewGenericProductBB {
	
	private String name;
    private String description;
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	private List<DataProductAdditionalAttribute> additionalAttributes = new LinkedList<DataProductAdditionalAttribute>();
	
	private String additionalAttributeName = null;
	private String additionalAttributeValue = null;
	
	private DataProductAdditionalAttribute attributeSelected = null;
	
	public NewGenericProductBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
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
	
	public void deleteAttributeSelected() {
		this.additionalAttributes.remove(this.attributeSelected);
		this.attributeSelected = null;
	}
	
	public String create() {
		String ret = "OkNewProduct";
		
		try {
			if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
				if (this.name == null || this.name.trim().equals("")) {
					ret = "FailNewProduct";
					FacesMessage msg = new FacesMessage("Debe seleccionar la categroía en la que se va a incluir el producto");
			        FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					Comunicacion.getInstance().getIProductController().createGenericProduct(this.name, this.description, this.additionalAttributes, ((DataCategory)selectedNode.getData()).getId());
				}
			} else {
		        ret = "FailNewProduct";
				FacesMessage msg = new FacesMessage("Debe seleccionar la categroía en la que se va a incluir el producto");
		        FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
		} catch (Exception e) {
			ret = "FailNewProduct";
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void constructCategoryTree() {
		List<DataCategory> categories = new LinkedList<DataCategory>();
		try {
    		categories = Comunicacion.getInstance().getICategoryController().findGenericCategories();
		} catch (NamingException e) {
			e.printStackTrace();
		}
    	this.root = new DefaultTreeNode(new DataCategory(-2, "root", "", false), null);
    	this.root.setExpanded(true);
    	TreeNode raiz = new DefaultTreeNode(new DataCategory(-1, "CATEGORÍAS", "", false), this.root);
    	raiz.setExpanded(true);
    	for (DataCategory dCat: categories) {
    		constructNodeTree(dCat, raiz);
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

	public List<DataProductAdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(
			List<DataProductAdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
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
