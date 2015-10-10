package managedBeans;


import java.util.List;

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
	

	

	@PostConstruct
	private void init() {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{loginBB}",LoginBB.class);
		LoginBB bean = (LoginBB) ve.getValue(contextoEL);
		this.id=bean.getLoggedUser().getId();
		try{
			this.storesOwner = Comunicacion.getInstance().getIUserController().getStoresOwner(this.id);
			this.storesGuest= Comunicacion.getInstance().getIUserController().getStoresGuest(this.id);
				
		} catch (NamingException e) {
			e.printStackTrace();
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
	
	

	
}
