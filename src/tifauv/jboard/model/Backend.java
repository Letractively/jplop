/**
 * 19 oct. 07
 */
package tifauv.jboard.model;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * 
 *
 * @version 0.1
 *
 * @author Olivier Serve <olivier.serve@bull.net>
 */
public class Backend {

	// CONSTANTS \\
	public static final String DEFAULT_URL = "http://localhost:8080/jboard";
	
	private static final String CONFIG_PROPERTIES = "tifauv.jboard.config";
	
	private static final String URL_KEY = "jboard.url";
	
	private static final String SIZE_KEY = "jboard.size";
	
	
	// STATIC FIELDS \\
	private static Backend s_instance;
	
	
	// FIELDS \\
	/** The board's history. */
	private History m_history;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(Backend.class);
	

	// CONSTRUCTORS \\
	private Backend() {
		init();
	}
	

	// GETTERS \\
	public final String getLastModified() {
		return m_history.getLastModified();
	}
	
	public final String getText() {
		return m_history.toString();
	}
	
	public final synchronized String getText(String p_modifiedSince) {
		if (m_history.isModifiedSince(p_modifiedSince))
			return m_history.toString();
		return null;
	}
	

	// METHODS \\
	public static synchronized Backend getInstance() {
		if (s_instance == null) {
			s_instance = new Backend();
		}
		return s_instance;
	}
	
	protected final synchronized void init() {
		String url = DEFAULT_URL;
		int size = History.DEFAULT_SIZE;
		try {
			ResourceBundle config = ResourceBundle.getBundle(CONFIG_PROPERTIES);
			try {
				url = config.getString(URL_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
			}
			
			try {
				size = Integer.parseInt(config.getString(SIZE_KEY));
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
		
		if (m_history == null)
			m_history = new History(url, size);
		else {
			m_history.setURL(url);
			m_history.setMaxSize(size);
		}
		m_logger.info("Backend at '" + url + "' with " + size + "-posts history ready.");
	}
	
	public final synchronized void addMessage(String p_info, String p_message) {
		m_history.addPost(new Post(p_info, p_message));
	}
	
	public final synchronized void addMessage(String p_info, String p_message, String p_login) {
		m_history.addPost(new Post(p_info, p_message, p_login));
	}
}
