package controladores;

import interfaces.IUserController;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataStore;
import datatypes.DataUser;
import entities.Administrator;
import entities.Registered;
import entities.Store;
import entities.User;

@Stateless
@WebService
public class UserController implements IUserController {

	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public boolean isUserLogged (String email, String password) {
		String queryStr = " SELECT r FROM User r" + " WHERE r.email = :email AND r.password = :password";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("email", email);
		query.setParameter("password", password);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public boolean isFbUserLogged (String fbId) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public boolean isRegisteredUser (String email) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.email = :email";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("email", email);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public boolean isRegisteredFbUser (String fbId) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public void registerUser(String email, String password, String name, String acc) {
		Registered r = new Registered(email, password, null, name, acc);
		em.persist(r);
	}
	
	public void registerFbUser(String fbId, String name) {
		Registered r = new Registered(null, null, fbId, name, "F");
		em.persist(r);
	}
	
	public DataUser getUserData(String email) {
		String queryStr = " SELECT r FROM User r" + " WHERE r.email = :email";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("email", email);
		User r = (User)query.getSingleResult();
		int tipo = (r instanceof Administrator) ? 0 : 1;
		if (r instanceof Administrator) {
			tipo = 0;
		}
		return new DataUser(r.getId(), r.getEmail(), r.getPassword(), r.getFbId(), r.getName(), r.getAccount(), tipo);
		
	}
	
	public DataUser getFbUserData(String fbId) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		Registered r = (Registered)query.getSingleResult();
		return new DataUser(r.getId(), r.getEmail(), r.getPassword(), r.getFbId(), r.getName(), r.getAccount(), 1);
	}
	
	public List<DataStore> getStoresGuest(int Id){
		List<DataStore> ret=new LinkedList<DataStore>();
		DataStore dataS;
		String queryStr = " SELECT u FROM Registered u" + " WHERE u.id = :id";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("id", Id);
		Registered u = (Registered)query.getSingleResult();
		List<Store> stores=u.getStoresGuest();
		for(Store str: stores) {
			dataS= new DataStore(str);
			ret.add(dataS);
		}
		return ret;
	}
	
	public List<DataStore> getStoresOwner(int Id){
		List<DataStore> ret=new LinkedList<DataStore>();
		DataStore dataS;
		String queryStr = "SELECT r FROM Registered r WHERE r.id = :id";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("id", Id);
		Registered u = (Registered)query.getSingleResult();
		List<Store> stores=u.getStoreOwner();
		for(Store str: stores) {
			dataS= new DataStore(str);
			ret.add(dataS);
		}
		return ret;
	}
	
	public void setAccount(int Id){
		String queryStr = " SELECT u FROM Registered u" + " WHERE u.id = :id";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("id", Id);
		Registered u = (Registered)query.getSingleResult();
		u.setAccount("P");
		em.persist(u);
	}
	
}
