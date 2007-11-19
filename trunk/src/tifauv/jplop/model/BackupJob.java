/**
 * 
 */
package tifauv.jplop.model;

import tifauv.jplop.util.AbstractJob;

/**
 * This job asks regularly the {@link #Backend} to save itself.
 * 
 * @version 1.0
 * 
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class BackupJob extends AbstractJob {
	
	// CONSTANTS \\
	/** The job's name. */
	public static final String JOB_NAME = "Backup";

	
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
	 * Calls {@link Backend#saveToCache()}.
	 * 
	 * @see tifauv.jplop.util.AbstractJob#doWork()
	 */
	@Override
	protected void doWork() {
		Backend.getInstance().saveToCache();
	}
}
