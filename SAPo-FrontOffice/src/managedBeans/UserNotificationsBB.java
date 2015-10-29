package managedBeans;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import datatypes.DataNotification;
import datatypes.DataUser;

public class UserNotificationsBB {

	private DataUser user;
	
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
		this.user = session.getLoggedUser();
	}

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
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
