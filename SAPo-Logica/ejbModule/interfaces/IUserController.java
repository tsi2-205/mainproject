package interfaces;

import javax.ejb.Local;

@Local
public interface IUserController {
	
	public boolean isUserLogged (String email, String password);
	
	public boolean isFbUserLogged (String fbId);
	
	public boolean isRegisteredUser (String email);
	
	public boolean isRegisteredFbUser (String fbId);
	
	public void registerUser(String email, String password, String name);
	
	public void registerFbUser(String fbId, String name);
	
}
