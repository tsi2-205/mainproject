package rest;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import comunication.Comunicacion;
import datatypes.DataUser;
import response.LoginResponse;
import request.LoginRequest;

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
	
}
