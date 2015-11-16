package notifications;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
@RequestScoped
public class NotifyUserView {

	private final static String CHANNEL = "/notify/user/";
	
	public void sendNotification(int userId, String message) {
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(CHANNEL + userId, new FacesMessage("SAPo", message));
    }
}
