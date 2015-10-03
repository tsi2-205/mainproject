package managedBeans;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import comunication.Comunicacion;
import datatypes.DatosUsuario;


@ManagedBean
public class LoginBB {
	private String nick;
	private String pass;
	private boolean mostrarAdvertencia;
	private String nombre;
	private String mail;
	private String calle;
	private int numPuerta;
	private DatosUsuario usuario;
	
	
	public LoginBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		this.mostrarAdvertencia = false;
	}
	
	public String ingresar() {
		String retorno = "loginOk";
		this.mostrarAdvertencia = false;
		try {
			this.usuario = Comunicacion.getInstance().getIUserController().login(this.nick, this.pass);
			if (this.usuario == null) {
				this.mostrarAdvertencia = true;
				retorno = "loginError";
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	public String registrarse() {
		return "registrarse";
	}
	
	
	public String altaUsuario() {
		this.mostrarAdvertencia = false;
		try {
			Comunicacion.getInstance().getIUserController().registerUser(this.nick, this.pass, this.mail, this.nombre, this.calle, this.numPuerta);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return "altaOk";
	}

	
	
	
	public String logout() {
//		this.nick = null;
//		this.pass = null;
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Object session = externalContext.getSession(false);
		HttpSession httpSession = (HttpSession) session;
		httpSession.invalidate();
		return "Salir";
	}

	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}

	
	public boolean isMostrarAdvertencia() {
		return mostrarAdvertencia;
	}

	public void setMostrarAdvertencia(boolean mostrarAdvertencia) {
		this.mostrarAdvertencia = mostrarAdvertencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getNumPuerta() {
		return numPuerta;
	}

	public void setNumPuerta(int numPuerta) {
		this.numPuerta = numPuerta;
	}

	public DatosUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(DatosUsuario usuario) {
		this.usuario = usuario;
	}
	
	
	
}
