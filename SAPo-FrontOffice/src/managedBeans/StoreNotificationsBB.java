package managedBeans;

import java.util.LinkedList;
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
import datatypes.DataNotification;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class StoreNotificationsBB {

	private DataUser user;
	
	private DataStore store;
	
	private List<DataNotification> notifications = new LinkedList<DataNotification>();
	
	private DataNotification notificationSelected = null;
	
	public StoreNotificationsBB() {
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
		this.user = session.getLoggedUser();
		try {
			this.notifications = Comunicacion.getInstance().getINotificationController().getStoreUserNotifications(this.user.getId(), this.store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
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
	
	public List<DataNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<DataNotification> notifications) {
		this.notifications = notifications;
	}

	public DataNotification getNotificationSelected() {
		return notificationSelected;
	}

	public void setNotificationSelected(DataNotification notificationSelected) {
		this.notificationSelected = notificationSelected;
	}
}
