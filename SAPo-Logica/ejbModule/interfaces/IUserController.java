package interfaces;

import javax.ejb.Local;

import datatypes.DatosUsuario;

@Local
public interface IUserController {
	
	public DatosUsuario login(String user, String pas);
	
	public void registerUser(String nick, String pas, String mail, String nombre, String calle, int numPuerta);
	
}
