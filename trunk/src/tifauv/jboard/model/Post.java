/**
 * 19 oct. 07
 */
package tifauv.jboard.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 *
 * @version 0.1
 *
 * @author Olivier Serve <olivier.serve@bull.net>
 */
public class Post {

	// CONSTANTS \\
	/** The format of a time attribute. */
	private static final String TIME_FORMAT = "yyyyMMddhhmmss";
	
	private static final String POST_TAGNAME = "post";
	
	private static final String POST_ID_ATTRNAME = "id";
	
	private static final String POST_TIME_ATTRNAME = "time";
	
	private static final String INFO_TAGNAME = "info";
	
	private static final String MESSAGE_TAGNAME = "message";
	
	private static final String LOGIN_TAGNAME = "login";
	
	private static final String CDATA_START = "<![CDATA[";
	
	private static final String CDATA_END = "]]>";
	
	private static final String ANONYMOUS_LOGIN = "Anonymous";

	
	// STATIC FIELDS \\
	/** The time formatter. */
	private static final DateFormat s_timeFormat = new SimpleDateFormat(TIME_FORMAT);
	
	/** The counter of post identificers. */
	private static long s_idCounter = 0;
	
	
	// FIELDS \\
	/** The date the post was received by the server. */
	private Date m_datetime;
	
	/** The post's unique identifier. */
	private long m_id;
	
	/** The message's author (login or UA). */
	private String m_info;
	
	/** The author's login. */
	private String m_login;
	
	/** The message. */
	private String m_message;
	

	// CONSTRUCTORS \\
	public Post(String p_info, String p_message) {
		this(p_info, p_message, ANONYMOUS_LOGIN);
	}
	

	public Post(String p_info, String p_message, String p_login) {
		setId(nextId());
		setTime(new Date());
		setInfo(p_info);
		setMessage(p_message);
		setLogin(p_login);
	}
	

	// GETTERS \\
	public final long getId() {
		return m_id;
	}

	private final Date getTime() {
		return m_datetime;
	}
	
	public final String getFormattedTime() {
		return s_timeFormat.format(getTime());
	}

	public final String getInfo() {
		return m_info;
	}
	
	public final String getLogin() {
		return m_login;
	}
	
	public final String getMessage() {
		return m_message;
	}
	
	
	// SETTERS \\
	private final void setId(long p_id) {
		m_id = p_id;
	}
	
	private final void setTime(Date p_time) {
		m_datetime = p_time;
	}
	
	private final void setInfo(String p_info) {
		m_info = p_info;
	}
	
	private final void setLogin(String p_login) {
		m_login = p_login;
	}
	
	private final void setMessage(String p_message) {
		m_message = cleanMessage(p_message);
	}

	// METHODS \\
	private static synchronized long nextId() {
		return s_idCounter++;
	}
	
	private final String cleanMessage(String p_message) {
		return p_message.replaceAll("]]>", "]]&gt;");
	}
	
	@Override
	public final String toString() {
		return "<" + POST_TAGNAME + " " + POST_ID_ATTRNAME + "=\"" + getId() + "\" " + POST_TIME_ATTRNAME + "=\"" + getFormattedTime() + "\">"
		+ "<" + INFO_TAGNAME + ">" + CDATA_START + getInfo() + CDATA_END + "</" + INFO_TAGNAME + ">"
		+ "<" + MESSAGE_TAGNAME + ">" + CDATA_START + getMessage() + CDATA_END + "</" + MESSAGE_TAGNAME + ">"
		+ "<" + LOGIN_TAGNAME + ">" + CDATA_START + getLogin() + CDATA_END + "</" + LOGIN_TAGNAME + ">"
		+ "</" + POST_TAGNAME + ">";
	}
}
