package datatypes;

public class DataUser {

	private int id;
	
    private String email;
	
    private String password;
    
    private String fbId;
	
    private String name;
    
    private int version;
    
    public DataUser() {
		super();
	}
    
    public DataUser(int id, String email, String password, String fbId, String name, int version) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.fbId = fbId;
		this.name = name;
		this.version = version;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
