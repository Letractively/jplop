/**
 * 19 oct. 2007
 */
package tifauv.jplop.core.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tifauv.jplop.core.backend.file.persistence.DeserializeException;
import tifauv.jplop.core.backend.file.persistence.Persistable;


/**
 * The history.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class History implements Persistable {

	// CONSTANTS \\
	/** The default size of the history. */
	public static final int DEFAULT_SIZE = 50;

	/** The default cache file. */
	public static final String FILE_NAME = "history.xml";

	/** The root element of the backend. */
	private static final String BOARD_TAGNAME = "board";
	
	/** The site attribute of the backend. */
	private static final String BOARD_SITE_ATTRNAME = "site";
	
	/** The timezone attribute of the backend. */
	private static final String BOARD_TZ_ATTRNAME = "timezone";
	
	/** The format of the Last-Modified header. */
	private static final String HTTP_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";
	
	/** The date formatter using {@link #HTTP_DATE}. */
	private static final DateFormat s_httpDate = new SimpleDateFormat(HTTP_DATE);
	static {
		s_httpDate.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	
	// FIELDS \\
	/** The list of posts. */
	private LinkedList<Post> m_posts;
	
	/** The maximum size of the history. */
	private int m_maxSize;	
	
	/** The counter of post identificers. */
	private long m_idCounter;
	
	/** The URL of the backend file. */
	private String m_url;
	
	/** The date of the last modification. */
	private Date m_lastModified;
	
	/** The cached string of the history. */
	private String m_cache;
	
	/** Flag to tell whether the cache must be rewritten. */
	private boolean m_mustRewriteCache;
	
	/** The logger. */
	private Logger m_logger = Logger.getLogger(History.class);
	

	// CONSTRUCTORS \\
	/**
	 * Initializes an History with the given url.
	 * The size of the history is set to {@link #DEFAULT_SIZE}.
	 */
	public History(String p_url) {
		this(p_url, DEFAULT_SIZE);
	}
	
	
	/**
	 * Initializes an History with the given url and size.
	 */
	public History(String p_url, int p_size) {
		m_posts = new LinkedList<Post>();
		m_mustRewriteCache = true;
		setURL(p_url);
		setMaxSize(p_size);
		setModified();
		m_logger.info("History ready.");
	}
	

	// GETTERS \\
	/**
	 * Gives the next post identifier.
	 */
	private synchronized long getNextId() {
		return m_idCounter++;
	}
	
	
	/**
	 * Gives the history's URL.
	 */
	public String getURL() {
		return m_url;
	}

	
	/**
	 * Gives the file where the history is saved.
	 */
	public String getFilename() {
		return FILE_NAME;
	}
	
	
	/**
	 * Gives the date of the last modification.
	 * 
	 * @return the date of the last modification of the history
	 */
	public String getLastModified() {
		return s_httpDate.format(m_lastModified);
	}
	
	
	/**
	 * Tests whether the history has been modified since te given date.
	 * 
	 * @param p_date
	 *            the reference date
	 * 
	 * @return <code>true</code> if the history has been modified,
	 *         <code>false</code> otherwise
	 */
	public synchronized boolean isModifiedSince(String p_date) {
		if (p_date == null)
			return true;
		
		try {
			return m_lastModified.after(s_httpDate.parse(p_date));
		} catch (ParseException e) {
			return true;
		}
	}
	
	
	/**
	 * Gives the timezone value to put in the backend.
	 * The timezone format is <tt>UTC([+-]hhmm)?</tt>.
	 * If the timezone is UTC, no offset will be added.
	 */
	public static String getTimezone() {
		String tz = "UTC";
		Date now = new Date();
		int offset = TimeZone.getDefault().getOffset(now.getTime());
		if (offset != 0) {
			// Add the sign
			if (offset > 0)
				tz += '+';
			else {
				tz += '-';
				offset = -offset;
			}

			// Add the hours
			int hours = offset / 3600000; // 1h = 60 m * 60 s * 1000 ms
			if (hours < 10)
				tz += '0';
			tz += Integer.toString(hours);

			// Add the minutes
			int minutes = (offset % 3600000) / 60000; // 1m = 60s * 1000 ms
			if (minutes < 10)
				tz += '0';
			tz += Integer.toString(minutes);
		}
		return tz;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the next post identifier.
	 */
	public void setNextId(long p_id) {
		m_idCounter = p_id;
	}
	
	
	/**
	 * Sets the history's URL.
	 * 
	 * @param p_url
	 *            the history's URL
	 */
	public void setURL(String p_url) {
		m_url = p_url;
		setModified();
	}
	
	
	/**
	 * Sets the history's max size. If the new size
	 * is less that the current size, the oldest posts
	 * are removed to apply the new capacity.
	 * 
	 * @param p_size
	 *            the new size of the history
	 * 
	 * @see #truncate()
	 */
	public synchronized void setMaxSize(int p_size) {
		m_maxSize = p_size;
		truncate();
	}
	
	
	/**
	 * Sets the GMT date of the last modification to now,
	 * and toggles the rewrite cache flag.
	 */
	private void setModified() {
		Calendar now = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		now.set(Calendar.MILLISECOND, 0);
		m_lastModified = now.getTime();
		m_mustRewriteCache = true;
	}
	

	// METHODS \\
	/**
	 * Builds a Post and adds it to the history.
	 * 
	 * @param p_info
	 *            the user-agent
	 * @param p_message
	 *            the message
	 * @param p_login
	 *            the poster's login
	 * 
	 * @return the id of the new post
	 */
	public synchronized long addMessage(String p_info, String p_message, String p_login) {
		long id = getNextId();
		addPost(new Post(id, p_info, p_message, p_login));
		return id;
	}
	
	
	/**
	 * Adds a post to the history.
	 * 
	 * @param p_post
	 *            the post to add
	 * 
	 * @see #setModified()
	 * @see #truncate()
	 */
	public synchronized void addPost(Post p_post) {
		m_posts.addFirst(p_post);
		m_logger.info("Post #" + p_post.getId() + " added.");
		setModified();
		truncate();
	}

	
	/**
	 * Removes the old posts until the size of the history is at most the maximum size.
	 */
	private void truncate() {
		if (size() > maxSize() && size() > 0) {
			m_logger.info("Truncating history :");
			do {
				Post removed = m_posts.removeLast();
				m_logger.info("  - post #" + removed.getId() + " removed");
			} while (size() > maxSize() && size() > 0);
		}
	}

	
	/**
	 * Gives the maximum size of the history.
	 */
	public int maxSize() {
		return m_maxSize;
	}
	
	
	/**
	 * Gives the number of posts in the history.
	 */
	public int size() {
		return m_posts.size();
	}
	
	
	/**
	 * Tells whether the history is empty.
	 * 
	 * @return <code>true</code> iif size == 0
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	
	/**
	 * Loads a history from a DOM Document.
	 * 
	 * @param p_history
	 *            the DOM document
	 * 
	 * @throws BadPostException
	 *            if a <post> element cannot be loaded
	 */
	public synchronized void load(Document p_history)
	throws BadPostException {
		Element board = p_history.getDocumentElement();
		if (board != null) {
			// <post> elements
			NodeList posts = board.getElementsByTagName(Post.POST_TAGNAME);
			Post post = null;
			for (int i=posts.getLength()-1; i>=0; --i) {
				post = new Post((Element)posts.item(i));
				addPost(post);
			}
			
			// We have the past post, update the id counter
			if (post != null)
				setNextId(post.getId() + 1);
		}
	}
	
	
	/**
	 * Rewrites the cached XML representation of the history.
	 */
	private void updateCache() {
		if (m_mustRewriteCache) {
			m_logger.info("Updating the history cache...");
			StringBuffer buffer = new StringBuffer();
			buffer.append("<?xml-stylesheet type=\"text/xsl\" href=\"web.xslt\"?>\n");
			buffer.append("<").append(BOARD_TAGNAME).append(" ")
				.append(BOARD_SITE_ATTRNAME).append("=\"").append(getURL()).append("\" ")
				.append(BOARD_TZ_ATTRNAME).append("=\"").append(getTimezone()).append("\">\n");
			for (Post post : m_posts)
				buffer.append(post.toString());
			buffer.append("</").append(BOARD_TAGNAME).append(">");
			m_cache = buffer.toString();
			m_mustRewriteCache = false;
			m_logger.info("  done.");
		}
		else
			m_logger.debug("The history cache is up-to-date.");
	}
	
	
	/**
	 * Gives the XML representation of the history.
	 */
	@Override
	public synchronized String toString() {
		updateCache();
		return m_cache;
	}

	
	/**
	 * Loads the backend from the cache file.
	 */
	public synchronized void loadFromFile(File p_file)
	throws DeserializeException {
		if (p_file != null && p_file.exists()) {
			m_logger.info("Loading the history from '" + p_file + "'...");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				load(builder.parse(p_file));
				m_logger.info(size() + " posts loaded.");
			} catch (Exception e) {
				throw new DeserializeException("Could not load the history file", e);
			}
		}
		else
			m_logger.debug("The history file does not exist.");
	}
	
	
	/**
	 * Saves the history to a file.
	 * Does nothing if the history is empty.
	 */
	public synchronized void saveToFile(File p_file) {
		if (isEmpty() || p_file == null)
			return;
		
		// Create the file if needed
		if (!p_file.exists()) {
			try {
				p_file.createNewFile();
				m_logger.info("The file '" + p_file + "' has been created (empty).");
			} catch (IOException e) {
				m_logger.error("The file '" + p_file + "' could not be created.");
			}
		}

		// Check whether the file is writable
		if (!p_file.canWrite()) {
			m_logger.error("The history file '" + p_file + "' is not writable.");
			return;
		}
		
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(p_file);
			output.write(toString().getBytes("UTF-8"));
			m_logger.info("History saved to '" + p_file + "'.");
		} catch (FileNotFoundException e) {
			m_logger.error("The history file does not exist.");
		} catch (IOException e) {
			m_logger.error("Cannot write the history file", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					m_logger.error("An error occured while closing the history file.");
				}
			}
		}
	}
}
