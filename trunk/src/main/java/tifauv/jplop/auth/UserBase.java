package tifauv.jplop.auth;

import java.io.File;

import tifauv.jplop.util.DeserializeException;
import tifauv.jplop.util.Serializable;
import tifauv.jplop.util.SerializeException;

/**
 * This is a list of users.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class UserBase implements Serializable {
	
	// CONSTANTS \\
	/** The name of the default list file on disk. */
	public static final String DEFAULT_FILE = "users.list";
	
	
	// FIELDS \\
	/** The file where the users list is saved. */
	private File m_file;
	

	// GETTERS \\
	/**
	 * Gives the file where the users list is saved.
	 */
	public File getFile() {
		return m_file;
	}
	
	// SETTERS
	/**
	 * Sets the file where the history is saved.
	 * 
	 * @param p_contextDir
	 *            the current context directory
	 * @param p_file
	 *            the path to the file from the configuration
	 */
	public void setFile(String p_contextDir, String p_file) {
		File file = new File(p_file);
		if (file.isAbsolute())
			m_file = file;
		else
			m_file = new File(p_contextDir + File.separator + "WEB-INF", p_file);
	}
	
	
	// METHODS \\
	/**
	 * Gives the User with the given username.
	 * 
	 * @return a {@link User} or <code>null</code> 
	 */
	public User get(String p_username) {
		return null;
	}
	
	
	/**
	 * Loads the backend from the cache file. 
	 */
	public void loadFromFile()
	throws DeserializeException {
		
	}
	
	
	/**
	 * Saves the history to a file.
	 * Does nothing if the history is empty.
	 */
	public void saveToFile()
	throws SerializeException {
		
	}
}
