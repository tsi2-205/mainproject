package comunication;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import interfaces.IStoreController;
import interfaces.IUserController;
import interfaces.IMongoController;

public class Comunicacion {
	
	private static Comunicacion inst = null;
	private Context ctx;
	
	public Comunicacion() throws NamingException {
		super();
		this.ctx = new InitialContext();
	}
	
	public static Comunicacion getInstance() throws NamingException {
		if (Comunicacion.inst == null) {
			Comunicacion.inst = new Comunicacion();
		}
		return Comunicacion.inst;
	}
		
	public IUserController getIUserController() throws NamingException {
		return (IUserController)this.ctx.lookup("java:app/SAPo-Logica/UserController!interfaces.IUserController");
	}
	
	public IStoreController getIStoreController() throws NamingException {
		return (IStoreController)this.ctx.lookup("java:app/SAPo-Logica/StoreController!interfaces.IStoreController");
	}
	
	public IMongoController getIMongoController() throws NamingException {
		return (IMongoController)this.ctx.lookup("java:app/SAPo-Logica/MongoController!interfaces.IMongoController");
	}

}
