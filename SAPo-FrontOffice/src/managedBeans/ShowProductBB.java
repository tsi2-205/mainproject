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
import datatypes.DataStock;

@ManagedBean
@ViewScoped
public class ShowProductBB {
	
	private DataStock stockSelected;
	private DataCategory category;
	private String prodImage;
	
	public ShowProductBB() {
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
		this.stockSelected = session.getStockSelected();
		try {
			this.category = Comunicacion.getInstance().getIStoreController().findCategoryProduct(this.stockSelected.getProduct().getId());
			this.prodImage = this.stockSelected.getProduct().getImage();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String getProdImage() {
		return prodImage;
	}
	
	
	public String editProduct() {
		return "/pages/NewProduct.xhtml?faces-redirect=true";
	}

	public DataStock getStockSelected() {
		return stockSelected;
	}

	public void setStockSelected(DataStock stockSelected) {
		this.stockSelected = stockSelected;
	}

	public DataCategory getCategory() {
		return category;
	}

	public void setCategory(DataCategory category) {
		this.category = category;
	}
	
}
