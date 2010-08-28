package tifauv.jplop.core.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import tifauv.jplop.core.board.History;
import tifauv.jplop.core.board.Post;
import tifauv.jplop.core.storage.StorageManager;

public final class Configuration {
	
	// CONSTANTS \\
	/** The default configuration file. */
	private static final String CONFIG_PROPERTIES = "/jplop.properties";
	
	/** The default name. */
	public static final String DEFAULT_NAME = "JPlop";
	
	/** The default full name. */
	public static final String DEFAULT_FULLNAME = "Da J2EE tribioune";
	
	/** The default URL. */
	public static final String DEFAULT_URL = "http://localhost:8080/jplop";
	
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

	/** The configuration key of the backup frequency of the backend. */
	private static final String KEY_BACKUP_FREQ = "jplop.backupEvery";
	
	
	// FIELDS \\
	/** The configuration properties. */
	private Properties m_config;
	
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(Configuration.class);
	

	// GETTERS \\
	/**
	 * Gives the board's name.
	 */
	public String getName() {
		return getString(KEY_NAME);
	}

	
	/**
	 * Gives the board's full name.
	 */
	public String getFullName() {
		return getString(KEY_FULLNAME);
	}
	
	
	/**
	 * Gives the board's URL.
	 */
	public String getURL() {
		return getString(KEY_URL);
	}
	
	
	/**
	 * Gives the maximum number of messages of the history.
	 */
	public int getMaxSize() {
		return getInt(KEY_HISTORY_SIZE);
	}
	
	
	public int getAutoSaveInterval() {
		return getInt(KEY_BACKUP_FREQ);
	}
	
	
	/**
	 * Gives the maximum length of the incoming messages.
	 * Bigger messages will be cut.
	 * 
	 * @see #addMessage(String, String, String)
	 */
	public int getMaxPostLength() {
		return getInt(KEY_POST_LENGTH);
	}

	
	public String getString(String p_key) {
		return m_config.getProperty(p_key);
	}
	
	
	public int getInt(String p_key) {
		return Integer.parseInt(m_config.getProperty(p_key));
	}
	
	
	// METHODS \\
	/**
	 * Loads the configuration of the Backend.
	 * 
	 * @param p_contextDir
	 *            the context directory
	 */
	public synchronized void load(String p_contextDir) {
		// Build a new Properties if needed
		if (m_config == null)
			m_config = new Properties();

		// Try to load the file
		InputStream configStream = getClass().getResourceAsStream(CONFIG_PROPERTIES);
		if (configStream == null)
			m_logger.error("The configuration file '" + CONFIG_PROPERTIES + "' is not in the CLASSPATH.");
		else {
			try {
				m_config.load(configStream);
			} catch (Exception e) {
				m_logger.error("Failed to read the configuration file '" + CONFIG_PROPERTIES + "'", e);
			}
		}
			
		// Reset default values
		setDefaultIfAbsent(m_config, KEY_NAME,         DEFAULT_NAME);
		setDefaultIfAbsent(m_config, KEY_FULLNAME,     DEFAULT_FULLNAME);
		setDefaultIfAbsent(m_config, KEY_URL,          DEFAULT_URL);
		setDefaultIfAbsent(m_config, KEY_HISTORY_SIZE, History.DEFAULT_SIZE);
		setDefaultIfAbsent(m_config, KEY_BACKUP_FREQ,  StorageManager.DEFAULT_AUTOSAVE_INTERVAL);
		setDefaultIfAbsent(m_config, KEY_POST_LENGTH,  Post.DEFAULT_MAX_POST_LENGTH);
		
		m_logger.info("Board '" + getName() + " - " + getFullName() + "' reloaded.");
	}
	
	
	private void setDefaultIfAbsent(Properties p_props, String p_key, String p_defaultValue) {
		if (p_props.containsKey(p_key))
			return;
		
		p_props.setProperty(p_key, p_defaultValue);
		m_logger.warn("The configuration key '" + p_key + "' does not exist.");
		m_logger.warn(" `-> added default " + p_key + "=" + p_defaultValue);
	}
	
	
	private void setDefaultIfAbsent(Properties p_props, String p_key, int p_defaultValue) {
		if (p_props.containsKey(p_key)) {
			String value = p_props.getProperty(p_key);
			try {
				Integer.parseInt(value);
				return;
			} catch (NumberFormatException e) {
				m_logger.warn("The value '" + value + "' of configuration key '" + p_key + "' is not an integer.");
			}
		}
		else
			m_logger.warn("The configuration key '" + p_key + "' does not exist.");

		p_props.setProperty(p_key, Integer.toString(p_defaultValue));
		m_logger.warn(" `-> added default " + p_key + "=" + Integer.toString(p_defaultValue));
	}
}
