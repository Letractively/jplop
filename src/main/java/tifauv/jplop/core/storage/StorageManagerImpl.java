package tifauv.jplop.core.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import tifauv.jplop.core.Main;
import tifauv.jplop.core.util.AbstractJob;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class StorageManagerImpl implements StorageManager {
	
	// CONSTANTS \\
	/** The name of the backup job. */
	private static final String BACKUP_JOB_NAME = "Backup";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(StorageManagerImpl.class);
	
	/** The list of storage delegates. */
	private final List<StorageDelegate<?>> m_delegates;
	
	/** The delegates factory. */
	private StorageFactory m_factory;
	
	/** The backup job. */
	private AbstractJob m_backupJob;

	
	// CONSTRUCTOR \\
	public StorageManagerImpl() {
		m_delegates = new ArrayList<StorageDelegate<?>>(2);
	}
	
	
	// GETTERS \\
	protected List<StorageDelegate<?>> getDelegates() {
		return m_delegates;
	}
	
	
	// SETTERS \\
	@Override
	public void setAutoSaveInterval(long p_seconds) {
		m_backupJob.setFrequency(p_seconds * 1000);
	}
	
	
	// METHODS \\
	@Override
	public void init()
	throws StorageException {
		String factoryName = Main.get().getConfig().getStorageFactoryName();
		try {
			m_factory = createFactory(factoryName);
		} catch (NoClassDefFoundError e) {
			throw new StorageException("The storage factory class '" + factoryName + "' defined in the configuration could not be found.", e);
		} catch (ClassCastException e) {
			throw new StorageException("The storage factory class '" + factoryName + "' defined in the configuration does not implement '" + StorageFactory.class.getName() + "'.", e);
		} catch (Exception e) {
			throw new StorageException("The storage factory class '" + factoryName + "' defined in the configuration could not be instanciated.", e);
		}
		
		// Set the backup job
		m_backupJob = new AbstractJob(BACKUP_JOB_NAME) {
			@Override
			protected void init() {
				setFrequency(DEFAULT_AUTOSAVE_INTERVAL * 1000);
			}
			
			@Override
			protected void doWork() {
				saveAll();
			}
		};
	}
	
	
	@Override
	public void ready() {
		loadAll();
		m_backupJob.start();
	}

	
	@Override
	public void attach(Object p_object)
	throws StorageException {
		if (p_object == null)
			throw new IllegalArgumentException("Cannot attach null");
		
		if (m_factory == null)
			throw new StorageException("No storage factory defined");
		
		StorageDelegate<?> delegate = m_factory.createDelegate(p_object);
		if (delegate == null)
			throw new StorageException("The factory does not know how to build a delegate for " + p_object.getClass().getName());

		synchronized (m_delegates) {
			m_delegates.add(delegate);
		}
	}
	
	
	private StorageFactory createFactory(String p_className)
	throws ClassNotFoundException,
	ClassCastException,
	InstantiationException,
	IllegalAccessException {
		if (p_className == null)
			return null;
		
		return Class.forName(p_className).asSubclass(StorageFactory.class).newInstance();
	}
	
	
	private void loadAll() {
		synchronized (m_delegates) {
			for (StorageDelegate<?> delegate : m_delegates) {
				try {
					delegate.load();
				} catch (StorageException e) {
					m_logger.error("Failed to load [" + delegate.getObject().getClass().getName() + "]", e);
				}
			}
		}
	}
	
	
	void saveAll() {
		synchronized (m_delegates) {
			for (StorageDelegate<?> delegate : m_delegates) {
				try {
					delegate.save();
				} catch (StorageException e) {
					m_logger.error("Failed to save [" + delegate.getObject().getClass().getName() + "]", e);
				}
			}
		}
	}
	
	
	@Override
	public void clean() {
		m_backupJob.stop();
		
		synchronized (m_delegates) {
			Iterator<StorageDelegate<?>> delegates = m_delegates.iterator();
			while (delegates.hasNext()) {
				StorageDelegate<?> delegate = delegates.next();
				try {
					delegate.save();
				} catch (StorageException e) {
					m_logger.error("Failed to save [" + delegate.getObject().getClass().getName() + "]", e);
				} finally {
					delegates.remove();
				}
			}
		}
	}
}
