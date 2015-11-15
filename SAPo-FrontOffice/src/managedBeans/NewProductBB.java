package managedBeans;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
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

import notifications.NotifyUserView;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

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
	private Integer stockMin = null;
	private Integer stockMax = null;
	
	private DataStock stockSelected;
	private boolean isEdition = false;
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	private List<DataProductAdditionalAttribute> additionalAttributes = new LinkedList<DataProductAdditionalAttribute>();
	
	private String additionalAttributeName = null;
	private String additionalAttributeValue = null;
	
	private DataProductAdditionalAttribute attributeSelected = null;
	
	private UploadedFile imagen;
	private File imagenFile;
	private boolean imagenCargada;
	private StreamedContent imagenStream;
	
	
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
		//System.out.print(store.getFile().length());
		this.stockSelected = session.getStockSelected();
		this.name = null;
		
		this.imagen = null;
		this.imagenFile = null;
		this.imagenStream = null;
    	this.imagenCargada = false;
    	
		this.description = null;
		if (this.stockSelected != null) {
			this.isEdition = true;
			this.name = this.stockSelected.getProduct().getName();
			this.description = this.stockSelected.getProduct().getDescription();
			this.stockMin = this.stockSelected.getCantidadMin();
			this.stockMax = this.stockSelected.getCantidadMax();
			this.additionalAttributes = this.stockSelected.getProduct().getAdditionalAttributes();
		}
		this.constructCategoryTree();
	}
	
	
	
	public void subirImagen(FileUploadEvent event) {
		try {
			Comunicacion.getInstance().getIProductController().guardarImagenProducto(event.getFile().getInputstream());
			this.imagenCargada = true;
			this.imagenFile = Comunicacion.getInstance().getIProductController().asociarImagen("nombreNuevo");
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UploadedFile getImagen() {
		return imagen;
	}

	public void setImagen(UploadedFile imagen) {
		this.imagen = imagen;
	}
	
	public StreamedContent getImagenStream() {
		try {
			if (this.imagenFile != null) {
				if (this.imagenFile.exists()){
//					this.imagenStream = new DefaultStreamedContent(new FileInputStream(this.imagenFile));
					this.imagenStream = new DefaultStreamedContent(new FileInputStream(new File(this.imagenFile.getAbsolutePath())));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return imagenStream;
	}

	public void setImagenStream(StreamedContent imagenStream) {
		this.imagenStream = imagenStream;
	}

	public boolean isImagenCargada() {
		return imagenCargada;
	}

	public void setImagenCargada(boolean imagenCargada) {
		this.imagenCargada = imagenCargada;
	}
	
	
	public File getImagenFile() {
		return imagenFile;
	}

	public void setImagenFile(File imagenFile) {
		this.imagenFile = imagenFile;
	}

	public String addAttribute() {
		String ret = "OkAddAttribute";
		DataProductAdditionalAttribute dpaa = new DataProductAdditionalAttribute(this.additionalAttributeName, this.additionalAttributeValue);
		this.additionalAttributeName = null;
		this.additionalAttributeValue = null;
		this.additionalAttributes.add(dpaa);
		return ret;
	}
	
	public void deleteAttributeSelectedCreate() {
		this.additionalAttributes.remove(this.attributeSelected);
		this.attributeSelected = null;
	}
	
	public void deleteAttributeSelectedEdition() {
		this.stockSelected.getProduct().getAdditionalAttributes().remove(this.attributeSelected);
		this.attributeSelected = null;
	}
	
	public String create() {
		String ret = "OkNewProduct";
		
		try {
			if (!isEdition) {
				if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
					Comunicacion
							.getInstance()
							.getIProductController()
							.createSpecificProduct(this.name, this.description,
									this.stockMin, this.stockMax, this.store.getId(),
									this.additionalAttributes,
									((DataCategory) selectedNode.getData()).getId());
					if (Comunicacion.getInstance().getIProductController().shouldPromoteProduct(this.name)) {
						String message = "Se sugiere promover el producto " + this.name + " como producto generico";
						Comunicacion.getInstance().getINotificationController().sendAdminNotification(message);
						List<DataUser> administrators = Comunicacion.getInstance().getIUserController().getAdministrators();
						NotifyUserView notifyView = new NotifyUserView();
						for (DataUser admin : administrators) {
							notifyView.sendNotification(admin.getId(), message);
						}
					}
				} else {
					FacesMessage msg = new FacesMessage("Debe seleccionar la categroía en la que se va a incluir el producto");
			        FacesContext.getCurrentInstance().addMessage(null, msg);
			        ret = "FailNewProduct";
				}
			} else {
				this.stockSelected.getProduct().setName(this.name);
				this.stockSelected.getProduct().setDescription(this.description);
				this.stockSelected.setCantidadMin(this.stockMin);
				this.stockSelected.setCantidadMax(this.stockMax);
				this.stockSelected.getProduct().setAdditionalAttributes(this.additionalAttributes);
				if (this.stockSelected.getProduct().isGeneric()) {
					if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
						this.stockSelected.setCantidadMax(this.stockMax);
						this.stockSelected.setCantidadMin(this.stockMin);
						Integer idCategory = ((DataCategory) selectedNode.getData()).getId();
						Comunicacion
								.getInstance()
								.getIProductController()
								.editProductStore(this.stockSelected, store.getId(), idCategory);
					} else {
						FacesMessage msg = new FacesMessage("Debe seleccionar la categroía en la que se va a incluir el producto");
				        FacesContext.getCurrentInstance().addMessage(null, msg);
				        ret = "FailNewProduct";
					}
				} else {
					this.stockSelected.setCantidadMax(this.stockMax);
					this.stockSelected.setCantidadMin(this.stockMin);
					Integer idCategory = null;
					if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
						idCategory = ((DataCategory) selectedNode.getData()).getId();
					}
					Comunicacion
							.getInstance()
							.getIProductController()
							.editProductStore(this.stockSelected, store.getId(), idCategory);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void constructCategoryTree() {
		List<DataCategory> categories = new LinkedList<DataCategory>();
		try {
    		categories = Comunicacion.getInstance().getICategoryController().findSpecificCategoriesStore(store.getId());
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
    
    public String seeGenericProducts() {
    	return "/pages/ListGenericProducts.xhtml?faces-redirect=true";
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

	public Integer getStockMin() {
		return stockMin;
	}

	public void setStockMin(Integer stockMin) {
		this.stockMin = stockMin;
	}

	public Integer getStockMax() {
		return stockMax;
	}

	public void setStockMax(Integer stockMax) {
		this.stockMax = stockMax;
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

	public DataStock getStockSelected() {
		return stockSelected;
	}

	public void setStockSelected(DataStock stockSelected) {
		this.stockSelected = stockSelected;
	}

	public DataProductAdditionalAttribute getAttributeSelected() {
		return attributeSelected;
	}

	public void setAttributeSelected(
			DataProductAdditionalAttribute attributeSelected) {
		this.attributeSelected = attributeSelected;
	}
	
}