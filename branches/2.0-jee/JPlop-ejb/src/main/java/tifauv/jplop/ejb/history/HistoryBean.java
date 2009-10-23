/**
 * 8 juil. 2008
 */
package tifauv.jplop.ejb.history;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tifauv.jplop.entity.Account;
import tifauv.jplop.entity.board.Post;
import tifauv.jplop.exceptions.ValidationException;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Stateless(name="HistorySB", mappedName="ejb/stateless/History")
public class HistoryBean implements HistoryLocal {

	// FIELDS \\
	/** The Post entities manager. */
	@PersistenceContext(unitName="jplop-board")
	private EntityManager m_entities;
	
	
	// METHODS \\
	/**
	 * Creates a new post.
	 * 
	 * @param p_post
	 *            the uninitialized post
	 * @param p_time
	 *            the post's reception time
	 * @param p_info
	 *            the post's info (usually the author's User-Agent)
	 * @param p_message
	 *            the post's message
	 * @param p_author
	 *            the post's author
	 * 
	 * @return the initialized post
	 * 
	 * @see tifauv.jplop.stateless.history.HistoryLocal#creattifauv.jplop.entity.board.ntity.board.Post, java.util.Date, java.lang.String, java.lang.String, tifauv.jplop.entity.Account)
	 */
	@Override
	public Post createPost(Post p_post, Date p_time, String p_info, String p_message, Account p_author) {
		p_post.setTime(p_time);
		p_post.setRank(0);
		p_post.setInfo(p_info);
		p_post.setMessage(p_message);
		p_post.setAuthor(p_author);
		m_entities.persist(p_post);
		
		return p_post;
	}

	
	/**
	 * Finds the message posted at the given time and rank.
	 * 
	 * @param p_time
	 *            the post's time
	 * @param p_rank
	 *            the post's rank
	 * 
	 * @return the post or <tt>null</tt> if none exist
	 *
	 * @see tifauv.jplop.stateless.history.HistoryLocal#findPost(java.util.Date, int)
	 */
	@Override
	public Post findPost(Date p_time, int p_rank) {
		if (p_time == null)
			throw new ValidationException("The time must not be null");
	
		Query query = m_entities.createQuery("SELECT p FROM Post p WHERE p.time=:time AND p.rank=:rank");
		query.setParameter("time", p_time);
		query.setParameter("rank", p_rank);
		return (Post)query.getSingleResult();
	}

	
	/**
	 * Returns the list of the latest n posts.
	 * 
	 * @param p_nbPosts
	 *            the number of posts to get
	 * 
	 * @return the list of the last p_nnPosts posts
	 * 
	 * @see tifauv.jplop.stateless.history.HistoryLocal#getLastPosts(int)
	 */
	@Override
	public List<Post> getLastPosts(int p_nbPosts) {
		// TODO Auto-generated method stub
		return null;
	}
}
