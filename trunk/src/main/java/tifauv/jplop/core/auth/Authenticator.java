package tifauv.jplop.core.auth;

import org.apache.log4j.Logger;

import tifauv.jplop.core.Main;

public final class Authenticator {

	// FIELDS \\
	/** The user name. */
	private String m_username;
	
	/** The password. */
	private String m_password;
	
	/** The user found. */
	private User m_user;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(Authenticator.class);
	
	
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
		if (getUsername() == null) {
			m_logger.error("Authentication failed because no username has been given.");
			return false;
		}
		
		m_logger.debug("Authentication request for user [" + getUsername() + "].");
		User user = Main.get().getUserBase().get(getUsername());
		
		// If the user doesn't exist, exit
		if (user == null) {
			m_logger.warn("Authentication failed: user [" + getUsername() + "] does not exist.");
			return false;
		}

		// Else check the user's password
		if (!user.checkPassword(getPassword())) {
			m_logger.warn("Authentication failed: user [" + getUsername() + "] gave a bad password.");
			return false;
		}
		m_logger.info("Authentication of user [" + getUsername() + "] succeeded.");
		
		setUser(user);
		return true;
	}
}