package tifauv.jplop.util;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * This interface defines the common methods that are expected
 * from objects that should load and save themselves.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public abstract class Serializable {
	
	// FIELDS \\
	/** The data directory. */
	private static File s_dataDir;
	
	/** The logger. */
	private static Logger s_logger = Logger.getLogger(Serializable.class);
	

	// GETTERS \\
	/**
	 * Gives the data directory.
	 */
	public static final File getDataDir() {
		return s_dataDir;
	}
	
	
	/**
	 * Gives the file.
	 */
	public abstract File getFile();
	
	
	// SETTERS \\
	/**
	 * Sets the file. If the given file path is not absolute,
	 * it will be prefixed with the context directory.
	 * 
	 * @param p_contextDir 
	 *            the current context directory
	 * @param p_file
	 *            the given file path
	 */
	public static final void setDataDir(String p_contextDir, String p_path) {
		String path = p_path;
		String home = System.getProperty("catalina.home");
		if (home != null && home.length() > 0)
			path = path.replaceAll("\\$\\{catalina.home\\}", home);
		String base = System.getProperty("catalina.base");
		if (base != null && base.length() > 0)
			path = path.replaceAll("\\$\\{catalina.base\\}", base);
		
		File file = new File(path);
		if (file.isAbsolute())
			s_dataDir = file;
		else
			s_dataDir = new File(p_contextDir + File.separator + "WEB-INF", path);
		
		if (!s_dataDir.exists()) {
			if (s_dataDir.mkdir())
				s_logger.info("The data directory '" + s_dataDir + "' has been created.");
			else {
				s_logger.error("The data directory '" + s_dataDir + "' is needed but could be created.");
				s_logger.warn("JPlop's status will not be saved !");
			}
		}
	}
	
	
	// METHODS \\
	/**
	 * Loads from the file.
	 */
	public abstract void loadFromFile()
	throws DeserializeException;
	
	/**
	 * Saves to the file.
	 */
	public abstract void saveToFile()
	throws SerializeException;
}
