package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import entities.User;
import response.LoginResponse;

@Path("/user")
public class LoginRestService {
	
	@GET
	@Path("/login")
	@Produces("application/json")
	public LoginResponse login() {
		User u = new User(1, "Martin Alayon", "malayonoyarbide@gmail.com");
		LoginResponse response = new LoginResponse(0,"",u);
		return response;
	}

}
