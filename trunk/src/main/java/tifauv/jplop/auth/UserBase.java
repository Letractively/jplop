package tifauv.jplop.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tifauv.jplop.util.DeserializeException;
import tifauv.jplop.util.Serializable;
import tifauv.jplop.util.SerializeException;

/**
 * This is a list of users.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class UserBase implements Serializable {
	
	// CONSTANTS \\
	/** The name of the default list file on disk. */
	public static final String DEFAULT_FILE = "users.list";
	
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
	
	/** The user password attribute. */
	public static final String USER_PSW_ATTR = "password";
	
	/** The user email attribute. */
	public static final String USER_EMAIL_ATTR = "email";
	
	/** The user roles attribute. */
	public static final String USER_ROLES_ATTR = "roles";
	
	
	// FIELDS \\
	/** The file where the users list is saved. */
	private File m_file;
	
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
	public File getFile() {
		return m_file;
	}
	
	
	/**
	 * Gives the number of users.
	 */
	public int size() {
		return m_users.size();
	}
	
	
	// SETTERS
	/**
	 * Sets the file where the history is saved.
	 * 
	 * @param p_contextDir
	 *            the current context directory
	 * @param p_file
	 *            the path to the file from the configuration
	 */
	public void setFile(String p_contextDir, String p_file) {
		File file = new File(p_file);
		if (file.isAbsolute())
			m_file = file;
		else
			m_file = new File(p_contextDir + File.separator + "WEB-INF", p_file);
	}
	
	
	// METHODS \\
	public synchronized void clearRoles() {
		m_roles.clear();
	}
	
	
	public synchronized void addRole(String p_role) {
		m_roles.add(p_role);
		m_logger.debug("Role '" + p_role + "' added.");
	}
	
	
	public synchronized void clearUsers() {
		m_users.clear();
	}
	
	
	public synchronized void addUser(User p_user) {
		m_users.put(p_user.getLogin(), p_user);
		m_logger.debug("User '" + p_user.getLogin() + "' added.");
	}
	
	
	/**
	 * Gives the User with the given username.
	 * 
	 * @return a {@link User} or <code>null</code> 
	 */
	public User get(String p_username) {
		return null;
	}
	
	
	/**
	 * Loads a user base from a DOM Document.
	 * 
	 * @param p_users
	 *            the DOM document
	 */
	public final synchronized void load(Document p_users) {
		// Load the roles
		clearRoles();
		NodeList roles = p_users.getElementsByTagName(ROLE_TAG);
		for (int i=0; i<roles.getLength(); ++i) {
			Element role = (Element)roles.item(i);
			if (role.hasAttribute(ROLE_NAME_ATTR))
				addRole(role.getAttribute(ROLE_NAME_ATTR));
			else
				m_logger.warn("A <" + ROLE_TAG + "> element exists but has no '" + ROLE_NAME_ATTR + "' attribute.");
		}

		// Load the users
		clearUsers();
		NodeList users = p_users.getElementsByTagName(USER_TAG);
		for (int i=0; i<users.getLength(); ++i) {
			Element userEl = (Element)users.item(i);
			if (userEl.hasAttribute(USER_NAME_ATTR)) {
				User user = new User();
				user.setLogin(userEl.getAttribute(ROLE_NAME_ATTR));
				user.setPassword(userEl.getAttribute(USER_PSW_ATTR));
				user.setEmail(userEl.getAttribute(USER_EMAIL_ATTR));
				user.setRoles(userEl.getAttribute(USER_ROLES_ATTR));
				addUser(user);
			}
			else
				m_logger.warn("A <" + USER_TAG + "> element exists but has no '" + USER_NAME_ATTR + "' attribute.");
		}
	}
	
	
	/**
	 * Loads the backend from the cache file. 
	 */
	public void loadFromFile()
	throws DeserializeException {
		if (getFile().exists()) {
			m_logger.info("Loading the user base from '" + getFile() + "'...");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				load(builder.parse(getFile()));
				m_logger.info(size() + " users loaded from cache.");
			}
			catch (ParserConfigurationException e) {
				// Cannot happen
			}
			catch (Exception e) {
				throw new DeserializeException("Could not load the user base file", e);
			}
		}
		else
			m_logger.debug("The users base file does not exist.");
	}
	
	
	/**
	 * Saves the history to a file.
	 * Does nothing if the history is empty.
	 */
	public void saveToFile()
	throws SerializeException {
		
	}
}
