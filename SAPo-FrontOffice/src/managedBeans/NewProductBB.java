package managedBeans;
import java.io.Serializable;

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
import datatypes.DataProduct;
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
		
		
	}
	
	public String create() {
		String ret = "OkNewProduct";
		
		try {
			Comunicacion.getInstance().getIStoreController().createProduct(this.name, this.description, this.stockIni, this.precioCompra, this.precioVenta, store);
		} catch (NamingException e) {
			e.printStackTrace();
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

}