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
		m_username = p_username;
		m_password = p_password;
	}
	
	
	// GETTERS \\
	public User getUser() {
		return m_user;
	}
	
	
	// METHODS \\
	public boolean authenticate() {
		if (m_username == null) {
			m_logger.error("Authentication failed because no username has been given.");
			return false;
		}
		
		m_logger.debug("Authentication request for user [" + m_username + "].");
		User user = Main.get().getUserBase().get(m_username);
		
		// If the user doesn't exist, exit
		if (user == null) {
			m_logger.warn("Authentication failed: user [" + m_username + "] does not exist.");
			return false;
		}

		// Else check the user's password
		if (!user.checkPassword(m_password)) {
			m_logger.warn("Authentication failed: user [" + m_username + "] gave a bad password.");
			return false;
		}
		m_logger.info("Authentication of user [" + m_username + "] succeeded.");
		
		m_user = user;
		return true;
	}
}