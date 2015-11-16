package notifications;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
@RequestScoped
public class NotifyStoreView {

	private final static String CHANNEL = "/notify/store/";
	
	public void sendNotification(int storeId, String message) {
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(CHANNEL + storeId, new FacesMessage("SAPo", message));
    }
}
