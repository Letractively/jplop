/**
 * 8 juil. 2008
 */
package tifauv.jplop.ejb.history;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import tifauv.jplop.entity.Post;
import tifauv.jplop.entity.Account;

/**
 * This is the local interface that allows access to the history.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Local
public interface HistoryLocal {

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
	 */
	Post createPost(Post p_post, Date p_time, String p_info, String p_message, Account p_author);

	
	/**
	 * Finds the message posted at the given time and rank.
	 * 
	 * @param p_time
	 *            the post's time
	 * @param p_rank
	 *            the post's rank
	 * 
	 * @return the post or <tt>null</tt> if none exist
	 */
	Post findPost(Date p_time, int p_rank);
	
	
	/**
	 * Returns the list of the latest n posts.
	 * 
	 * @param p_nbPosts
	 *            the number of posts to get
	 * 
	 * @return the list of the last p_nnPosts posts
	 */
	List<Post> getLastPosts(int p_nbPosts);
}
