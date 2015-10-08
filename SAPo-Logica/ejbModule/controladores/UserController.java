package controladores;

import interfaces.IUserController;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Registered;
import entities.User;

@Stateless
@WebService
public class UserController implements IUserController {

	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;
	
	public boolean isUserLogged (String email, String password) {
		String queryStr = " SELECT u FROM User u" + " WHERE u.email = :email AND u.password = :password";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("email", email);
		query.setParameter("password", password);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public boolean isFbUserLogged (String fbId) {
		String queryStr = " SELECT u FROM User u" + " WHERE u.fbId = :fbId";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("fbId", fbId);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public boolean isRegisteredUser (String email) {
		String queryStr = " SELECT u FROM User u" + " WHERE u.email = :email";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("email", email);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public boolean isRegisteredFbUser (String fbId) {
		String queryStr = " SELECT u FROM User u" + " WHERE u.fbId = :fbId";
		Query query = em.createQuery(queryStr, User.class);
		query.setParameter("fbId", fbId);
		return (!query.getResultList().isEmpty() && query.getResultList().size() > 0);
	}
	
	public void registerUser(String email, String password, String name) {
		User u = new Registered(email, password, null, name);
		em.persist(u);
	}
	
	public void registerFbUser(String fbId, String name) {
		User u = new Registered(null, null, fbId, name);
		em.persist(u);
	}
	
}
