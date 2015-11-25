package interfaces;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Local;

import datatypes.DataStore;
import datatypes.DataUser;

@Local
public interface IUserController {
	
	public boolean isUserLogged (String email, String password);
	
	public boolean isFbUserLogged (String fbId);
	
	public boolean isRegisteredUser (String email);
	
	public boolean isRegisteredFbUser (String fbId);
	
	public void registerUser(String email, String password, String name, String acc);
	
	public void registerFbUser(String fbId, String name);
	
	public DataUser getUserData(String email);
	
	public DataUser getFbUserData(String fbId);
	
	public List<DataStore> getStoresGuest(int Id);
	
	public List<DataStore> getStoresOwner(int id);
	
	public void setAccount(int Id);
	
	public boolean createMoreStores(int id);
	
	public List<DataUser> getAdministrators();
	
	public void addLoggedUser(Calendar fecha);
	
}
