/**
 * 5 juil. 2008
 */
package tifauv.jplop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A post has the following elements:
 * <ul><li>a unique identifier&nbsp; (a sequential long integer)&nbsp;;</li>
 * <li>the datetime&nbsp;;</li>
 * <li>the author (null for anonymous posts)&nbsp;;</li>
 * <li>the user-agent of the author&nbsp;;</li>
 * <li>the message.</li></ul>
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
@Entity
public class Post implements Serializable {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = 8833649178782793302L;


	// FIELDS \\
	/** The post's unique identifier. */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="id")
	private long m_id;

	/** The date the post was received by the server. */
	@Column(name="time", nullable=false)
	@Temporal(TemporalType.DATE)
	private Date m_time;
	
	/** The post's rank amongst thoose that have been posted at the same time. */
	@Column(name="rank")
	private int m_rank;

	/** The message. */
	@Column(name="message", nullable=false)
	private String m_message;

	/** The message's author (login or UA). */
	@Column(name="info", nullable=false)
	private String m_info;

	/** The post's author. */
	@ManyToOne
	@JoinColumn(name="author_id")
	private Account m_author;


	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 */
	public Post() {
		setRank(0);
	}


	// GETTERS \\
	/**
	 * Gives the post identifier.
	 */
	public long getId() {
		return m_id;
	}


	/**
	 * Gives the date of the post.
	 */
	public Date getTime() {
		return m_time;
	}
	
	
	/**
	 * Gives the post rank amongst thoose that have the same time.
	 */
	public int getRank() {
		return m_rank;
	}


	/**
	 * Gives the info (user-agent) of the post.
	 */
	public String getInfo() {
		return m_info;
	}


	/**
	 * Gives the author that sent the post.
	 * This is null for anonymous posts.
	 */
	public Account getAuthor() {
		return m_author;
	}


	/**
	 * Gives the login of the author that sent the post.
	 * This is null for anonymous posts.
	 */
	public String getLogin() {
		if (getAuthor() == null)
			return null;

		return getAuthor().getLogin();
	}


	/**
	 * Gives the post's message.
	 */
	public String getMessage() {
		return m_message;
	}


	// SETTERS \\
	/**
	 * Sets the datetime of this post.
	 *
	 * @param p_time
	 *            the time of the post
	 */
	public void setTime(Date p_time) {
		m_time = p_time;
	}
	
	
	/**
	 * Sets the rank of the post amongst thoose with the same time.
	 * 
	 * @param p_rank
	 *            the rank of the post
	 */
	public void setRank(int p_rank) {
		m_rank = p_rank;
	}


	/**
	 * Sets the info (User-Agent) of this post.
	 *
	 * @param p_info
	 *            the info of the post
	 */
	public void setInfo(String p_info) {
		m_info = p_info;
	}


	/**
	 * Sets the author of this post.
	 *
	 * @param p_author
	 *            the author of the post
	 */
	public void setAuthor(Account p_author) {
		m_author = p_author;
	}


	/**
	 * Sets the given message.
	 *
	 * @param p_message
	 *            the post's message
	 */
	public void setMessage(String p_message) {
		m_message = p_message;
	}


	// METHODS \\
	/**
	 * A Post is equal to an object o if and only if
	 * o is another non-null Post with the same id.
	 */
	@Override
	public boolean equals(Object p_object) {
		return p_object != null
				&& p_object instanceof Post
				&& getId() == ((Post)p_object).getId();
	}


	/**
	 * Converts a post to an XML-like string.
	 */
	@Override
	public String toString() {
		return Long.toString(getId()) + ':' + getTime().toString() + ':' + getMessage();
	}


	/**
	 * The hashcode is the post's identifier.
	 */
	@Override
	public int hashCode() {
		return (int)getId();
	}
}
