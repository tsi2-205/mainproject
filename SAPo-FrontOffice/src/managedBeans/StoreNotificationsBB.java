package managedBeans;

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

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import comunication.Comunicacion;
import datatypes.DataNotification;

@ManagedBean
@ViewScoped
public class StoreNotificationsBB {

	private int userId;
	
	private int storeId;
	
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
		this.storeId = session.getStoreSelected().getId();
		this.userId = session.getLoggedUser().getId();
		try {
			this.notifications = Comunicacion.getInstance().getINotificationController().getStoreUserNotifications(this.userId, this.storeId);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
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
	
	public void refreshNotifications() {
		try {
			this.notifications = Comunicacion.getInstance().getINotificationController().getStoreUserNotifications(this.userId, this.storeId);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void send() {
		String message = "Store Notification";
		try {
			Comunicacion.getInstance().getINotificationController().sendStoreUserNotification(message, this.storeId, this.userId);
		} catch (NamingException e) {
			e.printStackTrace();
		}
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/notify/store/" + this.storeId, new FacesMessage("SAPo", message));
    }
}
