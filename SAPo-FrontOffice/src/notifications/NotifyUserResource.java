package notifications;

import javax.faces.application.FacesMessage;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;
 
@PushEndpoint("/notify/user/{user}")
public class NotifyUserResource {
    
	@PathParam("user")
    private int userId;
	
    @OnMessage(encoders = {JSONEncoder.class})
    public FacesMessage onMessage(FacesMessage message) {
        return message;
    }
 
}
