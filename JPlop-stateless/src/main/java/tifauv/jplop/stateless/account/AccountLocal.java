/**
 * 5 juil. 2008
 */
package tifauv.jplop.stateless.account;

import javax.ejb.Local;

import tifauv.jplop.entity.Role;
import tifauv.jplop.entity.Account;

/**
 * This is the local interface to the UserBean.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Local
public interface AccountLocal {

	/**
	 * Create a new user with a login and password.
	 * 
	 * @param p_user
	 *            the user to create
	 * @param p_login
	 *            the user's login
	 * @param p_password
	 *            the user's password
	 * 
	 * @return the initialized user
	 */
	Account createUser(Account p_user, String p_login, String p_password);

	
	/**
	 * Finds and authenticates a user with its login and password.
	 *  
	 * @param p_login
	 *            the user's login
	 * @param p_password
	 *            the user's password
	 * 
	 * @return the authenticated user or <tt>null</tt> if the user doesn't exist or can't be authenticated
	 */
	Account authenticate(String p_login, String p_password);

	
	/**
	 * Adds a role to a user.
	 * 
	 * @param p_user
	 *            the user
	 * @param p_role
	 *            the role
	 * 
	 * @return the updated user
	 */
	Account addRole(Account p_user, Role p_role);
	
	
	/**
	 * Removes a role from a user.
	 * 
	 * @param p_user
	 *            the user
	 * @param p_role
	 *            the role
	 * 
	 * @return the updated user
	 */
	Account removeRole(Account p_user, Role p_role);
	
	
	/**
	 * Changes a user's login.
	 * 
	 * @param p_user
	 *            the user
	 * @param p_login
	 *            the new login
	 * 
	 * @return the updated user
	 */
	Account changeLogin(Account p_user, String p_login);

	
	/**
	 * Changes a user's password.
	 * 
	 * @param p_user
	 *            the user
	 * @param p_login
	 *            the new login
	 * @param p_password
	 *            the new password
	 * 
	 * @return the updated user
	 */
	Account update(Account p_user, String p_login, String p_password);
	
	
	/**
	 * Deletes a user.
	 * 
	 * @param p_user
	 *            the user
	 */
	void deleteUser(Account p_user);
}
