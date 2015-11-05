package response;

import entities.User;

public class LoginResponse {

	int respCode;
	String message;
	User user;
	
	public LoginResponse(int respCode, String message, User user) {
		super();
		this.respCode = respCode;
		this.message = message;
		this.user = user;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
