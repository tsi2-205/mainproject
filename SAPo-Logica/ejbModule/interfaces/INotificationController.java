package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataNotification;

@Local
public interface INotificationController {
	
	public List<DataNotification> getStoreUserNotifications(int userId, int storeId);
	
	public List<DataNotification> getUserNotifications(int userId);
	
	public void sendStoreUserNotification(String message, int storeId, int senderId);
	
	public void sendUserNotification(String message, int userId);
	
	public void readNotification(int notificationId);

}
