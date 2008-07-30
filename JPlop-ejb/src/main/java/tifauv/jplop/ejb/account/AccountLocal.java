/**
 * 5 juil. 2008
 */
package tifauv.jplop.ejb.account;

import javax.ejb.Local;

import tifauv.jplop.entity.Account;

/**
 * This is the local interface to the AccountBean.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Local
public interface AccountLocal {

	/**
	 * Create a new user account with a login and password.
	 * 
	 * @param p_account
	 *            the account to create
	 * @param p_login
	 *            the user's login
	 * @param p_password
	 *            the user's password
	 * 
	 * @return the initialized account
	 */
	Account createUser(Account p_account, String p_login, String p_password);


	/**
	 * Finds a user by its login.
	 * 
	 * @param p_login
	 *            the account's login
	 * 
	 * @return the initialized account
	 */
	Account findUser(String p_login);
	
	
	/**
	 * Changes the account's login and/or password.
	 * 
	 * @param p_account
	 *            the account
	 * @param p_login
	 *            the new login
	 * @param p_password
	 *            the new password
	 * 
	 * @return the updated account
	 */
	Account update(Account p_account, String p_login, String p_password);
	
	
	/**
	 * Deletes an account.
	 * 
	 * @param p_account
	 *            the account
	 */
	void deleteAccount(Account p_account);
}
