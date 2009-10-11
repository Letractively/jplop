/**
 * 19 nov. 2007
 */
package tifauv.jplop.core.util;

import org.apache.log4j.Logger;

/**
 * Base class for jobs.
 * A Job is a thread that runs aside the main 
 * 
 * @version 1.0
 * 
 * @author Tifauv' <tifauv@gmail.com>
 */
public abstract class AbstractJob implements Runnable {

	// CONSTANTS \\
	/** Default frequency is 5 minutes (5 * 60 * 1000 = 300000ms). */
	public static final int DEFAULT_FREQUENCY = 300000;
	
	/** The default name of a job. */
	public static final String DEFAULT_NAME = "JPlop job";
	
	
	// FIELDS \\
	/** The logger. */
	private Logger m_logger = Logger.getLogger(AbstractJob.class);
	
	/** The job's name. */
	private String m_jobName;
	
	/** The thread. */
	private Thread m_runner;
	
	/** The stop flag. */
	private boolean m_stop;
	
	/** The wake-up timeout. */
	private long m_frequency;
	
	
	// CONSTRUCTORS \\
	/**
	 * Default constructor initializes the frequency to {@link #DEFAULT_FREQUENCY}
	 * and the job's name to {@link #DEFAULT_NAME}.
	 */
	public AbstractJob() {
		m_stop = true;
		setJobName(DEFAULT_NAME);
		setFrequency(DEFAULT_FREQUENCY);
	}
	
	
	/**
	 * Default constructor initializes the frequency to {@link #DEFAULT_FREQUENCY}.
	 */
	public AbstractJob(String p_name) {
		m_stop = true;
		setJobName(p_name);
		setFrequency(DEFAULT_FREQUENCY);
	}
	
	
	// GETTERS \\
	/**
	 * Gives the time (in ms) between two wake-ups.
	 * 
	 * @see #setFrequency(long)
	 */
	public final long getFrequency() {
		return m_frequency;
	}
	
	
	/**
	 * Gives the job's name (if any).
	 * 
	 * @see #setJobName(String)
	 */
	public final String getJobName() {
		return m_jobName;
	}
	
	
	/**
	 * Tells whether the job is stopped.
	 */
	public final boolean isStopped() {
		return m_stop;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the time between two wake-ups.
	 * 
	 * @param p_frequency
	 *            the frequency in milliseconds
	 * 
	 * @see #getFrequency()
	 */
	public final void setFrequency(long p_frequency) {
		m_frequency = p_frequency;
		m_logger.info("The Job [" + getJobName() + "] will wake up every " + (m_frequency/60000) + " minutes .");
	}
	
	
	/**
	 * Sets the job name.
	 * 
	 * @param p_jobName
	 *            the job name
	 * 
	 * @see #getJobName()
	 */
	protected synchronized void setJobName(String p_jobName) {
		String temp = DEFAULT_NAME;
		if (p_jobName != null)
			temp = p_jobName;
		if (m_runner != null)
			m_runner.setName(temp);
		if (getJobName() == null)
			m_logger.info("A new job known as [" + temp + "] has been created.");
		else
			m_logger.info("The job [" + getJobName() + "] is now known as [" + temp + "].");
		m_jobName = temp;
	}
	
	
	// METHODS \\
	/**
	 * Starts the job.
	 * 
	 * @see #stop()
	 */
	public final synchronized void start() {
		if (m_runner == null) {
			m_runner = new Thread(this, getJobName());
			m_stop = false;
			m_logger.info("Starting job [" + getJobName() + "]...");
			m_runner.start();
		}
	}
	
	
	/**
	 * Stops the job.
	 * 
	 * @see #start()
	 */
	public final synchronized void stop() {
		if (m_runner != null) {
			if (m_runner.isAlive()) {
				m_logger.info("Stopping job [" + getJobName() + "]...");
				m_stop = true;
				m_runner.interrupt();
				try {
					m_runner.join();
				} catch (InterruptedException e) {
					// Don't mind
				}
			}
			
			// Cleanup runner reference and flag
			forcedCleanup();
		}
	}
	
	
	/**
	 * The executed method. Subclasses MUST NOT override this method.
	 * The job's stuff is to be put in {@link #doWork()}.
	 * Note that the job will start by sleeping before executing the
	 * first time.
	 * 
	 * @see #init()
	 * @see #doWork()
	 * @see #cleanup()
	 */
	public final void run() {
		m_logger.info("Job [" + getJobName() + "] started.");
		init();

		while (!isStopped()) {
			try {
				Thread.sleep(getFrequency());
			} catch (InterruptedException e) {
				// Don't mind
			}
			doWork();
		}
		
		cleanup();
		forcedCleanup();
		m_logger.info("Job [" + getJobName() + "] stopped.");
	}
	
	
	/**
	 * This methods resets the stop flag and the Thread reference.
	 */
	private final void forcedCleanup() {
		m_stop = true;
		m_runner = null;
	}
	
	
	/**
	 * Initializes the job before executing the main work.
	 * The default implementation does nothing.
	 */
	protected void init() {
		// Default implementation does nothing
	}

	
	/**
	 * This method does the real work.
	 */
	protected abstract void doWork();
	
	
	/**
	 * Cleans the job before leaving.
	 * The default implementation does nothing.
	 */
	protected void cleanup() {
		// Default implementation does nothing
	}
}
