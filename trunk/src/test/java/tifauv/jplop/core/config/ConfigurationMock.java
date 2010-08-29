/**
 * 29 août 2010
 */
package tifauv.jplop.core.config;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class ConfigurationMock implements Configuration {
	
	// FIELDS \\
	private String m_contextDir = null;
	private String m_name = "JPlop";
	private String m_fullName = "Da J2EE tribioune";
	private String m_url = "http://localhost:8080/jplop";
	private int m_maxSize = 50;
	private int m_autosaveInterval = 5 * 60;
	private int m_maxPostLength = 512;
	private String m_storageFactory = "tifauv.jplop.core.storage.file.FileStorageFactory";


	// GETTERS \\
	@Override
	public String getContextDir() {
		return m_contextDir;
	}

	@Override
	public String getName() {
		return m_name;
	}

	@Override
	public String getFullName() {
		return m_fullName;
	}

	@Override
	public String getURL() {
		return m_url;
	}

	@Override
	public int getMaxSize() {
		return m_maxSize;
	}

	@Override
	public int getAutoSaveInterval() {
		return m_autosaveInterval;
	}

	@Override
	public int getMaxPostLength() {
		return m_maxPostLength;
	}

	@Override
	public String getStorageFactoryName() {
		return m_storageFactory;
	}

	@Override
	public String getString(String p_key) {
		return null;
	}

	@Override
	public int getInt(String p_key) {
		return 0;
	}

	
	// SETTERS \\
	public void setContextDir(String p_contextDir) {
		m_contextDir = p_contextDir;
	}

	public void setName(String p_name) {
		m_name = p_name;
	}

	public void setFullName(String p_fullName) {
		m_fullName = p_fullName;
	}

	public void setURL(String p_url) {
		m_url = p_url;
	}

	public void setMaxSize(int p_size) {
		m_maxSize = p_size;
	}

	public void setAutoSaveInterval(int p_save) {
		m_autosaveInterval = p_save;
	}

	public void setMaxPostLength(int p_postLength) {
		m_maxPostLength = p_postLength;
	}

	public void setStorageFactoryName(String p_factory) {
		m_storageFactory = p_factory;
	}
	
	
	// METHODS \\
	@Override
	public void load(String p_contextDir) {
		// do nothing
	}
}
