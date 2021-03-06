package controllers;

import interfaces.IUserController;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import datatypes.DataStore;
import datatypes.DataUser;
import datatypes.DataUserLogged;
import entities.Administrator;
import entities.Registered;
import entities.Store;
import entities.User;
import entities.UserLogCount;

@Stateless
public class UserController implements IUserController {

	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;

	public boolean isUserLogged(String email, String password) {
		String queryStr = " SELECT r FROM User r"
				+ " WHERE r.email = :email AND r.password = :password";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("email", email);
		query.setParameter("password", password);
		return (!query.getResultList().isEmpty() && query.getResultList()
				.size() > 0);
	}

	public boolean isFbUserLogged(String fbId) {
		String queryStr = " SELECT r FROM Registered r"
				+ " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		return (!query.getResultList().isEmpty() && query.getResultList()
				.size() > 0);
	}

	public boolean isRegisteredUser(String email) {
		String queryStr = " SELECT r FROM Registered r"
				+ " WHERE r.email = :email";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("email", email);
		return (!query.getResultList().isEmpty() && query.getResultList()
				.size() > 0);
	}

	public boolean isRegisteredFbUser(String fbId) {
		String queryStr = " SELECT r FROM Registered r"
				+ " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		return (!query.getResultList().isEmpty() && query.getResultList()
				.size() > 0);
	}

	public void registerUser(String email, String password, String name,
			String acc) {
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
		User r = (User) query.getSingleResult();
		int tipo = (r instanceof Administrator) ? 0 : 1;
		if (r instanceof Administrator) {
			tipo = 0;
		}
		return new DataUser(r.getId(), r.getEmail(), r.getPassword(),
				r.getFbId(), r.getName(), r.getAccount(), tipo);

	}

	public DataUser getFbUserData(String fbId) {
		String queryStr = " SELECT r FROM Registered r"
				+ " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		Registered r = (Registered) query.getSingleResult();
		return new DataUser(r.getId(), r.getEmail(), r.getPassword(),
				r.getFbId(), r.getName(), r.getAccount(), 1);
	}

	public List<DataStore> getStoresGuest(int Id) {
		List<DataStore> ret = new LinkedList<DataStore>();
		DataStore dataS;
		String queryStr = " SELECT u FROM Registered u" + " WHERE u.id = :id";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("id", Id);
		Registered u = (Registered) query.getSingleResult();
		List<Store> stores = u.getStoresGuest();
		for (Store str : stores) {
			dataS = new DataStore(str);
			ret.add(dataS);
		}
		return ret;
	}

	public List<DataStore> getStoresOwner(int Id) {
		List<DataStore> ret = new LinkedList<DataStore>();
		DataStore dataS;
		String queryStr = "SELECT r FROM Registered r WHERE r.id = :id";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("id", Id);
		Registered u = (Registered) query.getSingleResult();
		List<Store> stores = u.getStoreOwner();
		for (Store str : stores) {
			dataS = new DataStore(str);
			ret.add(dataS);
		}
		return ret;
	}

	public void setAccount(int Id) {
		String queryStr = " SELECT u FROM Registered u" + " WHERE u.id = :id";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("id", Id);
		Registered u = (Registered) query.getSingleResult();
		u.setAccount("P");
		em.persist(u);
	}

	public boolean createMoreStores(int id) {
		String queryStr = "SELECT s FROM Store s WHERE s.owner.id = :id";
		Query query = em.createQuery(queryStr, Store.class);
		query.setParameter("id", id);
		String queryStr1 = " SELECT u FROM Registered u" + " WHERE u.id = :id";
		Query query1 = em.createQuery(queryStr1, Registered.class);
		query1.setParameter("id", id);
		Registered u = (Registered) query1.getSingleResult();
		String account = u.getAccount();
		boolean ret = false;
		if (account.equals("P")) {
			ret = true;
		} else {
			if (!query.getResultList().isEmpty()) {
				ret = false;
			} else {
				ret = true;
			}
		}
		return ret;

	}

	public List<DataUser> getAdministrators() {
		List<DataUser> result = new LinkedList<DataUser>();
		String queryStr = " SELECT a FROM Administrator a";
		Query query = em.createQuery(queryStr, Administrator.class);
		for (Object o : query.getResultList()) {
			Administrator admin = (Administrator) o;
			result.add(new DataUser(admin.getId(), admin.getEmail(), admin
					.getPassword(), admin.getFbId(), admin.getName(), admin
					.getAccount(), 0));
		}
		return result;
	}
	
	public void addLoggedUser(Date fecha){
		String queryStr = "SELECT u FROM UserLogCount u WHERE u.fecha = :fecha";
		Query query = em.createQuery(queryStr, UserLogCount.class);
		query.setParameter("fecha", fecha);
		if (query.getResultList().isEmpty()){
			UserLogCount u = new UserLogCount(fecha);
			em.persist(u);
		}
		else{
			UserLogCount u = (UserLogCount)query.getSingleResult();
			u.setCount(u.getCount()+1);
			em.persist(u);
		}
	}
	
	public List<DataUserLogged> getLoggedUser(){
		List<DataUserLogged> result = new LinkedList<DataUserLogged>();
		String queryStr = "SELECT u FROM UserLogCount u";
		Query query = em.createQuery(queryStr, UserLogCount.class);
		for (Object o : query.getResultList()) {
			UserLogCount user = (UserLogCount) o;
			result.add(new DataUserLogged(user.getFecha(),user.getCount()));
		}
		return result;
	}
}
