package datatypes;

import java.io.Serializable;

import entities.Registered;

public class DataUser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
    private String email;
	
    private String password;
    
    private String fbId;
	
    private String name;
    
    private String account;
    
    private int version;
    
    public DataUser() {
		super();
	}
    
    public DataUser(int id, String email, String password, String fbId, String name, String acc, int version) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.fbId = fbId;
		this.name = name;
		this.account=acc;
		this.version = version;
	}
    
    public DataUser(Registered r) {
    	super();
    	this.id = r.getId();
    	this.email = r.getEmail();
    	this.password = r.getPassword();
    	this.fbId = r.getFbId();
    	this.name = r.getName();
    	this.account = r.getName();
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
