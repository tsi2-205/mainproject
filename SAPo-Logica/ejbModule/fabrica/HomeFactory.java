package fabrica;

import interfaces.IUserController;

import javax.ejb.Local;


@Local
public interface HomeFactory {
	
	public IUserController getUserController();
	
}
