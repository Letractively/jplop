/**
 * 19 oct. 07
 */
package tifauv.jboard.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A post has the following elements:
 * <ul><li>a unique identifier&nbsp; (a sequential long integer)&nbsp;;</li>
 * <li>the datetime&nbsp;;</li>
 * <li>the login of the author or Anonyme&nbsp;</li>
 * <li>the user-agent of the author&nbsp;;</li>
 * <li>the message</li></ul>
 * <p>The message accepts some HTML-like style tags (b, i, u, s, tt).
 * The message is checked for well-formness. If some tags are not closed,
 * the required close-tags are added.</p>
 * 
 * @version 1.0
 *
 * @author Olivier Serve <olivier.serve@bull.net>
 */
public class Post {

	// CONSTANTS \\
	/** The format of a time attribute. */
	private static final String TIME_FORMAT = "yyyyMMddHHmmss";
	
	/** The */
	private static final String POST_TAGNAME = "post";
	
	private static final String POST_ID_ATTRNAME = "id";
	
	private static final String POST_TIME_ATTRNAME = "time";
	
	private static final String INFO_TAGNAME = "info";
	
	private static final String MESSAGE_TAGNAME = "message";
	
	private static final String LOGIN_TAGNAME = "login";
	
	private static final String CDATA_START = "<![CDATA[";
	
	private static final String CDATA_END = "]]>";
	
	private static final String ANONYMOUS_LOGIN = "Anonymous";
	
	protected static final int DEFAULT_MAX_POST_LENGTH = 512;
	
	/** The list of allowed tags. */
	private static final String[] TAGS = { "b", "i", "u", "s", "tt" };
	
	/** The generated pattern regex for start tags. */
	private static final String STARTTAG_PATTERN;

	/** The generated pattern regex for end tags. */
	private static final String ENDTAG_PATTERN;
	
	/** Generates the start and end tags patterns. */
	static {
		String starttags = "";
		for (String tag : TAGS) {
			if (starttags.length() > 0)
				starttags += "|";
			starttags += tag;
		}
		STARTTAG_PATTERN = "<(" + starttags + ")>";
		ENDTAG_PATTERN = "</(" + starttags + ")>";
	}
	
	/** The aggregation of start and end tags. */
	private static final String PATTERN = STARTTAG_PATTERN + "|" + ENDTAG_PATTERN;

	
	// STATIC FIELDS \\
	/** The time formatter. */
	private static final DateFormat s_timeFormat = new SimpleDateFormat(TIME_FORMAT);
	
	/** The counter of post identificers. */
	private static long s_idCounter = 0;
	
	/** The maximum length of a post. */
	private static int s_maxLength = DEFAULT_MAX_POST_LENGTH;
	
	/** The tags pattern. */
	private static Pattern s_tagPattern = Pattern.compile(PATTERN);
	
	
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
	protected static int getMaxLength() {
		return s_maxLength;
	}
	
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
	protected static void setMaxLength(int p_length) {
		s_maxLength = p_length;
	}
	
	private final void setId(long p_id) {
		m_id = p_id;
	}
	
	private final void setTime(Date p_time) {
		m_datetime = p_time;
	}
	
	private final void setInfo(String p_info) {
		m_info = cleanText(p_info);
	}
	
	private final void setLogin(String p_login) {
		m_login = cleanText(p_login);
	}
	
	private final void setMessage(String p_message) {
		String message = p_message.trim();
		if (message.length() > getMaxLength())
			message = message.substring(0, getMaxLength());
		m_message = cleanText(message);
	}

	
	// METHODS \\
	/**
	 * Gives the next post identifier.
	 */
	private static synchronized long nextId() {
		return s_idCounter++;
	}
	
	
	/**
	 * Converts &, ", < and > into their XML entities.
	 * 
	 * @param p_str
	 *            the string to convert
	 * 
	 * @return the converted string
	 */
	private final String xmlEntities(String p_str) {
		return p_str.replace("&" , "&amp;")
					.replace("\"", "&quot;")
					.replace("<" , "&lt;")
					.replace(">" , "&gt;");
	}

	
	/**
	 * Trims the input message then keeps the allowed tags (b, i, u, s and tt)
	 * while removing others and closing them if needed.
	 * 
	 * @param p_message
	 *            the message to clean
	 * 
	 * @return the cleaned message
	 */
	private final String cleanText(String p_message) {
		String message = p_message.trim();
		StringBuffer buffer = new StringBuffer(message.length());
		Stack<String> tags = new Stack<String>();
		
		boolean found;
		int previousMatch = 0;
		Matcher matcher = s_tagPattern.matcher(message);
		while (!matcher.hitEnd()) {
			found = matcher.find();

			if (found) {
				// Add the string before the match
				if (matcher.start() > previousMatch) {
					String prev = message.substring(previousMatch, matcher.start());
					buffer.append(xmlEntities(prev));
				}

				String match = matcher.group();
				// Push the opening tag to the queue 
				if (match.matches(STARTTAG_PATTERN)) {
					tags.push(matcher.group(1));
					buffer.append(match);
				}
				
				// End tag : try to find the start tag
				else if (match.matches(ENDTAG_PATTERN)) {
					String tag = matcher.group(2);
					// If this is the last tag in the stack, it's ok
					if (tag != null && !tags.empty() && tag.equals(tags.peek())) {
						tags.pop();
						buffer.append(match);
					}
					// Else, search the first matching tag in the stack
					else {
						int index = tags.search(tag);
						if (index > 0) {
							while (index > 0) {
								buffer.append("</" + tags.pop() + ">");
								--index;
							}
						}
						else
							buffer.append(xmlEntities(match));
					}
				}
				
				// Save the end of the current match
				previousMatch = matcher.end();
			}
			else {
				if (previousMatch < message.length())
					buffer.append(xmlEntities(message.substring(previousMatch)));
			}
		}
		while (!tags.empty())
			buffer.append("</").append(tags.pop()).append(">");
		
		return buffer.toString();
	}
	
	
	/**
	 * Converts a post to an XML-like string.
	 */
	@Override
	public final String toString() {
		return " <" + POST_TAGNAME + " " + POST_ID_ATTRNAME + "=\"" + getId() + "\" " + POST_TIME_ATTRNAME + "=\"" + getFormattedTime() + "\">\n"
		+ "  <" + INFO_TAGNAME + ">" + CDATA_START + getInfo() + CDATA_END + "</" + INFO_TAGNAME + ">\n"
		+ "  <" + MESSAGE_TAGNAME + ">" + CDATA_START + getMessage() + CDATA_END + "</" + MESSAGE_TAGNAME + ">\n"
		+ "  <" + LOGIN_TAGNAME + ">" + CDATA_START + getLogin() + CDATA_END + "</" + LOGIN_TAGNAME + ">\n"
		+ " </" + POST_TAGNAME + ">\n";
	}
}
