/**
 * 04 dec. 2007
 */
package tifauv.jplop.auth;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The description of a user.
 * A user has a login, email, password and roles.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class User {

	// CONSTANTS \\
	/** The roles separator. */
	protected static final String ROLES_SEPARATOR = ",";
	
	
	// FIELDS \\
	/** The user's login. */
	private String m_login;
	
	/** The user's email address. */
	private String m_email;
	
	/** The user's password. */
	private Password m_password;
	
	/** The user's roles. */
	private SortedSet<String> m_roles;

	
	// CONSTRUCTORS \\
	/**
	 * Initializes the user.
	 */
	public User() {
		m_roles = new TreeSet<String>();
		m_password = new SSHAPassword();
	}
	
	
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
		if (m_email == null)
			m_email = "";
		return m_email;
	}

	
	/**
	 * Gives the user's password.
	 */
	public String getPassword() {
		return m_password.getPassword();
	}
	

	/**
	 * Gives the user's roles (comma-separated list).
	 * The roles follow the natural order of strings.
	 */
	public String getRoles() {
		StringBuffer buffer = new StringBuffer();
		for (String role : m_roles) {
			if (buffer.length() > 0)
				buffer.append(ROLES_SEPARATOR);
			buffer.append(role);
		}
		return buffer.toString();
	}
	

	// SETTERS \\
	/**
	 * @param p_login
	 *            the user's login
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}

	
	/**
	 * @param p_email
	 *            the user's email address
	 */
	public void setEmail(String p_email) {
		m_email = p_email;
	}
	
	
	/**
	 * Decodes the password from a Base64 form (with or without SSHA prefix)
	 * and sets it.
	 */
	public void setPassword(String p_password)
	throws PasswordException {
		m_password.setPassword(p_password);
	}

	
	// METHODS
	/**
	 * Checks the given password matches the user's one.
	 */
	public boolean checkPassword(String p_otherPassword) {
		try {
			return m_password.check(p_otherPassword);
		} catch (PasswordException e) {
			// XXX We should log the error here, but isn't building a logger
			// just for that exception a bit overkill ?
			return false;
		}
	}
	
	
	/**
	 * Sets the roles.
	 *
	 * @param p_roles
	 *            a comma-separated list of roles
	 */
	public void setRoles(String p_roles) {
		clearRoles();
		addRoles(p_roles);
	}
	

	/**
	 * Add the roles defined in the given comma-separated list.
	 *
	 * @param p_roles
	 *            a comma-separated list of roles
	 */
	public void addRoles(String p_roles) {
		if (p_roles != null && p_roles.length() > 0) {
			String[] roles = p_roles.split(ROLES_SEPARATOR);
			for (String role : roles)
				addRole(role.trim());
		}
	}
	

	/**
	 * Removes all the roles of this user.
	 */
	public void clearRoles() {
		m_roles.clear();
	}
	

	/**
	 * Adds a role to this user.
	 * Does nothing if the given role is <code>null</code>, the empty string,
	 * or contains the {@link #ROLES_SEPARATOR}. Note the role will be trimmed to remove
	 * unnecessary spaces before and after the role string.
	 *
	 * @param p_role
	 *            the role to add
	 */
	public void addRole(String p_role) {
		if (p_role != null) {
			String role = p_role.trim();
			if (role.length() > 0 && role.indexOf(ROLES_SEPARATOR) < 0)
				m_roles.add(role);
		}
	}
}
