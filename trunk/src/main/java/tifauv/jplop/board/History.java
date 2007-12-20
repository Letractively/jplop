/**
 * 19 oct. 2007
 */
package tifauv.jplop.board;

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

import tifauv.jplop.util.DeserializeException;
import tifauv.jplop.util.Serializable;


/**
 * The history.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class History extends Serializable {

	// CONSTANTS \\
	/** The default size of the history. */
	public static final int DEFAULT_SIZE = 50;

	/** The default cache file. */
	public static final String FILE_NAME = "history.xml";

	/** The root element of the backend. */
	private static final String BOARD_TAGNAME = "board";
	
	/** The site attribute of the backend. */
	private static final String BOARD_SITE_ATTRNAME = "site";
	
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
	public final String getURL() {
		return m_url;
	}

	
	/**
	 * Gives the file where the history is saved.
	 */
	@Override
	public final File getFile() {
		return new File(getDataDir(), FILE_NAME);
	}
	
	
	/**
	 * Gives the date of the last modification.
	 * 
	 * @return the date of the last modification of the history
	 */
	public final String getLastModified() {
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
	public final synchronized boolean isModifiedSince(String p_date) {
		if (p_date == null)
			return true;
		
		try {
			return m_lastModified.after(s_httpDate.parse(p_date));
		}
		catch (ParseException e) {
			return true;
		}
	}
	
	
	// SETTERS \\
	/**
	 * Sets the next post identifier.
	 */
	private synchronized void setNextId(long p_id) {
		m_idCounter = p_id;
	}
	
	
	/**
	 * Sets the history's URL.
	 * 
	 * @param p_url
	 *            the history's URL
	 */
	public final void setURL(String p_url) {
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
	public final synchronized void setMaxSize(int p_size) {
		m_maxSize = p_size;
		truncate();
	}
	
	
	/**
	 * Sets the GMT date of the last modification to now,
	 * and toggles the rewrite cache flag.
	 */
	private final void setModified() {
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
	 */
	public final synchronized void addMessage(String p_info, String p_message, String p_login) {
		addPost(new Post(getNextId(), p_info, p_message, p_login));
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
	protected final synchronized void addPost(Post p_post) {
		m_posts.addFirst(p_post);
		m_logger.info("New post added.");
		setModified();
		truncate();
	}

	
	/**
	 * Removes the old posts until the size of the history is at most the maximum size.
	 */
	private final void truncate() {
		m_logger.info("Truncating history :");
		while (size() > maxSize()) {
			Post removed = m_posts.removeLast();
			m_logger.info("  - post #" + removed.getId() + " removed");
		}
		m_logger.info("  - done.");
	}

	
	/**
	 * Gives the maximum size of the history.
	 */
	public final synchronized int maxSize() {
		return m_maxSize;
	}
	
	
	/**
	 * Gives the number of posts in the history.
	 */
	public final synchronized int size() {
		return m_posts.size();
	}
	
	
	/**
	 * Tells whether the history is empty.
	 * 
	 * @return <code>true</code> iif size == 0
	 */
	public final boolean isEmpty() {
		return size() == 0;
	}
	
	
	/**
	 * Loads a history from a DOM Document.
	 * 
	 * @param p_history
	 *            the DOM document
	 * 
	 * @throws ParseException
	 *            if a post time attribute cannot be loaded
	 * @throws NumberFormatException
	 *            if a post id cannot be loaded
	 */
	public final void load(Document p_history)
	throws ParseException,
	NumberFormatException {
		Element board = p_history.getDocumentElement();
		if (board != null) {
			// site attribute
			if (board.hasAttribute(BOARD_SITE_ATTRNAME))
				setURL(board.getAttribute(BOARD_SITE_ATTRNAME));
			
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
	private final void updateCache() {
		if (m_mustRewriteCache) {
			m_logger.info("Updating the history cache...");
			StringBuffer buffer = new StringBuffer();
			buffer.append("<?xml-stylesheet type=\"text/xsl\" href=\"web.xslt\"?>\n");
			buffer.append("<").append(BOARD_TAGNAME).append(" ")
				.append(BOARD_SITE_ATTRNAME).append("=\"").append(getURL()).append("\">\n");
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
	public final synchronized String toString() {
		updateCache();
		return m_cache;
	}

	
	/**
	 * Loads the backend from the cache file.
	 */
	@Override
	public synchronized final void loadFromFile()
	throws DeserializeException {
		if (getFile().exists()) {
			m_logger.info("Loading the history from '" + getFile() + "'...");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				load(builder.parse(getFile()));
				m_logger.info(size() + " posts loaded from file.");
			}
			catch (Exception e) {
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
	@Override
	public synchronized final void saveToFile() {
		if (isEmpty())
			return;
		
		// Create the file if needed
		if (!getFile().exists()) {
			try {
				getFile().createNewFile();
				m_logger.info("The file '" + getFile() + "' has been created (empty).");
			}
			catch (IOException e) {
				m_logger.error("The file '" + getFile() + "' could not be created.");
			}
		}

		// Check whether the file is writable
		if (!getFile().canWrite()) {
			m_logger.error("The history file '" + getFile() + "' is not writable.");
			return;
		}
		
		try {
			FileOutputStream output = new FileOutputStream(getFile());
			output.write(toString().getBytes("UTF-8"));
			output.close();
			m_logger.info("History saved to '" + getFile() + "'.");
		}
		catch (FileNotFoundException e) {
			m_logger.error("The history file does not exist.");
		}
		catch (IOException e) {
			m_logger.error("Cannot write the history file", e);
		}
	}
}
