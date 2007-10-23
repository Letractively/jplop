/**
 * 19 oct. 07
 */
package tifauv.jboard.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * 
 *
 * @version 0.1
 *
 * @author Olivier Serve <olivier.serve@bull.net>
 */
public class History {

	// CONSTANTS \\
	/** The default size of the history. */
	public static final int DEFAULT_SIZE = 50;
	
	private static final String BOARD_TAGNAME = "board";
	
	private static final String BOARD_SITE_ATTRNAME = "site";
	
	private static final String HTTP_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";
	
	private static final DateFormat s_httpDate = new SimpleDateFormat(HTTP_DATE);
	
	// FIELDS \\
	/** The list of posts. */
	private LinkedList<Post> m_posts;
	
	/** The maximum size of the history. */
	private int m_maxSize;	
	
	/** The URL of the backend file. */
	private String m_url;
	
	/** The date of the last modification. */
	private Date m_lastModified;
	

	// CONSTRUCTORS \\
	public History(String p_url) {
		this(p_url, DEFAULT_SIZE);
	}
	
	
	public History(String p_url, int p_size) {
		m_posts = new LinkedList<Post>();
		setURL(p_url);
		setMaxSize(p_size);
		setLastModified();
	}
	

	// GETTERS \\
	protected final String getURL() {
		return m_url;
	}
	
	public final String getLastModified() {
		return s_httpDate.format(m_lastModified);
	}
	
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
	protected final void setURL(String p_url) {
		m_url = p_url;
	}
	
	protected final synchronized void setMaxSize(int p_size) {
		m_maxSize = p_size;
		truncate();
	}
	
	private final void setLastModified() {
		m_lastModified = new Date();
	}
	

	// METHODS \\
	public final synchronized void addPost(Post p_post) {
		m_posts.addFirst(p_post);
		setLastModified();
		truncate();
	}
	
	private final void truncate() {
		while (size() > maxSize())
			m_posts.removeLast();
	}
	
	public final synchronized int maxSize() {
		return m_maxSize;
	}
	
	public final synchronized int size() {
		return m_posts.size();
	}
	
	@Override
	public final synchronized String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml-stylesheet type=\"text/xsl\" href=\"web.xslt\"?>\n");
		buffer.append("<" + BOARD_TAGNAME + " " + BOARD_SITE_ATTRNAME + "=\"" + getURL() + "\">\n");
		for (Post post : m_posts)
			buffer.append(post.toString());
		buffer.append("</" + BOARD_TAGNAME + ">");
		
		return buffer.toString();
	}
}
