package managedBeans;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataProduct;

@ManagedBean
@ViewScoped
public class ShowGenericProductBB {
	
	private DataProduct product;
	private DataCategory category;
	
	public ShowGenericProductBB() {
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
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String volver() {
		return "/pages/ListGenericProducts.xhtml?faces-redirect=true";
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
	
}
