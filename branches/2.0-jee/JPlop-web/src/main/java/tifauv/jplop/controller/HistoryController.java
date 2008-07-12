/**
 * 8 juil. 2008
 */
package tifauv.jplop.controller;

import java.util.Date;

import javax.ejb.EJB;

import tifauv.jplop.entity.Post;
import tifauv.jplop.entity.Account;
import tifauv.jplop.stateless.history.HistoryLocal;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class HistoryController {

	// FIELDS \\
	/** The history stateless bean. */
	@EJB
	private HistoryLocal m_historyBean;
	
	private String m_info;
	
	private String m_message;
	
	private Account m_user;
	
	
	// GETTERS \\
	
	
	// METHODS \\
	/**
	 * 
	 */
	public String doAddPost() {
		Post post = new Post();
		Date now = new Date();
		m_historyBean.createPost(post, now, null, null, null);
		return "post.added";
	}
}
