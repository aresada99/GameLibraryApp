package User;

public abstract class User {
	public String username;
	public String password;
	
	public boolean login(String username,String password) {
		return(this.username.equals(username) && this.password.equals(password));
			
	};
	
	public void sign_up(String newUsername,String newPassword) {
		this.username = newUsername;
		this.password = newPassword;
		
	}
	
}
