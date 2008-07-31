package tifauv.jplop.ejb.account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tifauv.jplop.entity.Account;
import tifauv.jplop.entity.Role;
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
	@Override
	public Account createUser(Account p_user, String p_login, String p_password) {
		if (p_user == null)
			throw new ValidationException("Cannot create a null account");

		if (p_login == null)
			throw new ValidationException("Cannot create an account without a login");

		if (p_password == null)
			throw new ValidationException("Cannot create an account without a password");

		p_user.setLogin(p_login);
		p_user.setPassword(p_password);
		m_entities.persist(p_user);
		return p_user;
	}
	

	/**
	 * Finds a user with its login.
	 *  
	 * @param p_login
	 *            the user's login
	 * 
	 * @return the account or <tt>null</tt> if the account doesn't exist
	 */
	@Override
	public Account findUser(String p_login) {
		if (p_login == null || p_login.isEmpty())
			throw new ValidationException("Cannot find an account with an empty login");
		
		return (Account)m_entities.createNamedQuery("findAccountByLogin")
		.setParameter("login", p_login)
		.getSingleResult();
	}

	
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
	@Override
	public Account addRole(Account p_user, Role p_role) {
		if (p_user == null)
			throw new ValidationException("Cannot add a role to a non-existent account");

		if (p_role == null)
			throw new ValidationException("Cannot add a non-existent role to an account");

		p_user.addRole(p_role);
		m_entities.merge(p_user);
		return p_user;
	}

	
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
	@Override
	public Account removeRole(Account p_user, Role p_role) {
		if (p_user == null)
			throw new ValidationException("Cannot remove a role from a non-existent account");

		if (p_role == null)
			throw new ValidationException("Cannot remove a non-existent role from an account");

		p_user.removeRole(p_role);
		m_entities.merge(p_user);
		return p_user;
	}
	

	/**
	 * Updates a user.
	 * 
	 * @param p_user
	 *            the user
	 * @param p_password
	 *            the new login
	 * @param p_password
	 *            the new password
	 * 
	 * @return the updated user
	 */
	@Override
	public Account update(Account p_user, String p_login, String p_password) {
		if (p_user == null)
			throw new ValidationException("Cannot update a non-existent account");

		if (p_login == null)
			throw new ValidationException("Cannot update an account with a non-existent login");
		
		p_user.setLogin(p_login);
		if (p_password != null && !p_password.isEmpty())
			p_user.setPassword(p_password);
		m_entities.merge(p_user);
		return p_user;
	}

	
	/**
	 * Deletes a user.
	 * 
	 * @param p_user
	 *            the user
	 */
	@Override
	public void deleteUser(Account p_user) {
		if (p_user == null)
			throw new ValidationException("Cannot delete a non-existing account");

		m_entities.remove(m_entities.merge(p_user));
	}
}
