package tifauv.jplop.ejb.account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tifauv.jplop.entity.Account;
import tifauv.jplop.exceptions.ValidationException;

/**
 * This is the stateless bean that manages the User entity beans.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Stateless(name="AccountSB", mappedName="ejb/stateless/Account")
public class AccountBean implements AccountLocal {

	// FIELDS \\
	/** The User entities manager. */
	@PersistenceContext(unitName="jplop-board")
	private EntityManager m_entities;

	
	// METHODS \\
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
	@Override
	public Account createUser(Account p_account, String p_login, String p_password) {
		if (p_account == null)
			throw new ValidationException("The account must not be null");
		
		if (p_login == null || p_login.isEmpty())
			throw new ValidationException("The login must not be empty");
		
		if (p_password == null || p_password.isEmpty())
			throw new ValidationException("The password must not be empty");

		p_account.setLogin(p_login);
		p_account.setPassword(p_password);
		m_entities.persist(p_account);
		return p_account;
	}
	

	/**
	 * Finds a user by its login.
	 * 
	 * @param p_login
	 *            the account's login
	 * 
	 * @return the initialized account or <tt>null</tt> if none exist
	 *
	 * @see tifauv.jplop.ejb.account.AccountLocal#findAccount(java.lang.String)
	 */
	@Override
	public Account findUser(String p_login) {
		if (p_login == null)
			throw new ValidationException("The login must not be null");
	
		return (Account)m_entities.createNamedQuery("findAccountByLogin")
		.setParameter("login", p_login)
		.getSingleResult();
	}

	
	/**
	 * Updates an account.
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
	@Override
	public Account update(Account p_account, String p_login, String p_password) {
		if (p_account == null)
			throw new ValidationException("Cannot update a null account");

		if (p_login == null || p_login.isEmpty())
			throw new ValidationException("The login must not be empty");

		if (p_password == null || p_password.isEmpty())
			throw new ValidationException("The password must not be empty");

		p_account.setLogin(p_login);
		p_account.setPassword(p_password);
		m_entities.merge(p_account);
		return p_account;
	}

	
	/**
	 * Deletes an account.
	 * 
	 * @param p_account
	 *            the account
	 */
	@Override
	public void deleteAccount(Account p_account) {
		if (p_account == null)
			throw new ValidationException("Cannot delete a null account");

		m_entities.remove(m_entities.merge(p_account));
	}
}
