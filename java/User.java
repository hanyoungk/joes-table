
public class User {

	private String username;
	private String email;
	private String password;
	
	private String token;
	
	User() {
		username = null;
		email = null;
		password = null;
		token = null;
	}
	
	User(String username, String password, String email) {
		this.username = username;
		this.email = email;
		this.password = password;
		token = null;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
