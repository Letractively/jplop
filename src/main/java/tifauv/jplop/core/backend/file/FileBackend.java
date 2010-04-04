/**
 * 19 oct. 2007
 */
package tifauv.jplop.core.backend.file;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import tifauv.jplop.core.CommonConstants;
import tifauv.jplop.core.auth.User;
import tifauv.jplop.core.auth.UserBase;
import tifauv.jplop.core.backend.Backend;
import tifauv.jplop.core.backend.file.persistence.DeserializeException;
import tifauv.jplop.core.backend.file.persistence.PersistenceManager;
import tifauv.jplop.core.board.History;
import tifauv.jplop.core.board.Post;


/**
 * This is the entry point of the model.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class FileBackend implements Backend {

	// CONSTANTS \\
	/** The default configuration file. */
	private static final String CONFIG_PROPERTIES = "tifauv.jplop.config";
	
	/** The default name. */
	public static final String DEFAULT_NAME = "JPlop";
	
	/** The default full name. */
	public static final String DEFAULT_FULLNAME = "Da J2EE tribioune";
	
	/** The default URL. */
	public static final String DEFAULT_URL = "http://localhost:8080/jplop";
	
	/** The default data directory. */
	public static final String DEFAULT_DATADIR = "${catalina.base}/jplop-data";
	
	/** The configuration key of the name. */
	private static final String KEY_NAME = "jplop.name";
	
	/** The configuration key of the full name. */
	private static final String KEY_FULLNAME = "jplop.fullName";
	
	/** The configuration key of the URL. */
	private static final String KEY_URL = "jplop.url";
	
	/** The configuration key of the size of the history. */
	private static final String KEY_HISTORY_SIZE = "jplop.history.size";
	
	/** The configuration key of the maximum length of an incoming message. */
	private static final String KEY_POST_LENGTH = "jplop.post.maxLength";

	/** The configuration key of the data directory. */
	private static final String KEY_DATADIR = "jplop.datadir";

	/** The configuration key of the backup frequency of the backend. */
	private static final String KEY_BACKUP_FREQ = "jplop.backupEvery";
	
	
	// FIELDS \\
	/** The board's name. */
	private String m_name;
	
	/** The board's full name. */
	private String m_fullName;
	
	/** The board's history. */
	private History m_history;
	
	/** The user base.*/
	private UserBase m_users;
	
	/** The persistence manager. */
	private PersistenceManager m_persistence;
	
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(FileBackend.class);
	

	// GETTERS \\
	/**
	 * Gives the board's name.
	 */
	public String getName() {
		return m_name;
	}

	
	/**
	 * Gives the board's full name.
	 */
	public String getFullName() {
		return m_fullName;
	}
	
	
	/**
	 * Gives the history.
	 */
	protected History getHistory() {
		return m_history;
	}
	
	
	/**
	 * Gives the persistence manager.
	 */
	private PersistenceManager getPersistenceManager() {
		if (m_persistence == null)
			m_persistence = new PersistenceManager();
		return m_persistence;
	}
	
	
	/**
	 * Gives the board's URL.
	 */
	public String getURL() {
		return getHistory().getURL();
	}
	
	
	/**
	 * Gives the maximum number of messages of the history.
	 */
	public int getMaxSize() {
		return getHistory().maxSize();
	}
	
	
	/**
	 * Gives the user base.
	 */
	public UserBase getUserBase() {
		return m_users;
	}
	
	
	/**
	 * Gives the maximum length of the incoming messages.
	 * Bigger messages will be cut.
	 * 
	 * @see #addMessage(String, String, String)
	 */
	public int getMaxPostLength() {
		return Post.getMaxLength();
	}
	
	
	/**
	 * Gives the Last-Modified header value to send.
	 */
	public String getLastModified() {
		return getHistory().getLastModified();
	}
	
	
	/**
	 * Gives the backend text.
	 */
	public String getText() {
		return getHistory().toString();
	}

	
	/**
	 * Gives the backend text if it has been modified since the given date.
	 * Otherwise, just return <code>null</code>.
	 * 
	 * @param p_modifiedSince
	 *            the Modified-Since value
	 * 
	 * @return the backend text or <code>null</code>
	 */
	public synchronized String getText(String p_modifiedSince) {
		if (getHistory().isModifiedSince(p_modifiedSince))
			return getHistory().toString();
		return null;
	}
	
	
	/**
	 * Gives the board's configuration for compliant coincoins.
	 */
	public String getBoardConfig() {
		StringBuffer buffer = new StringBuffer(473
				+ getName().length()
				+ getFullName().length()
				+ getURL().length()
				+ CommonConstants.LOGIN_PARAM.length()
				+ CommonConstants.PASSWORD_PARAM.length()
				+ CommonConstants.MESSAGE_PARAM.length());
		buffer.append("<?xml version=\"1.0\"?>")
		.append("<site name=\"").append(getName())
		.append("\" title=\"").append(getFullName())
		.append("\" baseurl=\"").append(getURL())
		.append("\" version=\"1.1\">")
		.append("<account>")
		.append("<login method=\"post\" path=\"/logon\">")
		.append("<field name=\"").append(CommonConstants.LOGIN_PARAM).append("\">$l</field>")
		.append("<field name=\"").append(CommonConstants.PASSWORD_PARAM).append("\">$p</field>")
		.append("</login>")
		.append("<logout method=\"get\" path=\"/logout\"/>")
		.append("</account>")
		.append("<module name=\"board\" title=\"Tribune\" type=\"application/board+xml\">")
		.append("<backend path=\"/backend\" public=\"true\" tags_encoded=\"false\" refresh=\"30\"/>")
		.append("<post method=\"post\" path=\"/post\" anonymous=\"true\" max_length=\"")
		.append(getMaxPostLength()).append("\">")
		.append("<field name=\"").append(CommonConstants.MESSAGE_PARAM).append("\">$m</field>")
		.append("</post>")
		.append("</module>")
		.append("</site>");
		return buffer.toString();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getSettings(HttpServletRequest p_request) {
		StringBuffer buffer = new StringBuffer(473
				+ getName().length()
				+ getFullName().length()
				+ getURL().length()
				+ CommonConstants.LOGIN_PARAM.length()
				+ CommonConstants.PASSWORD_PARAM.length()
				+ CommonConstants.MESSAGE_PARAM.length());
		buffer.append("<?xml version=\"1.0\"?>")
		.append("<settings version=\"1.0\">");

		// Add the login
		User user = (User)p_request.getSession().getAttribute(CommonConstants.USER_SESSION_ATTR);
		if (user != null)
			buffer.append("<param name=\"Login\">").append(user.getLogin()).append("</param>");
		
		// Add the nick
		if (user != null && user.getNick() != null)
			buffer.append("<param name=\"Nick\">").append(user.getNick()).append("</param>");
		else {
			String nick = (String)p_request.getSession().getAttribute(CommonConstants.NICK_SESSION_ATTR);
			if (nick != null)
				buffer.append("<param name=\"Nick\">").append(nick).append("</param>");
		}
		
		// Add the user agent
		buffer.append("<param name=\"User-Agent\">")
		.append(p_request.getHeader(CommonConstants.USER_AGENT_HDR))
		.append("</param>");
		buffer.append("</settings>");
		return buffer.toString();
	}
	
	
	// SETTERS \\
	/**
	 * Sets the board's name.
	 * 
	 * @param p_name
	 *            the name of the board
	 */
	private void setName(String p_name) {
		m_name = p_name;
	}
	
	
	/**
	 * Sets the board's full name.
	 * 
	 * @param p_fullName
	 *            the full name of the board
	 */
	private void setFullName(String p_fullName) {
		m_fullName = p_fullName;
	}
	
	
	/**
	 * Sets the backend's history.
	 * 
	 * @param p_history
	 *            the history
	 */
	private void setHistory(History p_history) {
		m_history = p_history;
	}
	
	
	/**
	 * Sets the user base.
	 * 
	 * @param p_userBase
	 *            the user base
	 */
	private void setUserBase(UserBase p_userBase) {
		m_users = p_userBase;
	}
	
	
	// METHODS \\
	/**
	 * Loads teh configuration of the Backend.
	 * 
	 * @param p_contextDir
	 *            the context directory
	 */
	public synchronized void loadConfig(String p_contextDir) {
		String name = DEFAULT_NAME;
		String fullName = DEFAULT_FULLNAME;
		String url = DEFAULT_URL;
		int size = History.DEFAULT_SIZE;
		String dataDir = DEFAULT_DATADIR;
		int backupTimeout = PersistenceManager.DEFAULT_BACKUP_FREQUENCY;
		int maxPostLength = Post.DEFAULT_MAX_POST_LENGTH;
		
		try {
			// Load the .properties
			ResourceBundle config = ResourceBundle.getBundle(CONFIG_PROPERTIES);
			
			// Load the name property
			try {
				name = config.getString(KEY_NAME);
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			// Load the fullname property
			try {
				fullName = config.getString(KEY_FULLNAME);
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			// Load the URL property
			try {
				url = config.getString(KEY_URL);
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			// Load the history size property
			try {
				size = Integer.parseInt(config.getString(KEY_HISTORY_SIZE));
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			} catch (NumberFormatException e) {
				m_logger.warn("The configuration key '" + KEY_HISTORY_SIZE + "' doesn't have an integer value.");
			}
			
			// Load the backup frenquency
			try {
				backupTimeout = Integer.parseInt(config.getString(KEY_BACKUP_FREQ));
				if (backupTimeout == 0)
					backupTimeout = PersistenceManager.DEFAULT_BACKUP_FREQUENCY;
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			} catch (NumberFormatException e) {
				m_logger.warn("The configuration key '" + KEY_BACKUP_FREQ + "' doesn't have an integer value.");
			}
			
			
			// Load the max post length property
			try {
				maxPostLength = Integer.parseInt(config.getString(KEY_POST_LENGTH));
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			} catch (NumberFormatException e) {
				m_logger.warn("The configuration key '" + KEY_HISTORY_SIZE + "' doesn't have an integer value.");
			}

			
			// Load the data directory name
			try {
				dataDir = config.getString(KEY_DATADIR);
			} catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
		} catch (MissingResourceException e) {
			m_logger.error("The configuration file '" + CONFIG_PROPERTIES + "' is not in the CLASSPATH.");
		}

		/* Update the persistence */
		getPersistenceManager().setDataDir(p_contextDir, dataDir);
		getPersistenceManager().setTimeout(backupTimeout);
		
		/* Update the history */
		if (getHistory() == null) {
			setHistory(new History(url, size));
			try {
				getPersistenceManager().register(getHistory());
			} catch (DeserializeException e) {
				m_logger.error("Could not restore the history state : ", e);
			}
		}
		else {
			getHistory().setURL(url);
			getHistory().setMaxSize(size);
		}
		
		/* Update the user base */
		if (getUserBase() == null) {
			setUserBase(new UserBase());
			try {
				getPersistenceManager().register(getUserBase());
			} catch (DeserializeException e) {
				m_logger.error("Could not restore the user base : ", e);
			}
		}
		
		setName(name);
		setFullName(fullName);
		Post.setMaxLength(maxPostLength);
		m_logger.info("Board '" + getName() + " - " + getFullName() + "' reloaded.");
		m_logger.info(" |- the board is hosted at '" + url + "'");
		m_logger.info(" |- the backend keeps " + size + " posts");
		m_logger.info(" |- the data are stored in '" + getPersistenceManager().getDataDir() + "'");
		m_logger.info(" |- the backend will be saved every " + backupTimeout + " minute" + (backupTimeout < 2 ? "" : "s"));
		m_logger.info(" `- the messages sent to '" + url + "/post' may have a length of '" + maxPostLength + "' caracters.");
		
		
	}
	
	
	/**
	 * Starts the persistence manager backup job.
	 */
	public void init() {
		getPersistenceManager().startBackupJob();
	}
	
	
	/**
	 * Adds a received message to the history.
	 * 
	 * @param p_info
	 *            the user-agent
	 * @param p_message
	 *            the message
	 * @param p_login
	 *            the login
	 * 
	 * @return the id of the new post
	 */
	public synchronized long addMessage(String p_info, String p_message, String p_login) {
		return getHistory().addMessage(p_info, p_message, p_login);
	}
	
	
	/**
	 * Stops the persistence manager backup job.
	 */
	public void clean() {
		getPersistenceManager().clean();
	}
}
