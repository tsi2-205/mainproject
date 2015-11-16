package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DataNotification;

@Local
public interface INotificationController {
	
	public List<DataNotification> getStoreUserNotifications(int userId, int storeId);
	
	public List<DataNotification> getUserNotifications(int userId);
	
	public void sendStoreNotification(String message, int storeId, boolean sendOwner);
	
	public void sendUserNotification(String message, int userId);
	
	public void sendAdminNotification(String message);
	
	public void readNotification(int notificationId);

}
