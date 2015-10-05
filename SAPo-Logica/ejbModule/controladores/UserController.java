package controladores;

import interfaces.IUserController;

import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DatosUsuario;
import entities.Registered;
import entities.User;

@Stateless
@WebService
public class UserController implements IUserController {

	@PersistenceContext(unitName = "SAPo-Logica")
	private EntityManager em;

	public DatosUsuario login(String user, String pas) {
		String consulta = " SELECT u FROM Usuario u"
				+ " WHERE u.nick = :user AND u.password = :pass";
		Query query = em.createQuery(consulta, User.class);
		query.setParameter("user", user);
		query.setParameter("pass", pas);
		List<User> result = query.getResultList();
		DatosUsuario datosUsuario = null;
		for (User usr : result) {
			datosUsuario = new DatosUsuario(usr.getId(), usr.getNick(),
					usr.getPassword(), usr.getMail(), usr.getName(), usr.getVersion());
		}

		return datosUsuario;
	}
	
	public void registerUser(String nick, String pas, String mail,
			String nombre, String calle, int numPuerta) {
		User usr = new Registered(nick, pas, mail, nombre);
		em.persist(usr);
		
	}
	
}
