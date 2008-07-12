package tifauv.jplop.stateless.account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tifauv.jplop.entity.Role;
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
		p_user.setLogin(p_login);
		p_user.hashPassword(p_password);
		m_entities.persist(p_user);
		return p_user;
	}
	

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
	@Override
	public Account authenticate(String p_login, String p_password) {
		if (p_login == null || p_login.isEmpty())
			throw new ValidationException("The login must not be null or empty");
		
		Query query = m_entities.createQuery("SELECT u FROM User u WHERE u.login=:login");
		query.setParameter("login", p_login);
		Account user = (Account)query.getSingleResult();
		
		if (user != null && !user.matchPassword(p_password))
			user = null;
		
		return user;
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
			throw new ValidationException("");

		if (p_role == null)
			throw new ValidationException("");

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
			throw new ValidationException("");

		if (p_role == null)
			throw new ValidationException("");

		p_user.removeRole(p_role);
		m_entities.merge(p_user);
		return p_user;
	}
	

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
	@Override
	public Account changeLogin(Account p_user, String p_login) {
		if (p_user == null)
			throw new ValidationException("");

		if (p_login == null)
			throw new ValidationException("");
		
		p_user.setLogin(p_login);
		m_entities.merge(p_user);
		return p_user;
	}
	

	/**
	 * Changes a user's password.
	 * 
	 * @param p_user
	 *            the user
	 * @param p_password
	 *            the new password
	 * 
	 * @return the updated user
	 */
	@Override
	public Account changePassword(Account p_user, String p_password) {
		if (p_user == null)
			throw new ValidationException("");

		if (p_password == null)
			throw new ValidationException("");
		
		p_user.hashPassword(p_password);
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
			throw new ValidationException("");

		m_entities.remove(m_entities.merge(p_user));
	}
}
