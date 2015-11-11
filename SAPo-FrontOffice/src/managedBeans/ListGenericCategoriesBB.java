package managedBeans;

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

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataStore;
import exceptions.ExistStoreException;

@ManagedBean
@ViewScoped
public class ListGenericCategoriesBB {
	
	private DataStore store;
	private DataCategory categorySelectedG = null;
	private DataCategory categorySelectedE = null;
	private List<DataCategory> categoriesG = new LinkedList<DataCategory>();
	private List<DataCategory> categoriesE = new LinkedList<DataCategory>();
	private TreeNode rootG;
	private TreeNode rootE;
	private TreeNode selectedNodeG;
	private TreeNode selectedNodeE;
	private boolean hayCategoriasG;
	private boolean hayCategoriasE;
	
	public ListGenericCategoriesBB() {
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
		this.constructCategoryTreeG();
		this.constructCategoryTreeE();
		
	}
	
	public void constructCategoryTreeG() {
		try {
			this.categoriesG = Comunicacion.getInstance().getIStoreController().findGenericCategories();
			if (this.categoriesG.isEmpty()) {
				this.hayCategoriasG = false;
			} else {
				this.hayCategoriasG = true;
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

    	this.rootG = new DefaultTreeNode(new DataCategory(51, "root", "", false), null);
    	for (DataCategory dCat: this.categoriesG) {
    		constructNodeTreeG(dCat, this.rootG);
    	}
    }
	
	public void constructNodeTreeG(DataCategory dCat, TreeNode nodoPadre) {
    	TreeNode nodo = new DefaultTreeNode(dCat, nodoPadre);
    	for (DataCategory dCatSon: dCat.getSonsCategories()) {
    		constructNodeTreeG(dCatSon, nodo);
    	}
    }
    
    public void constructCategoryTreeE() {
		try {
			this.categoriesE = Comunicacion.getInstance().getIStoreController().findSpecificCategoriesStore(store.getId());
			if (this.categoriesE.isEmpty()) {
				this.hayCategoriasE = false;
			} else {
				this.hayCategoriasE = true;
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

    	this.rootE = new DefaultTreeNode(new DataCategory(51, "root", "", false), null);
    	for (DataCategory dCat: this.categoriesE) {
    		constructNodeTreeE(dCat, this.rootE);
    	}
    }
    
    public void constructNodeTreeE(DataCategory dCat, TreeNode nodoPadre) {
    	TreeNode nodo = new DefaultTreeNode(dCat, nodoPadre);
    	for (DataCategory dCatSon: dCat.getSonsCategories()) {
    		constructNodeTreeE(dCatSon, nodo);
    	}
    }
    
    public void addCategorySelected() {
    	if (selectedNodeG == null) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "No seleccionó ninguna categoría genérica"));
    	} else {
    		if (selectedNodeE != null) {
    			this.categorySelectedG = (DataCategory) selectedNodeG.getData();
            	this.categorySelectedE = (DataCategory) selectedNodeE.getData();
            	try {
        			Comunicacion.getInstance().getIStoreController().createSpecificCategory(this.categorySelectedG.getName(), this.categorySelectedG.getDescription(), this.store, this.categorySelectedE);
        			this.constructCategoryTreeE();
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Categoría agregada"));
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
    		} else {
    			this.categorySelectedG = (DataCategory) selectedNodeG.getData();
    	    	try {
    				Comunicacion.getInstance().getIStoreController().createSpecificCategory(this.categorySelectedG.getName(), this.categorySelectedG.getDescription(), this.store, null);
    				this.constructCategoryTreeE();
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Categoría agregada"));
    			} catch (Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
	}
    
    public void addTreeSelected() {
    	this.categorySelectedG = (DataCategory) selectedNodeG.getData();
    	this.categorySelectedE = (DataCategory) selectedNodeE.getData();
    	
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Falta implementar"));
	}
    
    public String volver() {
    	return "/pages/CategoryDetail.xhtml?faces-redirect=true";
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataCategory getCategorySelectedG() {
		return categorySelectedG;
	}

	public void setCategorySelectedG(DataCategory categorySelectedG) {
		this.categorySelectedG = categorySelectedG;
	}

	public DataCategory getCategorySelectedE() {
		return categorySelectedE;
	}

	public void setCategorySelectedE(DataCategory categorySelectedE) {
		this.categorySelectedE = categorySelectedE;
	}

	public List<DataCategory> getCategoriesG() {
		return categoriesG;
	}

	public void setCategoriesG(List<DataCategory> categoriesG) {
		this.categoriesG = categoriesG;
	}

	public List<DataCategory> getCategoriesE() {
		return categoriesE;
	}

	public void setCategoriesE(List<DataCategory> categoriesE) {
		this.categoriesE = categoriesE;
	}

	public TreeNode getRootG() {
		return rootG;
	}

	public void setRootG(TreeNode rootG) {
		this.rootG = rootG;
	}

	public TreeNode getRootE() {
		return rootE;
	}

	public void setRootE(TreeNode rootE) {
		this.rootE = rootE;
	}

	public TreeNode getSelectedNodeG() {
		return selectedNodeG;
	}

	public void setSelectedNodeG(TreeNode selectedNodeG) {
		this.selectedNodeG = selectedNodeG;
	}

	public TreeNode getSelectedNodeE() {
		return selectedNodeE;
	}

	public void setSelectedNodeE(TreeNode selectedNodeE) {
		this.selectedNodeE = selectedNodeE;
	}

	public boolean isHayCategoriasG() {
		return hayCategoriasG;
	}

	public void setHayCategoriasG(boolean hayCategoriasG) {
		this.hayCategoriasG = hayCategoriasG;
	}

	public boolean isHayCategoriasE() {
		return hayCategoriasE;
	}

	public void setHayCategoriasE(boolean hayCategoriasE) {
		this.hayCategoriasE = hayCategoriasE;
	}

}
