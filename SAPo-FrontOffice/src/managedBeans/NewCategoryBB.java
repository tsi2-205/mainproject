package managedBeans;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataStore;
import exceptions.ExistStoreException;

@ManagedBean
@ViewScoped
public class NewCategoryBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String description;
	private DataStore store;
	private DataCategory fatherCategory;
	private SessionBB session;
	
	public NewCategoryBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		session = (SessionBB) ve.getValue(contextoEL);
		this.store = session.getStoreSelected();
		this.fatherCategory = session.getCategorySelected();
	}
	
	public String create() {
		String ret = "OkNewCategory";
		
		try {
			Comunicacion.getInstance().getICategoryController().createSpecificCategory(this.name, this.description, this.store, this.fatherCategory);
			session.setCategorySelected(null);
		} catch (ExistStoreException e) {
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

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataCategory getFatherCategory() {
		return fatherCategory;
	}

	public void setFatherCategory(DataCategory fatherCategory) {
		this.fatherCategory = fatherCategory;
	}
	
}