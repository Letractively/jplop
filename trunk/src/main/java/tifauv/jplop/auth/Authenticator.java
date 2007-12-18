package tifauv.jplop.auth;

import tifauv.jplop.Backend;

public class Authenticator {

	// FIELDS \\
	/** The user name. */
	private String m_username;
	
	/** The password. */
	private String m_password;
	
	private User m_user;
	
	
	// CONSTRUCTORS \\
	public Authenticator(String p_username, String p_password) {
		setUsername(p_username);
		setPassword(p_password);
	}
	
	
	// GETTERS \\
	public String getUsername() {
		return m_username;
	}
	
	
	private String getPassword() {
		return m_password;
	}
	
	
	public User getUser() {
		return m_user;
	}
	
	
	// SETTERS \\
	private void setUsername(String p_username) {
		m_username = p_username;
	}
	
	
	private void setPassword(String p_password) {
		m_password = p_password;
	}
	
	
	private void setUser(User p_user) {
		m_user = p_user;
	}
	
	
	// METHODS \\
	public boolean authenticate() {
		User user = Backend.getInstance().getUserBase().get(getUsername());
		
		// If the user doesn't exist, exit
		if (user == null)
			return false;

		// Else check the user's password
		if (!user.checkPassword(getPassword()))
			return false;
		
		setUser(user);
		return true;
	}
}
