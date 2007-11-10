/**
 * 19 oct. 07
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
 * 
 *
 * @version 0.1
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class Backend {

	// CONSTANTS \\
	public static final String DEFAULT_NAME = "JBoard";
	
	public static final String DEFAULT_FULLNAME = "Da J2EE tribioune";
	
	public static final String DEFAULT_URL = "http://localhost:8080/jboard";
	
	public static final String DEFAULT_CACHEFILE = "jboard.cache";
	
	private static final String CONFIG_PROPERTIES = "tifauv.jplop.config";
	
	private static final String NAME_KEY = "jboard.name";
	
	private static final String FULLNAME_KEY = "jboard.fullName";
	
	private static final String URL_KEY = "jboard.url";
	
	private static final String SIZE_KEY = "jboard.history.size";
	
	private static final String POST_LENGTH_KEY = "jboard.post.maxLength";
	
	private static final String CACHE_FILE_KEY = "jboard.cache";
	
	private static final String WEBINF = "WEB-INF";
	
	
	// STATIC FIELDS \\
	private static Backend s_instance;
	
	
	// FIELDS \\
	/** The board's name. */
	private String m_name;
	
	/** The board's full name. */
	private String m_fullName;
	
	/** The board's history. */
	private History m_history;
	
	/** The cache file on disk. */
	private File m_cacheFile;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(Backend.class);
	

	// CONSTRUCTORS \\
	private Backend() {
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
	
	public final File getCacheFile() {
		return m_cacheFile;
	}
	

	// SETTERS \\
	private final void setName(String p_name) {
		m_name = p_name;
	}
	
	private final void setFullName(String p_fullName) {
		m_fullName = p_fullName;
	}
	
	private final void setCacheFile(String p_contextDir, String p_cacheFile) {
		File cacheFile = new File(p_cacheFile);
		if (cacheFile.isAbsolute())
			m_cacheFile = cacheFile;
		else
			m_cacheFile = new File(p_contextDir + File.separator + WEBINF, p_cacheFile);
	}
	
	
	// METHODS \\
	public static synchronized Backend getInstance() {
		if (s_instance == null) {
			s_instance = new Backend();
		}
		return s_instance;
	}
	
	public final synchronized void init(String p_contextDir) {
		String name = DEFAULT_NAME;
		String fullName = DEFAULT_FULLNAME;
		String url = DEFAULT_URL;
		int size = History.DEFAULT_SIZE;
		int maxPostLength = Post.DEFAULT_MAX_POST_LENGTH;
		String cacheFile = DEFAULT_CACHEFILE;
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
			
			// Load the cache file
			try {
				cacheFile = config.getString(CACHE_FILE_KEY);
			}
			catch (MissingResourceException e) {
				m_logger.warn("The configuration key '" + e.getKey() + "' does not exist.");
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
		setCacheFile(p_contextDir, cacheFile);
		Post.setMaxLength(maxPostLength);
		m_logger.info("Board '" + getName() + " - " + getFullName() + "' reloaded.");
		m_logger.info(" |- the backend at '" + url + "/backend' keeps " + size + " posts");
		m_logger.info(" |- the backend is cached to file '" + getCacheFile() + "'");
		m_logger.info(" `- the messages sent to '" + url + "/post' may have a length of '" + maxPostLength + "' caracters.");
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
		if (!cacheFile.canWrite())
			m_logger.warn("Cannot write the cache file '" + getCacheFile() + "'");
		
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
	
	public final synchronized void addMessage(String p_info, String p_message, String p_login) {
		m_history.addMessage(p_info, p_message, p_login);
	}
}
