package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataNotification;

@Local
public interface INotificationController {
	
	public List<DataNotification> getReadNotifications(int userId, int storeId);
	
	public List<DataNotification> getUnreadNotifications(int userId, int storeId);
	
	public void readNotification(int notificationId);
	
	public void sendNotification(String message, int storeId, int senderId);

}
