package tifauv.jplop.core.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This is a list of users.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class UserBase {
	
	// CONSTANTS \\
	/** The name of the default list file on disk. */
	public static final String FILE_NAME = "users.xml";
	
	/** The root tag name. */
	public static final String USERS_TAG = "jplop-users";
	
	/** The role tag name. */
	public static final String ROLE_TAG = "role";
	
	/** The role name attribute. */
	public static final String ROLE_NAME_ATTR = "rolename";
	
	/** The user tag name*/
	public static final String USER_TAG = "user";
	
	/** The user name attribute. */
	public static final String USER_NAME_ATTR = "username";
	
	/** The nickname attribute. */
	public static final String USER_NICK_ATTR = "nick";
	
	/** The user password attribute. */
	public static final String USER_PSW_ATTR = "password";
	
	/** The user email attribute. */
	public static final String USER_EMAIL_ATTR = "email";
	
	/** The user roles attribute. */
	public static final String USER_ROLES_ATTR = "roles";
	
	
	// FIELDS \\
	/** The roles that are defined. */
	private Collection<String> m_roles;
	
	/** The users. */
	private Map<String, User> m_users;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(UserBase.class);

	
	// CONSTRUCTOR \\
	/**
	 * Default constructor.
	 */
	public UserBase() {
		m_roles = new ArrayList<String>();
		m_users = new HashMap<String, User>();
	}
	
	
	// GETTERS \\
	/**
	 * Gives the file where the users list is saved.
	 */
	public String getFilename() {
		return FILE_NAME;
	}
	
	
	/**
	 * Gives the number of users.
	 */
	public int size() {
		return m_users.size();
	}
	
	
	/**
	 * Return the default roles.
	 * TODO implement the roles system
	 */
	public String getDefaultRoles() {
		return "";
	}
	
	
	public Collection<String> getRoles() {
		return Collections.unmodifiableCollection(m_roles);
	}
	
	
	public Collection<User> getUsers() {
		return Collections.unmodifiableCollection(m_users.values());
	}
	
	
	// METHODS \\
	/**
	 * Removes all the defined roles.
	 */
	public synchronized void clearRoles() {
		m_roles.clear();
	}
	
	
	/**
	 * Adds a new role.
	 * 
	 * @param p_role
	 *            the role to add
	 */
	public synchronized void addRole(String p_role) {
		m_roles.add(p_role);
		m_logger.debug("Role [" + p_role + "] added.");
	}
	
	
	/**
	 * Removes all the users.
	 */
	public synchronized void clearUsers() {
		m_users.clear();
	}
	
	
	/**
	 * Adds a new user.
	 * 
	 * @param p_user
	 *            the user to add
	 */
	public synchronized void addUser(User p_user) {
		if (p_user == null) {
			m_logger.warn("Tried to add a null user.");
			return;
		}
		
		if (!m_users.containsKey(p_user.getLogin())) {
			m_users.put(p_user.getLogin(), p_user);
			m_logger.debug("User [" + p_user.getLogin() + "] added.");
		}
		else
			m_logger.warn("The user [" + p_user.getLogin() + "] already exists in the user base.");
	}
	
	
	/**
	 * Tells whether the base contains the given user.
	 * 
	 * @param p_userName
	 *            the login of the user
	 * 
	 * @return <code>true</code> iff the base contains a user which has the given username
	 */
	public boolean containsUser(String p_userName) {
		return m_users.containsKey(p_userName);
	}
	
	
	/**
	 * Gives the User with the given username.
	 * 
	 * @return a {@link User} or <code>null</code> 
	 */
	public User get(String p_username) {
		return m_users.get(p_username);
	}
}