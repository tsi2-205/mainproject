package fabrica;

import interfaces.IUserController;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named
public class HomeFactoryBean implements HomeFactory {
	
	@Resource
	public SessionContext ctx;
	
	@Override
	public IUserController getUserController() {
		return (IUserController) ctx.lookup("java:module/UserController!interfaces.IUserController");
	}

}
