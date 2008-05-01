package tifauv.jplop.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import tifauv.jplop.util.AbstractJob;

public class PersistenceManager {
	
	// FIELDS \\
	/** The data directory. */
	private File m_dataDir;
	
	/** The list of persistent objects. */
	private List<Persistable> m_objects;
	
	/** The backup job. */
	private BackupJob m_job;
	
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(PersistenceManager.class);

	
	// CONSTRUCTORS \\
	/**
	 * Default constructor creates the list of objects.
	 */
	public PersistenceManager() {
		m_objects = new ArrayList<Persistable>(2);
		m_job = new BackupJob(this);
	}
	
	
	// GETTERS \\
	/**
	 * Gives the data directory.
	 */
	public final File getDataDir() {
		return m_dataDir;
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
	
	
	/**
	 * Sets the duration between two backups of the objects.
	 * 
	 * @param p_timeout
	 *            the timeout in minutes
	 */
	public final void setTimeout(int p_timeout) {
		m_job.setFrequency(p_timeout * 60000);
	}

	
	// METHODS \\
	/**
	 * Adds the object to the list of watched objects.
	 * This method silently ignores <tt>null</tt> objects.
	 * 
	 * @param p_object
	 *            the object to watch
	 */
	public final void register(Persistable p_object)
	throws DeserializeException {
		if (p_object != null && !m_objects.contains(p_object)) {
			p_object.loadFromFile(getFile(p_object));
			m_objects.add(p_object);
			m_logger.info("Object [" + p_object.getClass().getName() + "] registered.");
		}
	}
	
	
	/**
	 * Removes all the objects from the list of watched objects.
	 */
	public final void unregisterAll() {
		m_logger.info("======[ Unregistering the persistent objects ]======");
		Persistable object;
		Iterator<Persistable> iter = m_objects.iterator();
		while (iter.hasNext()) {
			object = iter.next();
			iter.remove();
			m_logger.info("Object [" + object.getClass().getName() + "] unregistered.");
			object = null;
		}
		m_logger.info("=========[ Persistent objects unregistered ]========");
	}
	
	
	/**
	 * Saves all the objects' state (through {@link Persistable#saveToFile(File)}).
	 */
	public final void saveAll() {
		m_logger.info("======[ Saving the persistent objects ]======");
		for (Persistable object : m_objects) {
			try {
				object.saveToFile(getFile(object));
			} catch (SerializeException e) {
				m_logger.error("Failed to save [" + object.getClass().getName() + "]", e);
			}
		}
		m_logger.info("=========[ Persistent objects saved ]========");
	}
	
	
	/**
	 * Gives the file of a watched object.
	 * 
	 * @param p_object
	 *            the object for which we want the file
	 * 
	 * @return the file that contains the state of the object
	 */
	private final File getFile(Persistable p_object) {
		return new File(getDataDir(), p_object.getFilename());
	}
	
	
	/**
	 * Starts the backup job.
	 */
	public final void startBackupJob() {
		m_logger.debug("Starting the backup job...");
		m_job.start();
		m_logger.debug("...backup job started.");
	}
	
	
	/**
	 * Stops the backup job.
	 */
	private final void stopBackupJob() {
		m_logger.debug("Stopping the backup job...");
		m_job.stop();
		m_logger.info("...backup job stopped.");
	}
	
	
	/**
	 * Stops the backup job, saves all the objects then unregisters them.
	 * 
	 * @see #stopBackupJob()
	 * @see #saveAll()
	 * @see #unregisterAll()
	 */
	public final void clean() {
		m_logger.info("Cleaning the persistence manager...");
		stopBackupJob();
		saveAll();
		unregisterAll();
		m_logger.debug("...persistence manager cleaned.");
	}
}


/**
 * This job regularly saves the registered persistent objects.
 * 
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
final class BackupJob extends AbstractJob {
	
	// CONSTANTS \\
	/** The job's name. */
	public static final String JOB_NAME = "Backup";
	
	
	// FIELDS \\
	/** The persistence manager. */
	private PersistenceManager m_persistence;
	
	/** The logger. */
	public final Logger m_logger = Logger.getLogger(BackupJob.class);

	
	// CONSTRUCTORS \\
	/**
	 * Default constructor sets the job's name.
	 * 
	 * @see tifauv.jplop.util.AbstractJob#AbstractJob(String)
	 */
	public BackupJob(PersistenceManager p_manager) {
		super(JOB_NAME);
		m_persistence = p_manager;
	}
	
	
	// METHODS \\
	/**
	 * Calls {@link Backend#saveToDisk()}.
	 * 
	 * @see tifauv.jplop.util.AbstractJob#doWork()
	 */
	@Override
	protected void doWork() {
		m_logger.info("Auto-backup started.");
		m_persistence.saveAll();
		m_logger.info("Auto-backup finished.");
	}
}
