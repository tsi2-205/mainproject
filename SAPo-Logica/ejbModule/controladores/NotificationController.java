package controladores;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataNotification;
import entities.Notification;
import entities.Registered;
import entities.Store;
import interfaces.INotificationController;

@Stateless
@WebService
public class NotificationController implements INotificationController {
	
	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public List<DataNotification> getReadNotifications(int userId, int storeId) {
		List<DataNotification> result = new LinkedList<DataNotification>();
		String queryStr = " SELECT n FROM Notification n" + " WHERE n.user.id = :IdUser AND n.store.id = :IdStore AND n.isRead = true";
		Query query = em.createQuery(queryStr, Notification.class);
		query.setParameter("IdUser", userId);
		query.setParameter("IdStore", storeId);
		for (Object o: query.getResultList()) {
			Notification n = (Notification)o;
			DataNotification dn = new DataNotification(n.getMessage(), n.isRead(), null, null, n.getDate());
			result.add(dn);
		}
		return result;
	}
	
	public List<DataNotification> getUnreadNotifications(int userId, int storeId) {
		List<DataNotification> result = new LinkedList<DataNotification>();
		String queryStr = " SELECT n FROM Notification n" + " WHERE n.user.id = :IdUser AND n.store.id = :IdStore AND n.isRead = false";
		Query query = em.createQuery(queryStr, Notification.class);
		query.setParameter("IdUser", userId);
		query.setParameter("IdStore", storeId);
		for (Object o: query.getResultList()) {
			Notification n = (Notification)o;
			DataNotification dn = new DataNotification(n.getMessage(), n.isRead(), null, null, n.getDate());
			result.add(dn);
		}
		return result;
	}
	
	public void readNotification(int notificationId) {
		Notification n = em.find(Notification.class, notificationId);
		if (n == null) {
//			Excepcion
		}
		n.setRead(true);
		em.persist(n);
	}
	
	public void sendNotification(String message, int storeId, int senderId) {
		Store s = em.find(Store.class, storeId);
		Registered owner = s.getOwner();
		List<Registered> users = s.getGuests();
		for (Registered r: users) {
			if (r.getId() != senderId) {
				Notification not = new Notification(message,false,r,s,new GregorianCalendar());
				em.persist(not);
			}
		}
		Notification notOwner = new Notification(message,false,owner,s,new GregorianCalendar());
		em.persist(notOwner);
	}
}
