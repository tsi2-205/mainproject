package managedBeans;

import java.io.IOException;
import java.sql.SQLException;
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

import notifications.NotifyStoreView;

import org.primefaces.event.SelectEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import comunication.Comunicacion;
import datatypes.DataProduct;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreMovesBB {
	
	private DataStore store;
	private DataUser user;
	private String nameProduct;
	private DataProduct productSelected;
	private String productNameSelected;
	private List<DataProduct> products = new LinkedList<DataProduct>();
	private List<DataProduct> productsFiltered = new LinkedList<DataProduct>();
	private List<String> productsNamefiltered = new LinkedList<String>();
	
	private String movTipo = "Alta";
	private Integer movCant = null;
	private Integer movPrecio = null;
	
	private String productSelectedName;
	
	public StoreMovesBB() {
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
		if (session.chequearAcceso(3)) {
			this.user = session.getLoggedUser();
			this.store = session.getStoreSelected();
			session.setProductSelected(null);
			try{
				this.products = Comunicacion.getInstance().getIProductController().findProductsStore(store.getId(), null);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		try {
			session.setCssCustom(Comunicacion.getInstance().getIStoreController().getCustomizeStore(store.getId()));
		} catch (SQLException | IOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> completeName(String query) {
		this.productsNamefiltered = new LinkedList<String>();
		this.productsFiltered = new LinkedList<DataProduct>();
		for (DataProduct dp: this.products) {
			DataProduct skin = dp;
			if (skin.getName().toLowerCase().startsWith(query.toLowerCase())) {
				this.productsNamefiltered.add(skin.getName());
				this.productsFiltered.add(skin);
			}
		}
		return this.productsNamefiltered;
	}
	
	public void onProductSelect(SelectEvent event) {
		String name = event.getObject().toString();
		for (DataProduct dp: this.productsFiltered) {
			if (name.toLowerCase().equals(dp.getName().toLowerCase())) {
				this.productSelected = dp;
				break;
			}
		}
	}
	
	public String saveChangeStock() {
		String ret = "OkChangeStock";
		if (this.movCant != null && this.movPrecio != null) {
			int tipo = this.movTipo.equals("Alta") ? 1 : 0;
			try {
				int result = Comunicacion.getInstance().getIProductController().changeStockProduct(store.getId(), this.productSelected.getId(), this.movCant, this.movPrecio, tipo);
				if (result > 0) {
					String message;
					if (result == 1) {
						message = "El stock de " + this.productNameSelected + " esta por debajo del minimo.";
					} else {
						message = "El stock de " + this.productNameSelected + " esta por encima del maximo.";
					}
					Comunicacion.getInstance().getINotificationController().sendStoreNotification(message, this.store.getId(), true);
					NotifyStoreView notifyView = new NotifyStoreView();
					notifyView.sendNotification(this.store.getId(), message);
				}
				this.movCant = null;
				this.movPrecio = null;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cambio de stock realizado con éxito"));
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public String seeProductMoves() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		session.setProductSelected(this.productSelected);
		return "/pages/ProductMoves.xhtml?faces-redirect=true";
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

	public DataProduct getProductSelected() {
		return productSelected;
	}

	public void setProductSelected(DataProduct productSelected) {
		this.productSelected = productSelected;
	}

	public List<DataProduct> getProducts() {
		return products;
	}

	public void setProducts(List<DataProduct> products) {
		this.products = products;
	}

	public List<String> getProductsfiltered() {
		return productsNamefiltered;
	}

	public void setProductsfiltered(List<String> productsfiltered) {
		this.productsNamefiltered = productsfiltered;
	}

	public String getProductSelectedName() {
		return productSelectedName;
	}

	public void setProductSelectedName(String productSelectedName) {
		this.productSelectedName = productSelectedName;
	}

	public String getProductNameSelected() {
		return productNameSelected;
	}

	public void setProductNameSelected(String productNameSelected) {
		this.productNameSelected = productNameSelected;
	}

	public List<String> getProductsNamefiltered() {
		return productsNamefiltered;
	}

	public void setProductsNamefiltered(List<String> productsNamefiltered) {
		this.productsNamefiltered = productsNamefiltered;
	}

	public List<DataProduct> getProductsFiltered() {
		return productsFiltered;
	}

	public void setProductsFiltered(List<DataProduct> productsFiltered) {
		this.productsFiltered = productsFiltered;
	}

	public String getMovTipo() {
		return movTipo;
	}

	public void setMovTipo(String movTipo) {
		this.movTipo = movTipo;
	}

	public Integer getMovCant() {
		return movCant;
	}

	public void setMovCant(Integer movCant) {
		this.movCant = movCant;
	}

	public Integer getMovPrecio() {
		return movPrecio;
	}

	public void setMovPrecio(Integer movPrecio) {
		this.movPrecio = movPrecio;
	}
	
}
