package managedBeans;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import comunication.Comunicacion;


@ManagedBean
public class LoginBB {
	private String name;
	private String email;
	private String password;
	private String fbId;
	private boolean showError;
	
	public LoginBB() {
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
			if (!isUserLogged) {
				this.showError = true;
				ret = "loginError";
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String loginWithFacebook() {
		String ret = "loginOk";
		this.showError = false;
		try {
			boolean isUserLogged = Comunicacion.getInstance().getIUserController().isUserLogged(this.email, this.password);
			if (!isUserLogged) {
				this.showError = true;
				ret = "loginError";
			}
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
				Comunicacion.getInstance().getIUserController().registerUser(this.email, this.password, this.name);;
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String goToRegister() {
		return "goToRegister";
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Object session = externalContext.getSession(false);
		HttpSession httpSession = (HttpSession) session;
		httpSession.invalidate();
		return "logoutOk";
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

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}
	
}
