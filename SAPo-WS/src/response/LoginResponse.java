package response;

import datatypes.DataUser;

public class LoginResponse {

	int respCode;
	String message;
	DataUser user;
	
	public LoginResponse(int respCode, String message, DataUser user) {
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

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}
}
