/**
 * 20 nov. 2007
 */
package tifauv.jplop;

import org.apache.log4j.Logger;

import tifauv.jplop.util.AbstractJob;
import tifauv.jplop.util.SerializeException;

/**
 * This job asks regularly the {@link Backend} to save itself.
 * 
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class BackupJob extends AbstractJob {
	
	// CONSTANTS \\
	/** The job's name. */
	public static final String JOB_NAME = "Backup";
	
	
	// FIELDS \\
	/** The logger. */
	public Logger m_logger = Logger.getLogger(BackupJob.class);

	
	// CONSTRUCTORS \\
	/**
	 * Default constructor sets the job's name.
	 * 
	 * @see tifauv.jplop.util.AbstractJob#AbstractJob(String)
	 */
	public BackupJob() {
		super(JOB_NAME);
	}
	
	
	// METHODS \\
	/**
	 * Calls {@link Backend#saveToDisk()}.
	 * 
	 * @see tifauv.jplop.util.AbstractJob#doWork()
	 */
	@Override
	protected void doWork() {
		m_logger.info("Auto-saving the backend...");
		try {
			Backend.getInstance().saveToDisk();
		} catch (SerializeException e) {
			m_logger.error("The backend auto-saving failed", e);
		}
	}
}
