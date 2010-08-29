package tifauv.jplop.core.storage.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tifauv.jplop.core.auth.User;
import tifauv.jplop.core.auth.UserBase;
import tifauv.jplop.core.storage.StorageException;

/**
 * 
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class UserDelegate extends FileStorage<UserBase> {

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
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(UserDelegate.class); 
	
	
	// CONSTRUCTOR \\
	/**
	 * Attaches the given user base to this delegate.
	 */
	public UserDelegate(UserBase p_users) {
		super(p_users);
	}
	
	
	// GETTERS \\
	@Override
	public String getFileName() {
		return FILE_NAME;
	}

	
	// METHODS \\
	@Override
	public void load()
	throws StorageException {
		File file = getFile();

		if (file != null && file.exists()) {
			m_logger.info("Loading the user base from '" + file + "'...");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				load(builder.parse(file));
				m_logger.info(getObject().size() + " users loaded.");
			} catch (Exception e) {
				throw new StorageException("Could not load the user base file", e);
			}
		}
		else
			m_logger.debug("The users base file does not exist.");
	}
	
	
	/**
	 * Loads a user base from a DOM Document.
	 * 
	 * @param p_users
	 *            the DOM document
	 */
	private void load(Document p_users) {
		// Load the roles
		getObject().clearRoles();
		NodeList roles = p_users.getElementsByTagName(ROLE_TAG);
		for (int i=0; i<roles.getLength(); ++i) {
			Element role = (Element)roles.item(i);
			if (role.hasAttribute(ROLE_NAME_ATTR))
				getObject().addRole(role.getAttribute(ROLE_NAME_ATTR));
			else
				m_logger.warn("A <" + ROLE_TAG + "> element exists but has no '" + ROLE_NAME_ATTR + "' attribute.");
		}

		// Load the users
		getObject().clearUsers();
		NodeList users = p_users.getElementsByTagName(USER_TAG);
		for (int i=0; i<users.getLength(); ++i) {
			Element userEl = (Element)users.item(i);
			if (userEl.hasAttribute(USER_NAME_ATTR)) {
				try {
					User user = new User();
					user.setLogin(userEl.getAttribute(USER_NAME_ATTR));
					if (userEl.hasAttribute(USER_NICK_ATTR))
						user.setNick(userEl.getAttribute(USER_NICK_ATTR));
					user.setPassword(userEl.getAttribute(USER_PSW_ATTR));
					user.setEmail(userEl.getAttribute(USER_EMAIL_ATTR));
					user.setRoles(userEl.getAttribute(USER_ROLES_ATTR));
					getObject().addUser(user);
				} catch (Exception e) {
					m_logger.error("Could not load a user", e);
				}
			}
			else
				m_logger.warn("A <" + USER_TAG + "> element exists but has no '" + USER_NAME_ATTR + "' attribute.");
		}
	}
	

	@Override
	public void save()
	throws StorageException {
		File file = getFile();
		if (file == null)
			return;

		// Create the file if needed
		if (!file.exists()) {
			try {
				file.createNewFile();
				m_logger.info("The file '" + file + "' has been created (empty).");
			} catch (IOException e) {
				m_logger.error("The file '" + file + "' could not be created.");
			}
		}

		// Check whether the file is writable
		if (!file.canWrite()) {
			m_logger.error("The users file '" + file + "' is not writable.");
			return;
		}
		
		// Prepare the text to be written
		StringBuffer buffer = new StringBuffer();
		buffer.append("<").append(USERS_TAG).append(">");
		for (String role : getObject().getRoles())
			buffer.append("<").append(ROLE_TAG).append(" ")
				.append(ROLE_NAME_ATTR).append("=\"").append(role).append("\"/>");
		for (User user : getObject().getUsers()) {
			buffer.append("<").append(USER_TAG).append(" ")
				.append(USER_NAME_ATTR).append("=\"").append(user.getLogin()).append("\" ");
			if (user.getNick() != null)
				buffer.append(USER_NICK_ATTR).append("=\"").append(user.getNick()).append("\" ");
			buffer.append(USER_EMAIL_ATTR).append("=\"").append(user.getEmail()).append("\" ")
				.append(USER_PSW_ATTR).append("=\"").append(user.getPassword()).append("\" ")
				.append(USER_ROLES_ATTR).append("=\"").append(user.getRoles()).append("\"/>");
		}
		buffer.append("</").append(USERS_TAG).append(">");
		
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(buffer.toString().getBytes("UTF-8"));
			m_logger.info("User base saved to '" + file + "'.");
		} catch (FileNotFoundException e) {
			m_logger.error("The users file cannot be open or written.");
		} catch (IOException e) {
			m_logger.error("Cannot write the users file", e);
		} finally {
			if (output != null) {
				try {	
					output.close();
				} catch (IOException e) {
					m_logger.error("An error occured while closing the users file.");
				}
			}
		}
	}
}
