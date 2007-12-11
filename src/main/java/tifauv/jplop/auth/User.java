/**
 * 04 dec. 2007
 */
package tifauv.jplop.auth;

/**
 * The description of a user.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class User {

	// FIELDS \\
	/** The user's login. */
	private String m_login;
	
	/** The user's email address. */
	private String m_email;
	
	/** The user's password. */
	private byte[] m_password;

	
	// GETTERS \\
	/**
	 * Gives the user's login.
	 */
	public String getLogin() {
		return m_login;
	}

	
	/**
	 * Gives the user's email address.
	 */
	public String getEmail() {
		return m_email;
	}

	
	/**
	 * Gives the user's password.
	 */
	public byte[] getPassword() {
		return m_password;
	}

	
	// SETTERS \\
	/**
	 * @param m_login
	 *            the user's login
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}

	
	/**
	 * @param m_email
	 *            the user's email address
	 */
	public void setEmail(String p_email) {
		m_email = p_email;
	}

	
	/**
	 * @param m_password
	 *            the user's password
	 */
	public void setPassword(byte[] p_password) {
		m_password = p_password;
	}
}
