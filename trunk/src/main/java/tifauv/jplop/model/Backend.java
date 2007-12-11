/**
 * 19 oct. 2007
 */
package tifauv.jplop.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

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
	
	/** The default cache file. */
	public static final String DEFAULT_CACHEFILE = "jplop.cache";

	/** The configuration key of the name. */
	private static final String NAME_KEY = "jplop.name";
	
	/** The configuration key of the full name. */
	private static final String FULLNAME_KEY = "jplop.fullName";
	
	/** The configuration key of the URL. */
	private static final String URL_KEY = "jplop.url";
	
	/** The configuration key of the size of the history. */
	private static final String SIZE_KEY = "jplop.history.size";
	
	/** The configuration key of the cache file of the history. */
	private static final String CACHE_FILE_KEY = "jplop.history.cache";

	/** The configuration key of the save rate of the history. */
	private static final String WRITE_CACHE_KEY = "jplop.history.saveEvery";
	
	/** The configuration key of the maximum length of an incoming message. */
	private static final String POST_LENGTH_KEY = "jplop.post.maxLength";
	
	/** The name of the WEB-INF directory. */
	private static final String WEBINF = "WEB-INF";
	
	
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
	
	/** The cache file on disk. */
	private File m_cacheFile;
	
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
	 * Gives the board's URL.
	 */
	public final String getURL() {
		return m_history.getURL();
	}
	
	
	/**
	 * Gives the maximum number of messages of the history.
	 */
	public final int getMaxSize() {
		return m_history.maxSize();
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
		return m_history.getLastModified();
	}
	
	
	/**
	 * Gives the backend text.
	 */
	public final String getText() {
		return m_history.toString();
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
		if (m_history.isModifiedSince(p_modifiedSince))
			return m_history.toString();
		return null;
	}
	
	
	/**
	 * Gives the cache file.
	 */
	public final File getCacheFile() {
		return m_cacheFile;
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
	 * Sets the cache file.
	 * 
	 * @param p_contextDir
	 *            the context's directory path
	 * @param p_cacheFile
	 *            the name of the cache file
	 */
	private final void setCacheFile(String p_contextDir, String p_cacheFile) {
		File cacheFile = new File(p_cacheFile);
		if (cacheFile.isAbsolute())
			m_cacheFile = cacheFile;
		else
			m_cacheFile = new File(p_contextDir + File.separator + WEBINF, p_cacheFile);
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
			s_instance.saveToCache();
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
		String cacheFile = DEFAULT_CACHEFILE;
		int backupTimeout = BackupJob.DEFAULT_TIMEOUT;
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
			
			// Load the cache file
			try {
				cacheFile = config.getString(CACHE_FILE_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
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
		}
		catch (MissingResourceException e) {
			m_logger.error("The configuration file '" + CONFIG_PROPERTIES + "' is not in the CLASSPATH.");
		}
		
		/* Update the history */
		if (m_history == null)
			m_history = new History(url, size);
		else {
			m_history.setURL(url);
			m_history.setMaxSize(size);
		}
		
		/* Update the backup job. */
		m_backupJob.setTimeout(backupTimeout * 60000);
		
		setName(name);
		setFullName(fullName);
		setCacheFile(p_contextDir, cacheFile);
		Post.setMaxLength(maxPostLength);
		m_logger.info("Board '" + getName() + " - " + getFullName() + "' reloaded.");
		m_logger.info(" |- the board is hosted at '" + url + "'");
		m_logger.info(" |- the backend keeps " + size + " posts");
		m_logger.info(" |- the backend is cached to file '" + getCacheFile() + "'");
		m_logger.info(" |- the backend will be saved every " + backupTimeout + " minutes");
		m_logger.info(" `- the messages sent to '" + url + "/post' may have a length of '" + maxPostLength + "' caracters.");

		/* start the backup job if needed. */
		if (m_backupJob.isStopped())
			m_backupJob.start();
	}

	
	/**
	 * Loads the backend from the cache file. 
	 */
	public synchronized final void loadFromCache()
	throws IOException,
	SAXException,
	ParseException {
		File cacheFile = getCacheFile();
		if (cacheFile.exists()) {
			m_logger.info("Loading the Backend from cache...");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				m_history.load(builder.parse(getCacheFile()));
				m_logger.info(m_history.size() + " posts loaded from cache.");
			}
			catch (ParserConfigurationException e) {
				// Cannot happen
			}
		}
		else
			m_logger.debug("The cache file does not exist.");
	}
	
	
	/**
	 * Saves the backend to a cache file.
	 * Does nothing if the history is empty.
	 */
	public synchronized final void saveToCache() {
		if (m_history.isEmpty())
			return;
		
		File cacheFile = getCacheFile();
		
		// Create the file if needed
		if (!cacheFile.exists()) {
			try {
				cacheFile.createNewFile();
				m_logger.info("The cache file '" + getCacheFile() + "' has been created (empty).");
			}
			catch (IOException e) {
				m_logger.error("The cache file '" + getCacheFile() + "' could not be created.");
			}
		}

		// Check whether the file is writable
		if (!cacheFile.canWrite()) {
			m_logger.error("The cache file '" + getCacheFile() + "' is not writable.");
			return;
		}
		
		try {
			FileOutputStream output = new FileOutputStream(getCacheFile());
			output.write(getText().getBytes("UTF-8"));
			output.close();
			m_logger.info("Backend saved to cache.");
		}
		catch (FileNotFoundException e) {
			m_logger.error("The cache file does not exist.");
		}
		catch (IOException e) {
			m_logger.error("Cannot write the cache file", e);
		}
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
