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
	public static final String DEFAULT_NAME = "JBoard";
	
	public static final String DEFAULT_FULLNAME = "Da J2EE tribioune";
	
	public static final String DEFAULT_URL = "http://localhost:8080/jboard";
	
	private static final String CONFIG_PROPERTIES = "tifauv.jboard.config";
	
	private static final String NAME_KEY = "jboard.name";
	
	private static final String FULLNAME_KEY = "jboard.fullName";
	
	private static final String URL_KEY = "jboard.url";
	
	private static final String SIZE_KEY = "jboard.history.size";
	
	private static final String POST_LENGTH_KEY = "jboard.post.maxLength";
	
	
	// STATIC FIELDS \\
	private static Backend s_instance;
	
	
	// FIELDS \\
	/** The board's name. */
	private String m_name;
	
	/** The board's full name. */
	private String m_fullName;
	
	/** The board's history. */
	private History m_history;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(Backend.class);
	

	// CONSTRUCTORS \\
	private Backend() {
		init();
	}
	

	// GETTERS \\
	public final String getName() {
		return m_name;
	}
	
	public final String getFullName() {
		return m_fullName;
	}
	
	public final String getURL() {
		return m_history.getURL();
	}
	
	public final int getMaxSize() {
		return m_history.maxSize();
	}
	
	public final int getMaxPostLength() {
		return Post.getMaxLength();
	}
	
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
	

	// SETTERS \\
	private final void setName(String p_name) {
		m_name = p_name;
	}
	
	private final void setFullName(String p_fullName) {
		m_fullName = p_fullName;
	}
	
	
	// METHODS \\
	public static synchronized Backend getInstance() {
		if (s_instance == null) {
			s_instance = new Backend();
		}
		return s_instance;
	}
	
	protected final synchronized void init() {
		String name = DEFAULT_NAME;
		String fullName = DEFAULT_FULLNAME;
		String url = DEFAULT_URL;
		int size = History.DEFAULT_SIZE;
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
		
		if (m_history == null)
			m_history = new History(url, size);
		else {
			m_history.setURL(url);
			m_history.setMaxSize(size);
		}
		setName(name);
		setFullName(fullName);
		Post.setMaxLength(maxPostLength);
		m_logger.info("Board '" + getName() + " - " + getFullName() + "' reloaded.");
		m_logger.info(" |- the backend at '" + url + "/backend' keeps " + size + " posts");
		m_logger.info(" `- the messages sent to '" + url + "/post' may have a length of '" + maxPostLength + "' caracters.");
	}
	
	public final synchronized void addMessage(String p_info, String p_message) {
		m_history.addPost(new Post(p_info, p_message));
	}
	
	public final synchronized void addMessage(String p_info, String p_message, String p_login) {
		m_history.addPost(new Post(p_info, p_message, p_login));
	}
}
