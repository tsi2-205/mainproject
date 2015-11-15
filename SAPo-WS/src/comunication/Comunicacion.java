package comunication;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import interfaces.IBuyListController;
import interfaces.ICategoryController;
import interfaces.INotificationController;
import interfaces.IProductController;
import interfaces.IStoreController;
import interfaces.IUserController;

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
	
	public INotificationController getINotificationController() throws NamingException {
		return (INotificationController)this.ctx.lookup("java:app/SAPo-Logica/NotificationController!interfaces.INotificationController");
	}
	
	public IProductController getIProductController() throws NamingException {
		return (IProductController)this.ctx.lookup("java:app/SAPo-Logica/ProductController!interfaces.IProductController");
	}
	
	public ICategoryController getICategoryController() throws NamingException {
		return (ICategoryController)this.ctx.lookup("java:app/SAPo-Logica/CategoryController!interfaces.ICategoryController");
	}
	
	public IBuyListController getIBuyListController() throws NamingException {
		return (IBuyListController)this.ctx.lookup("java:app/SAPo-Logica/BuyListController!interfaces.IBuyListController");
	}

}
