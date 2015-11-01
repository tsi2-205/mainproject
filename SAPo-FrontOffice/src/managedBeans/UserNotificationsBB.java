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

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import javax.faces.application.FacesMessage;

@ManagedBean
@ViewScoped
public class UserNotificationsBB {

	private int userId;
	
	private List<DataNotification> notifications = new LinkedList<DataNotification>();
	
	private DataNotification notificationSelected = null;
	
	public UserNotificationsBB() {
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
		this.userId = session.getLoggedUser().getId();
		try {
			this.notifications = Comunicacion.getInstance().getINotificationController().getUserNotifications(this.userId);
		} catch (NamingException e) {
			e.printStackTrace();
		}
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
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void send() {
		String message = "User Notification";
		try {
			Comunicacion.getInstance().getINotificationController().sendUserNotification(message, this.userId);
		} catch (NamingException e) {
			e.printStackTrace();
		}
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish("/notify/user/" + this.userId, new FacesMessage("SAPo", message));
    }
}
