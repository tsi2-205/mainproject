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

import notifications.NotifyStoreView;
import notifications.NotifyUserView;

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
		ELContext contextoEL = context.getELContext();
		Application apli = context.getApplication();
		ExpressionFactory ef = apli.getExpressionFactory();
		ValueExpression ve = ef.createValueExpression(contextoEL,
				"#{sessionBB}", SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.store = session.getStoreSelected();
		this.loggedUser = session.getLoggedUser();
		try {
			this.users = Comunicacion.getInstance().getIStoreController()
					.getShareUsersFromStore(this.store.getId());
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

	public void shareStore() {
		if (this.selectedUsers == null || this.selectedUsers.isEmpty()) {

		} else {
			// Store notifications
			String storeMessage = this.loggedUser.getName() + " ha agregado a ";
			for (DataUser u : this.selectedUsers) {
				storeMessage = storeMessage + u.getName();
				if (this.selectedUsers.indexOf(u) == (this.selectedUsers.size() - 1)) {
					storeMessage = storeMessage + " ";
				} else {
					storeMessage = storeMessage + ", ";
				}
				this.users.remove(u);
			}
			storeMessage = storeMessage + "al almacen";
			try {
				Comunicacion.getInstance().getIStoreController()
						.shareStore(this.store.getId(), this.selectedUsers);
				Comunicacion
						.getInstance()
						.getINotificationController()
						.sendStoreNotification(storeMessage,
								this.store.getId(), false);
			} catch (NamingException e) {
				e.printStackTrace();
			}
			NotifyStoreView notifyStoreView = new NotifyStoreView();
			notifyStoreView.sendNotification(this.store.getId(), storeMessage);

			// User notifications
			String userMessage = this.loggedUser.getName()
					+ " te ha agregado al almacen " + this.store.getName();
			NotifyUserView notifyUserView = new NotifyUserView();
			for (DataUser u : this.selectedUsers) {
				try {
					Comunicacion.getInstance().getINotificationController()
							.sendUserNotification(userMessage, u.getId());
				} catch (NamingException e) {
					e.printStackTrace();
				}
				notifyUserView.sendNotification(u.getId(), userMessage);
			}
			this.selectedUsers = null;
		}
	}
}
