package managedBeans;

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

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import comunication.Comunicacion;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class ShareStoreBB {
	
	private DataUser loggedUser;
	
	private DataStore store;
	
	private List<DataUser> users;
	
	private List<DataUser> selectedUsers;
	
	public ShareStoreBB() {
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
		this.store = session.getStoreSelected();
		this.loggedUser = session.getLoggedUser();
		try {
			this.users = Comunicacion.getInstance().getIStoreController().getShareUsersFromStore(this.store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public List<DataUser> getUsers() {
		return users;
	}

	public void setUsers(List<DataUser> users) {
		this.users = users;
	}

	public List<DataUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<DataUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	
	public DataUser getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(DataUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	public String shareStore() {
		String result = "ErrorShareStore";
		if (this.selectedUsers == null || this.selectedUsers.isEmpty()) {
			
		} else {
			result = "OkShareStore";
			String message = this.loggedUser.getName() + " ha agregado a ";
			for (DataUser u: this.selectedUsers) {
				message = message + u.getName();
				if (this.selectedUsers.indexOf(u) == (this.selectedUsers.size()-1)) {
					message = message + " ";
				} else {
					message = message + ", ";
				}
			}
			message = message + "al almacen";
			try {
				Comunicacion.getInstance().getIStoreController().shareStore(this.store.getId(), this.selectedUsers);
				Comunicacion.getInstance().getINotificationController().sendStoreUserNotification(message, this.store.getId(), this.loggedUser.getId());
			} catch (NamingException e) {
				e.printStackTrace();
			}
			EventBus eventBus = EventBusFactory.getDefault().eventBus();
	        eventBus.publish("/notify/store/" + this.store.getId(), new FacesMessage("SAPo", message));
		}
		return result;
	}
}
