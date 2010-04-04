package tifauv.jplop.core.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

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
	/** The default auto-save interval is every 5 minutes. */
	public static final int DEFAULT_AUTOSAVE_INTERVAL = 5 * 60;
	
	/** The nma of the backup job. */
	public static final String BACKUP_JOB_NAME = "Backup";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(StorageManagerImpl.class);
	
	/** The list of storage delegates. */
	private final List<StorageDelegate<?>> m_delegates;
	
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
	public void init() {
		m_backupJob = new AbstractJob() {
			@Override
			protected void init() {
				setJobName(BACKUP_JOB_NAME);
				setFrequency(DEFAULT_AUTOSAVE_INTERVAL * 1000);
			}
			
			@Override
			protected void doWork() {
				synchronized (getDelegates()) {
					for (StorageDelegate<?> delegate : getDelegates()) {
						try {
							delegate.save();
						} catch (StorageException e) {
							Logger.getLogger(StorageManagerImpl.class).error("Failed to save [" + delegate.getObject().getClass().getName() + "]", e);
						}
					}
				}
			}
		};
	}

	
	@Override
	public void attach(StorageDelegate<?> p_delegate) {
		if (p_delegate == null)
			throw new IllegalArgumentException("Null delegate given");

		synchronized (m_delegates) {
			m_delegates.add(p_delegate);
		}
	}


	@Override
	public void detach(StorageDelegate<?> p_delegate) {
		if (p_delegate == null)
			throw new IllegalArgumentException("Null delegate given");

		synchronized (m_delegates) {
			m_delegates.remove(p_delegate);
		}
	}
	
	
	public void saveAll() {
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
