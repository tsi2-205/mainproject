package fabrica;

import interfaces.IMongoController;

import javax.ejb.Local;


@Local
public interface MongoFactory {
	
	public IMongoController getMongoController();
	
}
