package tifauv.jplop.core;

import org.apache.log4j.Logger;

import tifauv.jplop.core.auth.UserBase;
import tifauv.jplop.core.board.History;
import tifauv.jplop.core.config.Configuration;
import tifauv.jplop.core.config.PropertiesConfiguration;
import tifauv.jplop.core.storage.StorageException;
import tifauv.jplop.core.storage.StorageManager;
import tifauv.jplop.core.storage.StorageManagerImpl;

public final class BackendImpl implements Backend {

	// FIELDS \\
	/** The board's configuration. */
	private Configuration m_config;
	
	/** The board's history. */
	private History m_history;
	
	/** The user base.*/
	private UserBase m_users;
	
	/** The persistence manager. */
	private StorageManager m_storage;
	

	// CONSTRUCTORS \\
	public BackendImpl() {
		m_config = new PropertiesConfiguration();
		m_history = new History();
		m_users = new UserBase();
		m_storage = new StorageManagerImpl();
	}
	
	
	// GETTERS \\
	/**
	 * Gives the configuration.
	 */
	@Override
	public Configuration getConfig() {
		return m_config;
	}
	
	
	/**
	 * Gives the history.
	 */
	@Override
	public History getHistory() {
		return m_history;
	}
	
	
	/**
	 * Gives the user base.
	 */
	@Override
	public UserBase getUserBase() {
		return m_users;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the backend's history.
	 * 
	 * @param p_history
	 *            the history
	 */
	public void setHistory(History p_history) {
		m_history = p_history;
	}
	
	
	/**
	 * Sets the user base.
	 * 
	 * @param p_userBase
	 *            the user base
	 */
	public void setUserBase(UserBase p_userBase) {
		m_users = p_userBase;
	}
	
	
	// METHODS \\
	/**
	 * Loads the configuration and initializes the storage manager.
	 * 
	 * @param p_contextDir
	 *            the context directory of the webapp
	 */
	@Override
	public void init(String p_contextDir) {
		m_config.load(p_contextDir);
		try {
			m_storage.init();
			m_storage.attach(m_history);
			m_storage.attach(m_users);
			m_storage.ready();
		} catch (StorageException e) {
			Logger.getLogger(BackendImpl.class).error("Could not load from storage", e);
		}
	}
	
	
	/**
	 * Stops the storage manager.
	 */
	@Override
	public void clean() {
		m_storage.clean();
	}
}
