package rest;

import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import comunication.Comunicacion;
import datatypes.DataUser;
import datatypes.DataStore;
import response.LoginResponse;
import request.LoginRequest;
import response.GetStoresResponse;

@Path("/user")
public class UserRestService {

	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces("application/json")
	public LoginResponse login(LoginRequest request) throws NamingException {
		
		if (request.getFbId() != null) {
			boolean isLoggedUser = Comunicacion.getInstance().getIUserController().isFbUserLogged(request.getFbId());
			if (isLoggedUser) {
				DataUser u = Comunicacion.getInstance().getIUserController().getFbUserData(request.getFbId());
				LoginResponse response = new LoginResponse(0,"",u);
				return response;
			} else {
				LoginResponse response = new LoginResponse(1,"Usuario y/o contraseña incorrectos",null);
				return response;
			}
		} else {
			boolean isLoggedUser = Comunicacion.getInstance().getIUserController().isUserLogged(request.getEmail(), request.getPassword());
			if (isLoggedUser) {
				DataUser u = Comunicacion.getInstance().getIUserController().getUserData(request.getEmail());
				LoginResponse response = new LoginResponse(0,"",u);
				return response;
			} else {
				LoginResponse response = new LoginResponse(1,"Usuario y/o contraseña incorrectos",null);
				return response;
			}
		}
		
	}
	
	@GET
	@Path("/{userId}/stores")
	@Produces("application/json")
	public GetStoresResponse getStores(@PathParam("userId") int userId) throws NamingException {
		List<DataStore> myStores = Comunicacion.getInstance().getIUserController().getStoresOwner(userId);
		List<DataStore> sharedStores = Comunicacion.getInstance().getIUserController().getStoresGuest(userId);
		GetStoresResponse response = new GetStoresResponse(0,"",myStores,sharedStores);
		return response;
	}
}
