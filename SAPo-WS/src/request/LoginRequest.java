package request;

public class LoginRequest {

	String email;
	String password;
	String fbId;
	
	public LoginRequest() {
		super();
	}
	
	public LoginRequest(String email, String password, String fbId) {
		super();
		this.email = email;
		this.password = password;
		this.fbId = fbId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
}
