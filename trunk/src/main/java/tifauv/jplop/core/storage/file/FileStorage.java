/**
 * 27 mars 2010
 */
package tifauv.jplop.core.storage.file;

import java.io.File;

import org.apache.log4j.Logger;

import tifauv.jplop.core.storage.StorageDelegate;

/**
 * This is an implementation that uses XML files to store the users
 * and history.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public abstract class FileStorage<T> implements StorageDelegate<T> {

	// CONSTANTS \\
	/** The configuration key of the data directory. */
	private static final String KEY_DATADIR = "jplop.datadir";
	
	/** The default data directory. */
	public static final String DEFAULT_DATADIR = "${catalina.base}/jplop-data";

	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(FileStorage.class);
	
	/** The data directory. */
	private File m_dataDir;
	
	/** The object to manage. */
	private final T m_object;
	
	
	// CONSTRUCTOR \\
	/**
	 * Initialize the object to load and save.
	 * The associated object cannot be changed afterwards.
	 */
	public FileStorage(T p_object) {
		m_object = p_object;
	}
	
	
	// GETTERS \\
	/**
	 * Gives the object to load and save
	 */
	@Override
	public final T getObject() {
		return m_object;
	}

	
	/**
	 * Gives the name of the file.
	 */
	public abstract String getFileName();
	
	
	/**
	 * Gives the file. 
	 */
	public final File getFile() {
		return new File(m_dataDir, getFileName());
	}

	
	// SETTERS \\
	/**
	 * Sets the file. If the given file path is not absolute,
	 * it will be prefixed with the context directory.
	 * 
	 * @param p_contextDir 
	 *            the current context (webapp) directory
	 * @param p_path
	 *            the given file path
	 */
	public final void setDataDir(String p_contextDir, String p_path) {
		if (p_path == null) {
			m_dataDir = null;
			return;
		}
		
		String path = p_path;
		String home = System.getProperty("catalina.home");
		if (home != null && home.length() > 0)
			path = path.replaceAll("\\$\\{catalina.home\\}", home);
		String base = System.getProperty("catalina.base");
		if (base != null && base.length() > 0)
			path = path.replaceAll("\\$\\{catalina.base\\}", base);
		
		File file = new File(path);
		if (file.isAbsolute() || p_contextDir == null || p_contextDir.length() == 0)
			m_dataDir = file;
		else
			m_dataDir = new File(p_contextDir + File.separator + "WEB-INF", path);
		
		if (!m_dataDir.exists()) {
			if (m_dataDir.mkdir())
				m_logger.info("The data directory '" + m_dataDir + "' has been created.");
			else
				m_logger.error("The data directory '" + m_dataDir + "' is needed but could not be created.");
		}
	}
}
