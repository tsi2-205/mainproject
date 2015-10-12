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
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class NewStoreBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String addr;
	private String tel;
	private String city;
	private DataUser user;
	
	public NewStoreBB() {
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
		
	}
	
	public String create() {
		String ret = "OkNewStore";
		
		try {
			Comunicacion.getInstance().getIStoreController().createStore(this.name, this.addr, this.tel, this.city, user);
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}
	
}
