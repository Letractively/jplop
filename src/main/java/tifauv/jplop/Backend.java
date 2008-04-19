/**
 * 19 oct. 2007
 */
package tifauv.jplop;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import tifauv.jplop.auth.UserBase;
import tifauv.jplop.board.History;
import tifauv.jplop.board.Post;
import tifauv.jplop.persistence.DeserializeException;
import tifauv.jplop.persistence.PersistenceManager;
import tifauv.jplop.servlets.CommonConstants;
import tifauv.jplop.util.AbstractJob;


/**
 * This is the entry point of the model.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class Backend {

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
	
	
	// STATIC FIELDS \\
	/** The backend's instance. */
	private static Backend s_instance;
	
	
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
	private final Logger m_logger = Logger.getLogger(Backend.class);
	

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	private Backend() {
		// Nothing to do
	}
	

	// GETTERS \\
	/**
	 * Gives the board's name.
	 */
	public final String getName() {
		return m_name;
	}

	
	/**
	 * Gives the board's full name.
	 */
	public final String getFullName() {
		return m_fullName;
	}
	
	
	/**
	 * Gives the history.
	 */
	protected final History getHistory() {
		return m_history;
	}
	
	
	/**
	 * Gives the persistence manager.
	 */
	private final PersistenceManager getPersistenceManager() {
		if (m_persistence == null)
			m_persistence = new PersistenceManager();
		return m_persistence;
	}
	
	
	/**
	 * Gives the board's URL.
	 */
	public final String getURL() {
		return getHistory().getURL();
	}
	
	
	/**
	 * Gives the maximum number of messages of the history.
	 */
	public final int getMaxSize() {
		return getHistory().maxSize();
	}
	
	
	/**
	 * Gives the user base.
	 */
	public final UserBase getUserBase() {
		return m_users;
	}
	
	
	/**
	 * Gives the maximum length of the incoming messages.
	 * Bigger messages will be cut.
	 * 
	 * @see #addMessage(String, String, String)
	 */
	public final int getMaxPostLength() {
		return Post.getMaxLength();
	}
	
	
	/**
	 * Gives the Last-Modified header value to send.
	 */
	public final String getLastModified() {
		return getHistory().getLastModified();
	}
	
	
	/**
	 * Gives the backend text.
	 */
	public final String getText() {
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
	public final synchronized String getText(String p_modifiedSince) {
		if (getHistory().isModifiedSince(p_modifiedSince))
			return getHistory().toString();
		return null;
	}
	
	
	/**
	 * Gives the board's configuration for compliant coincoins.
	 */
	public final String getBoardConfig() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\"?>");
		buffer.append("<site name=\"").append(getName())
			.append("\" title=\"").append(getFullName())
			.append("\" baseurl=\"").append(getURL())
			.append("\" version=\"1.0\">");
		buffer.append("<account>");
		buffer.append("<login method=\"post\" path=\"/logon\">");
		buffer.append("<field name=\"").append(CommonConstants.LOGIN_PARAM).append("\">$l</field>");
		buffer.append("<field name=\"").append(CommonConstants.PASSWORD_PARAM).append("\">$p</field>");
		buffer.append("</login>");
		buffer.append("<logout method=\"get\" path=\"/logout\"/>");
		buffer.append("</account>");
		buffer.append("<board name=\"board\" title=\"Tribune\">");
		buffer.append("<backend path=\"/backend\" public=\"true\" tags_encoded=\"false\" refresh=\"30\"/>");
		buffer.append("<post method=\"post\" path=\"/post\" anonymous=\"true\" max_length=\"")
			.append(getMaxPostLength()).append("\">");
		buffer.append("<field name=\"").append(CommonConstants.MESSAGE_PARAM).append("\">$m</field>");
		buffer.append("</post>");
		buffer.append("</board>");
		buffer.append("</site>");
		return buffer.toString();
	}
	
	
	// SETTERS \\
	/**
	 * Sets the board's name.
	 * 
	 * @param p_name
	 *            the name of the board
	 */
	private final void setName(String p_name) {
		m_name = p_name;
	}
	
	
	/**
	 * Sets the board's full name.
	 * 
	 * @param p_fullName
	 *            the full name of the board
	 */
	private final void setFullName(String p_fullName) {
		m_fullName = p_fullName;
	}
	
	
	/**
	 * Sets the backend's history.
	 * 
	 * @param p_history
	 *            the history
	 */
	private final void setHistory(History p_history) {
		m_history = p_history;
	}
	
	
	/**
	 * Sets the user base.
	 * 
	 * @param p_userBase
	 *            the user base
	 */
	private final void setUserBase(UserBase p_userBase) {
		m_users = p_userBase;
	}
	
	
	// METHODS \\
	public static void create(String p_contextDir) {
		s_instance = new Backend();
		getInstance().init(p_contextDir);
		getInstance().getPersistenceManager().startBackupJob();
	}
	
	
	/**
	 * Gives the backend's instance.
	 */
	public static Backend getInstance() {
		return s_instance;
	}
	
	
	/**
	 * Stops the backup job and saves the backend.
	 */
	public static synchronized void destroy() {
		if (getInstance() != null) {
			getInstance().getPersistenceManager().clean();
			s_instance = null;
		}
	}
	

	/**
	 * Initializes the Backend. Loads the configuration properties.
	 * 
	 * @param p_contextDir
	 *            the context directory
	 */
	public final synchronized void init(String p_contextDir) {
		String name = DEFAULT_NAME;
		String fullName = DEFAULT_FULLNAME;
		String url = DEFAULT_URL;
		int size = History.DEFAULT_SIZE;
		String dataDir = DEFAULT_DATADIR;
		int backupTimeout = AbstractJob.DEFAULT_FREQUENCY;
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
				if (backupTimeout == 0) {
					backupTimeout = AbstractJob.DEFAULT_FREQUENCY;
				}
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
	 * Adds a received message to the history.
	 * 
	 * @param p_info
	 *            the user-agent
	 * @param p_message
	 *            the message
	 * @param p_login
	 *            the login
	 */
	public final synchronized void addMessage(String p_info, String p_message, String p_login) {
		m_history.addMessage(p_info, p_message, p_login);
	}
}
