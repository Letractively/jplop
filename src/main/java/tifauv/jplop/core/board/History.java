/**
 * 19 oct. 2007
 */
package tifauv.jplop.core.board;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tifauv.jplop.core.Main;


/**
 * The history.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public final class History {

	// CONSTANTS \\
	/** The default size of the history. */
	public static final int DEFAULT_SIZE = 50;

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
	
	/** The counter of post identificers. */
	private long m_idCounter;
	
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
	public History() {
		m_posts = new LinkedList<Post>();
		m_mustRewriteCache = true;
		setModified();
		m_logger.debug("History ready.");
	}
	

	// GETTERS \\
	/**
	 * Gives the next post identifier.
	 */
	private synchronized long getNextId() {
		return m_idCounter++;
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
		m_logger.debug("Post #" + p_post.getId() + " added.");
		setModified();
		truncate();
	}

	
	/**
	 * Removes the old posts until the size of the history is at most the maximum size.
	 */
	private void truncate() {
		if (size() > Main.get().getConfig().getMaxSize() && size() > 0) {
			m_logger.info("Truncating history :");
			do {
				Post removed = m_posts.removeLast();
				m_logger.info("  - post #" + removed.getId() + " removed");
			} while (size() > Main.get().getConfig().getMaxSize() && size() > 0);
		}
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
				.append(BOARD_SITE_ATTRNAME).append("=\"").append(Main.get().getConfig().getURL()).append("\" ")
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
	 * Gives the backend text if it has been modified since the given date.
	 * Otherwise, just return <code>null</code>.
	 * 
	 * @param p_modifiedSince
	 *            the Modified-Since value
	 * 
	 * @return the backend text or <code>null</code>
	 */
	public synchronized String toString(String p_modifiedSince) {
		if (isModifiedSince(p_modifiedSince))
			return toString();
		return null;
	}
}
