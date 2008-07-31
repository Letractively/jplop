package tifauv.jplop.web;

import javax.ejb.EJB;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;

import tifauv.jplop.ejb.account.AccountLocal;
import tifauv.jplop.entity.Account;
import tifauv.jplop.utils.PasswordUtils;

/**
 * This is the custom user session container.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class JPlopSession extends AuthenticatedWebSession {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = -2164612750755151084L;
	
	
	// FIELDS \\
	/** The Accounts manager bean. */
	@EJB(name="ejb/stateless/Account")
	private AccountLocal m_accountBean;

	/** The current user. */
	private Account m_account;

	
	// CONSTRUCTORS \\
	/**
	 * Default constructor.
	 * 
	 * @param p_request
	 *            the initialization request
	 */
	public JPlopSession(Request p_request) {
		super(p_request);
		InjectorHolder.getInjector().inject(this);
	}
	
	
	// GETTERS \\
	/**
	 * Gives the current user account.
	 */
	public Account getAccount() {
		return m_account;
	}
	
	
	/**
	 * Gives the user's roles.
	 */
	@Override
	public Roles getRoles() {
		if (isSignedIn())
			return new Roles("User");
        return null;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the current user account.
	 */
	public void setAccount(Account p_account) {
		m_account = p_account;
	}


	// METHODS \\
	/**
	 * Authenticates a user.
	 */
	@Override
	public boolean authenticate(String p_login, String p_password) {
		if (p_login == null || p_password == null)
			return false;
		
		// Find the user
		Account account = m_accountBean.findUser(p_login);
		if (account.getPassword().equals(PasswordUtils.sha256B64(p_password)))
			setAccount(account);
		else
			setAccount(null);
		return getAccount() != null;
	}
}
