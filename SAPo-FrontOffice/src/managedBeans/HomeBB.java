package managedBeans;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import comunication.Comunicacion;

import datatypes.DataStore;


@ManagedBean
@ViewScoped
public class HomeBB {
	private int id;
	private String name;
	private String idEmail;
	private String idFace;
	private List<DataStore> storesOwner;
	private List<DataStore> storesGuest;
	private DataStore storeSelected;
	

	@PostConstruct
	private void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		if (session.chequearAcceso(2)) {
			this.id=session.getLoggedUser().getId();
			session.setStoreSelected(null);
			session.setCategorySelected(null);
			session.setBuyListSelected(null);
			session.setProductSelected(null);
			session.setStockSelected(null);
			
			try{
				this.storesOwner = Comunicacion.getInstance().getIUserController().getStoresOwner(this.id);
				this.storesGuest= Comunicacion.getInstance().getIUserController().getStoresGuest(this.id);
					
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public HomeBB() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdEmail() {
		return idEmail;
	}

	public void setIdEmail(String id) {
		this.idEmail = id;
	}
	
	public String getIdFace() {
		return idFace;
	}

	public void setIdFace(String f) {
		this.idFace = f;
	}

	public List<DataStore> getStoresOwner() {
		return storesOwner;
	}

	public void setStoresOwner(List<DataStore> storesOwner) {
		this.storesOwner = storesOwner;
	}

	public List<DataStore> getStoresGuest() {
		return storesGuest;
	}

	public void setStoresGuest(List<DataStore> storesGuest) {
		this.storesGuest = storesGuest;
	}

	public DataStore getStoreSelected() {
		return storeSelected;
	}

	public void setStoreSelected(DataStore storeSelected) {
		this.storeSelected = storeSelected;
	}

	public String showStore() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		session.setStoreSelected(this.storeSelected);
		try {
			session.setCssCustom(Comunicacion.getInstance().getIStoreController().getCustomizeStore(this.storeSelected.getId()));
		} catch (SQLException | IOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	File f =this.storeSelected.getFile();
		System.out.println(f.length());
		OutputStream oos;
		try {
			oos = new FileOutputStream("customer"+storeSelected.getId());
			byte[] buf = new byte[1024];
			InputStream is = new FileInputStream(f);
			System.out.println(is.available());
			int c = 0; 
			while ((c = is.read(buf, 0, buf.length)) > 0) { 
				oos.write(buf, 0, c); 
				System.out.println(buf.toString());
				oos.flush(); 
			} 
			oos.close(); 
			session.setCssCustom(oos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		return "/pages/StoreMoves.xhtml?faces-redirect=true";
//		FacesContext faces = FacesContext.getCurrentInstance();
//		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
//		configurableNavigationHandler.performNavigation("/pages/StoreDetail.xhtml?faces-redirect=true");
	}
	
	public void createStore() throws NamingException {
//		FacesContext faces = FacesContext.getCurrentInstance();
//		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
//		configurableNavigationHandler.performNavigation("/pages/NewStore.xhtml?faces-redirect=true");
		if (Comunicacion.getInstance().getIUserController().createMoreStores(this.id)){
			ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
			configurableNavigationHandler.performNavigation("/pages/NewStore.xhtml?faces-redirect=true");
		}
		else{
			FacesMessage msg = new FacesMessage("Su cuenta debe ser premium");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		
	}
	
	
	
}