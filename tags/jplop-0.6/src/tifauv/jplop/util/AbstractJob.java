package tifauv.jplop.util;

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
	/** Default timeout is 5 minutes (5 * 60 * 1000 = 300000ms). */
	public static final int DEFAULT_TIMEOUT = 300000;
	
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
	private long m_timeout;
	
	
	// CONSTRUCTORS \\
	/**
	 * Default constructor initializes the timeout to {@link #DEFAULT_TIMEOUT}
	 * and the job's name to {@link #DEFAULT_NAME}.
	 */
	public AbstractJob() {
		m_stop = true;
		setTimeout(DEFAULT_TIMEOUT);
		setJobName(DEFAULT_NAME);
	}
	
	
	/**
	 * Default constructor initializes the timeout to {@link #DEFAULT_TIMEOUT}.
	 */
	public AbstractJob(String p_name) {
		m_stop = true;
		setTimeout(DEFAULT_TIMEOUT);
		setJobName(p_name);
	}
	
	
	// GETTERS \\
	/**
	 * Gives the time (in ms) between two wake-ups.
	 * 
	 * @see #setTimeout(long)
	 */
	public final long getTimeout() {
		return m_timeout;
	}
	
	
	/**
	 * Tells whether the job is stopped.
	 */
	public final boolean isStopped() {
		return m_stop;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the time (in ms) between two wake-ups.
	 * 
	 * @see #getTimeout()
	 */
	public final void setTimeout(long p_timeout) {
		m_timeout = p_timeout;
	}
	
	
	/**
	 * Sets the job name.
	 * 
	 * @param p_jobName
	 *            the job name
	 */
	protected synchronized void setJobName(String p_jobName) {
		String temp = DEFAULT_NAME;
		if (p_jobName == null)
			temp = p_jobName;
		if (m_runner != null)
			m_runner.setName(temp);
		m_jobName = temp;
	}
	
	
	// METHODS \\
	/**
	 * Starts the job.
	 * 
	 * @see #start(String)
	 * @see #stop()
	 */
	public final synchronized void start() {
		if (m_runner == null) {
			m_runner = new Thread(this, m_jobName);
			m_stop = false;
			m_logger.info("Starting job...");
			m_runner.start();
		}
	}
	
	
	/**
	 * Stops the job.
	 * 
	 * @see #start()
	 * @see #start(String)
	 */
	public final synchronized void stop() {
		if (m_runner != null) {
			if (m_runner.isAlive()) {
				m_logger.info("Stopping job...");
				m_stop = true;
				m_runner.interrupt();
				try {
					m_runner.wait();
				}
				catch (InterruptedException e) {
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
		m_logger.info("Job started.");
		init();

		while (!isStopped()) {
			try {
				Thread.sleep(getTimeout());
			}
			catch (InterruptedException e) {
				// Don't mind
			}
			doWork();
		}
		
		cleanup();
		forcedCleanup();
		m_logger.info("Job stopped.");
	}
	
	
	/**
	 * This methods resets the stop flag and the Thread reference.
	 */
	private final synchronized void forcedCleanup() {
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
