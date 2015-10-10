package controladores;

import interfaces.IUserController;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DataUser;
import entities.Registered;

@Stateless
@WebService
public class UserController implements IUserController {

	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public boolean isUserLogged (String email, String password) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.email = :email AND r.password = :password";
		Query query = em.createQuery(queryStr, Registered.class);
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
	
	public void registerUser(String email, String password, String name) {
		Registered r = new Registered(email, password, null, name);
		em.persist(r);
	}
	
	public void registerFbUser(String fbId, String name) {
		Registered r = new Registered(null, null, fbId, name);
		em.persist(r);
	}
	
	public DataUser getUserData(String email) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.email = :email";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("email", email);
		Registered r = (Registered)query.getSingleResult();
		return new DataUser(r.getId(), r.getEmail(), r.getPassword(), r.getFbId(), r.getName(), r.getVersion());
	}
	
	public DataUser getFbUserData(String fbId) {
		String queryStr = " SELECT r FROM Registered r" + " WHERE r.fbId = :fbId";
		Query query = em.createQuery(queryStr, Registered.class);
		query.setParameter("fbId", fbId);
		Registered r = (Registered)query.getSingleResult();
		return new DataUser(r.getId(), r.getEmail(), r.getPassword(), r.getFbId(), r.getName(), r.getVersion());
	}
	
}
