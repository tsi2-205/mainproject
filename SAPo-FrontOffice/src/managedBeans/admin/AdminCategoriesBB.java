package managedBeans.admin;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import managedBeans.SessionBB;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;

import datatypes.DataCategory;
import datatypes.DataStore;
import exceptions.ExistStoreException;
import exceptions.NoDeleteCategoryException;

@ManagedBean
@ViewScoped
public class AdminCategoriesBB {
	
	private String name;
    private String description;
    private String nameNew;
    private String descriptionNew;
	private DataCategory categorySelected = null;
	private List<DataCategory> categories = new LinkedList<DataCategory>();
	private TreeNode root;
	private TreeNode selectedNode;
	private boolean hayCategorias;
	private boolean hayCategoriaSeleccionada = false;
	private boolean seeCreateNewSeleccionada = false;
	private boolean seeCreateSubSeleccionada = false;
	
	
	public AdminCategoriesBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		this.constructCategoryTree();
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
		this.categorySelected = (DataCategory) selectedNode.getData();
		this.name = this.categorySelected.getName();
		this.description = this.categorySelected.getDescription();
		this.hayCategoriaSeleccionada = true;
    }
	
    public void seeCreateNew() {
		this.seeCreateNewSeleccionada = true;
	}
    
    public String seeCreateSub() {
		this.seeCreateSubSeleccionada = true;
		return "";
	}
    
    public String clearCategorySelect() {
		this.categorySelected = null;
		this.name = null;
		this.description = null;
		this.hayCategoriaSeleccionada = false;
		this.seeCreateSubSeleccionada = false;
		this.seeCreateNewSeleccionada = false;
		return "";
	}
    
    public String createSub() {
		String ret = "OkNewCategory";
		try {
			Comunicacion.getInstance().getIStoreController().createGenericCategory(this.nameNew, this.descriptionNew, this.categorySelected);
			this.constructCategoryTree();
			this.nameNew = null;
			this.descriptionNew = null;
			this.seeCreateSubSeleccionada = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Subcategoria creada"));
		} catch (Exception ex) {
			ret = "FailNewCategory";
			ex.printStackTrace();
		}
		return ret;
	}
    
    public String createNew() {
		String ret = "OkNewCategory";
		try {
			Comunicacion.getInstance().getIStoreController().createGenericCategory(this.nameNew, this.descriptionNew, null);
			this.constructCategoryTree();
			this.nameNew = null;
			this.descriptionNew = null;
			this.seeCreateNewSeleccionada = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Categoria creada"));
		} catch (Exception ex) {
			ret = "FailNewCategory";
			ex.printStackTrace();
		}
		return ret;
	}
    
    public String editCategorySelect() {
    	String ret = "OkNewCategory";
		try {
			Comunicacion.getInstance().getIStoreController().editGenericCategory(this.name, this.description, this.categorySelected);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Categoria editada"));
			this.constructCategoryTree();
		} catch (Exception e) {
			ret = "FailNewCategory";
			this.categorySelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
		}
		return ret;
    }
    
    public String deleteCategorySelect() {
    	String ret = "OkNewCategory";
		try {
			Comunicacion.getInstance().getIStoreController().deleteGenericCategory(this.categorySelected);
			this.constructCategoryTree();
			this.categorySelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Categoria borrada"));
		} catch (NoDeleteCategoryException e) {
			ret = "FailNewCategory";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
		} catch (Exception ex) {
			ret = "FailNewCategory";
			ex.printStackTrace();
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

	public String getNameNew() {
		return nameNew;
	}

	public void setNameNew(String nameNew) {
		this.nameNew = nameNew;
	}

	public String getDescriptionNew() {
		return descriptionNew;
	}

	public void setDescriptionNew(String descriptionNew) {
		this.descriptionNew = descriptionNew;
	}

	public DataCategory getCategorySelected() {
		return categorySelected;
	}

	public void setCategorySelected(DataCategory categorySelected) {
		this.categorySelected = categorySelected;
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

	public boolean isHayCategoriaSeleccionada() {
		return hayCategoriaSeleccionada;
	}

	public void setHayCategoriaSeleccionada(boolean hayCategoriaSeleccionada) {
		this.hayCategoriaSeleccionada = hayCategoriaSeleccionada;
	}

	public boolean isSeeCreateNewSeleccionada() {
		return seeCreateNewSeleccionada;
	}

	public void setSeeCreateNewSeleccionada(boolean seeCreateNewSeleccionada) {
		this.seeCreateNewSeleccionada = seeCreateNewSeleccionada;
	}

	public boolean isSeeCreateSubSeleccionada() {
		return seeCreateSubSeleccionada;
	}

	public void setSeeCreateSubSeleccionada(boolean seeCreateSubSeleccionada) {
		this.seeCreateSubSeleccionada = seeCreateSubSeleccionada;
	}
    
}
