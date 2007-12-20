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
import tifauv.jplop.util.AbstractJob;
import tifauv.jplop.util.DeserializeException;
import tifauv.jplop.util.Serializable;
import tifauv.jplop.util.SerializeException;


/**
 * This is the entry point of the model.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class Backend {

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
	private static final String NAME_KEY = "jplop.name";
	
	/** The configuration key of the full name. */
	private static final String FULLNAME_KEY = "jplop.fullName";
	
	/** The configuration key of the URL. */
	private static final String URL_KEY = "jplop.url";
	
	/** The configuration key of the size of the history. */
	private static final String SIZE_KEY = "jplop.history.size";
	
	/** The configuration key of the save rate of the history. */
	private static final String WRITE_CACHE_KEY = "jplop.history.saveEvery";
	
	/** The configuration key of the maximum length of an incoming message. */
	private static final String POST_LENGTH_KEY = "jplop.post.maxLength";

	/** The configuration key of the data directory. */
	private static final String DATADIR_KEY = "jplop.datadir";
	
	
	// STATIC FIELDS \\
	/** The backend's instance. */
	private static Backend s_instance = new Backend();
	
	
	// FIELDS \\
	/** The board's name. */
	private String m_name;
	
	/** The board's full name. */
	private String m_fullName;
	
	/** The board's history. */
	private History m_history;
	
	/** The user base.*/
	private UserBase m_users;
	
	/** The backup job. */
	private BackupJob m_backupJob;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(Backend.class);
	

	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	private Backend() {
		m_backupJob = new BackupJob();
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
		buffer.append("<board name=\"board\" title=\"Tribune\">");
		buffer.append("<backend path=\"/backend\" public=\"true\" tags_encoded=\"false\" refresh=\"30\"/>");
		buffer.append("<post method=\"post\" path=\"/post\" anonymous=\"true\" max_length=\"")
			.append(getMaxPostLength()).append("\">");
		buffer.append("<field name=\"message\">$m</field>");
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
		if (s_instance != null) {
			if (s_instance.m_backupJob != null)
				s_instance.m_backupJob.stop();
			try {
				s_instance.saveToDisk();
			}
			catch (SerializeException e) {
				s_instance.m_logger.error(e);
			}
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
		int backupTimeout = AbstractJob.DEFAULT_TIMEOUT;
		int maxPostLength = Post.DEFAULT_MAX_POST_LENGTH;
		try {
			// Load the .properties
			ResourceBundle config = ResourceBundle.getBundle(CONFIG_PROPERTIES);
			
			// Load the name property
			try {
				name = config.getString(NAME_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			// Load the fullname property
			try {
				fullName = config.getString(FULLNAME_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			// Load the URL property
			try {
				url = config.getString(URL_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			// Load the history size property
			try {
				size = Integer.parseInt(config.getString(SIZE_KEY));
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			catch (NumberFormatException e) {
				m_logger.warn("The configuration key '" + SIZE_KEY + "' doesn't have an integer value.");
			}
			
			// Load the backup job's timeout
			try {
				backupTimeout = Integer.parseInt(config.getString(WRITE_CACHE_KEY));
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			catch (NumberFormatException e) {
				m_logger.warn("The configuration key '" + WRITE_CACHE_KEY + "' doesn't have an integer value.");
			}
			
			
			// Load the max post length property
			try {
				maxPostLength = Integer.parseInt(config.getString(POST_LENGTH_KEY));
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			catch (NumberFormatException e) {
				m_logger.warn("The configuration key '" + SIZE_KEY + "' doesn't have an integer value.");
			}

			
			// Load the data directory name
			try {
				dataDir = config.getString(DATADIR_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
		}
		catch (MissingResourceException e) {
			m_logger.error("The configuration file '" + CONFIG_PROPERTIES + "' is not in the CLASSPATH.");
		}

		/* Update the data directory */
		Serializable.setDataDir(p_contextDir, dataDir);
		
		/* Update the history */
		if (getHistory() == null)
			setHistory(new History(url, size));
		else {
			getHistory().setURL(url);
			getHistory().setMaxSize(size);
		}
		
		/* Update the user base */
		if (getUserBase() == null)
			setUserBase(new UserBase());
		
		/* Update the backup job. */
		m_backupJob.setTimeout(backupTimeout * 60000);
		
		setName(name);
		setFullName(fullName);
		Post.setMaxLength(maxPostLength);
		m_logger.info("Board '" + getName() + " - " + getFullName() + "' reloaded.");
		m_logger.info(" |- the board is hosted at '" + url + "'");
		m_logger.info(" |- the backend keeps " + size + " posts");
		m_logger.info(" |- the data are stored in '" + Serializable.getDataDir() + "'");
		m_logger.info(" |- the backend will be saved every " + backupTimeout + " minutes");
		m_logger.info(" `- the messages sent to '" + url + "/post' may have a length of '" + maxPostLength + "' caracters.");

		/* start the backup job if needed. */
		if (m_backupJob.isStopped())
			m_backupJob.start();
	}

	
	/**
	 * Loads the backend from the disk.
	 * This currently loads the history and user base.
	 */
	public synchronized final void loadFromDisk()
	throws DeserializeException {
		getHistory().loadFromFile();
		getUserBase().loadFromFile();
	}
	
	
	/**
	 * Saves the backend to disk.
	 * This currently saves the user base and history.
	 */
	public synchronized final void saveToDisk()
	throws SerializeException {
		getUserBase().saveToFile();
		getHistory().saveToFile();
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
