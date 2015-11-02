package managedBeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import comunication.Comunicacion;
import datatypes.DataBuyList;
import datatypes.DataCategory;
import datatypes.DataStore;
import datatypes.DataUser;


@ManagedBean
@SessionScoped
public class SessionBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private String password;
	private String fbId;
	private DataUser loggedUser;
	private boolean showError;
	private DataStore storeSelected;
	private DataCategory categorySelected;
	private DataBuyList buyListSelected;
	
	public SessionBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		this.showError = false;
	}
	
	public String loginWithEmail() {
		String ret = "loginOk";
		this.showError = false;
		try {
			boolean isUserLogged = Comunicacion.getInstance().getIUserController().isUserLogged(this.email, this.password);
//			SecurityUtils.getSubject().login(new UsernamePasswordToken(this.email, this.password, false));
			if (!isUserLogged) {
				this.showError = true;
				ret = "loginError";
			} else {
				this.loggedUser = Comunicacion.getInstance().getIUserController().getUserData(this.email);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnknownAccountException ex) {
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
		} catch (IncorrectCredentialsException ex) {
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
		} catch (LockedAccountException ex) {
			ex.printStackTrace();
		} catch (ExcessiveAttemptsException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	public String loginWithFacebook() {
		String ret = "loginOk";
		FacesContext context = FacesContext.getCurrentInstance();
	    Map map = context.getExternalContext().getRequestParameterMap();
	    this.fbId = (String) map.get("fbId");
	    this.name = (String) map.get("name");
		try {
			boolean isUserRegistered = Comunicacion.getInstance().getIUserController().isRegisteredFbUser(this.fbId);
			if (!isUserRegistered) {
				Comunicacion.getInstance().getIUserController().registerFbUser(this.fbId, this.name);
			} 
			this.loggedUser = Comunicacion.getInstance().getIUserController().getFbUserData(this.fbId);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String register() {
		String ret = "registerOk";
		this.showError = false;
		try {
			boolean isRegisteredUser = Comunicacion.getInstance().getIUserController().isRegisteredUser(this.email);
			if (isRegisteredUser) {
				this.showError = true;
				ret = "registerError";
			} else {
				Comunicacion.getInstance().getIUserController().registerUser(this.email, this.password, this.name, "F");
				this.loggedUser = Comunicacion.getInstance().getIUserController().getUserData(this.email);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String registerPremiun() {
		String ret = "registerOkPrem";
		this.showError = false;
		try {
			boolean isRegisteredUser = Comunicacion.getInstance().getIUserController().isRegisteredUser(this.email);
			if (isRegisteredUser) {
				this.showError = true;
				ret = "registerError";
			} else {
				Comunicacion.getInstance().getIUserController().registerUser(this.email, this.password, this.name, "P");
				this.loggedUser = Comunicacion.getInstance().getIUserController().getUserData(this.email);
				ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
				configurableNavigationHandler.performNavigation("/pages/Home.xhtml?faces-redirect=true");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String goToRegister() {
		return "goToRegister";
	}
	
	public String logoutUser() {
//		this.loggedUser = null;
//		FacesContext context = FacesContext.getCurrentInstance();
//		ExternalContext externalContext = context.getExternalContext();
//		Object session = externalContext.getSession(false);
//		HttpSession httpSession = (HttpSession) session;
//		httpSession.invalidate();
//		return "logoutOk";
		this.loggedUser = null;
		this.email = null;
		this.fbId = null;
		this.name = null;
		this.password = null;
		this.showError = false;
		this.storeSelected = null;
		SecurityUtils.getSubject().logout();
		return "/pages/Login?faces-redirect=true";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public DataUser getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(DataUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public DataStore getStoreSelected() {
		return storeSelected;
	}

	public void setStoreSelected(DataStore storeSelected) {
		this.storeSelected = storeSelected;
	}

	public DataCategory getCategorySelected() {
		return categorySelected;
	}

	public void setCategorySelected(DataCategory categorySelected) {
		this.categorySelected = categorySelected;
	}

	public DataBuyList getBuyListSelected() {
		return buyListSelected;
	}

	public void setBuyListSelected(DataBuyList buyListSelected) {
		this.buyListSelected = buyListSelected;
	}
	
	public void paypal() throws IOException {
		String ret=null;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	boolean isRegisteredUser = Comunicacion.getInstance().getIUserController().isRegisteredUser(this.email);
			if (isRegisteredUser) {
				this.showError = true;
				ret = "registerError";
			} else {
					ec.redirect("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=A575ACHN4DXME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void paypalChangeAccount() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
					ec.redirect("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=B5Q4JAN9TNZUJ");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void changeAccount(){
		if (this.loggedUser.getAccount().equals("P")){
			FacesMessage msg = new FacesMessage("Su Cuenta ya es Premium");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		else{
			ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
			configurableNavigationHandler.performNavigation("/pages/ChangeAccount.xhtml?faces-redirect=true");
		}
	}
	
	public String AccountPremiun() {
		String ret = "registerOkPrem";
		this.showError = false;
		try {
				Comunicacion.getInstance().getIUserController().setAccount(this.loggedUser.getId());
				if (this.loggedUser.getEmail()==null){
					this.loggedUser= Comunicacion.getInstance().getIUserController().getFbUserData(this.loggedUser.getFbId());
				}
				else{
					this.loggedUser= Comunicacion.getInstance().getIUserController().getUserData(this.loggedUser.getEmail());
				}

				ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
				configurableNavigationHandler.performNavigation("/pages/Home.xhtml?faces-redirect=true");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	
}
