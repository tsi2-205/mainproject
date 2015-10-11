package fabrica;

import interfaces.IMongoController;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named
public class MongoFactoryBean implements MongoFactory {
	
	@Resource
	public SessionContext ctx;
	
	@Override
	public IMongoController getMongoController() {
		return (IMongoController) ctx.lookup("java:module/MongoController!interfaces.IMongoController");
	}

}
