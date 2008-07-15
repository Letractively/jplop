/**
 * 15 juil. 08
 */
package tifauv.jplop.controller;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import tifauv.jplop.entity.Account;
import tifauv.jplop.stateless.account.AccountLocal;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public class AccountController {

	// CONSTANTS \\
	private static final String LOGGED_IN = "user.logged.in";

	private static final String LOGGED_OUT = "user.logged.out";
	
	private static final String CREATED = "account.created";
	
	private static final String UPDATED = "account.updated";
	
	// FIELDS \\
	/** The account stateless bean. */
	@EJB
	private AccountLocal m_accountBean;
	
	/** The login. */
	private String m_login;
	
	/** The password. */
	private String m_password;
	
	/** The password confirmation. */
	private String m_passwordConfirm;
	
	/** The account. */
	private Account m_account;
	
	
	// CONSTRUCTOR \\
	public AccountController() {
		m_account = new Account();
	}
	
	
	// GETTERS \\
	public Account getAccount() {
		return m_account;
	}
	
	
	public String getLogin() {
		return m_login;
	}
	
	
	public String getPassword() {
		return m_password;
	}
	
	
	public String getPasswordConfirm() {
		return m_passwordConfirm;
	}
	
	
	// SETTERS \\
	public void setAccount(Account p_account) {
		m_account = p_account;
	}
	
	
	public void setLogin(String p_login) {
		m_login = p_login;
	}
	
	
	public void setPassword(String p_password) {
		m_password = p_password;
	}
	
	
	public void setPasswordConfirm(String p_passwordConfirm) {
		m_passwordConfirm = p_passwordConfirm;
	}
	
	
	// METHODS \\
	public String doCreate() {
		m_account = m_accountBean.createUser(getAccount(), getLogin(), getPassword());
		return CREATED;
	}
	
	
	public String doUpdate() {
		m_account = m_accountBean.update(getAccount(), getLogin(), getPassword());
		return UPDATED;
	}
	
	
	public String doLogon() {
		m_account = m_accountBean.authenticate(getLogin(), getPassword());
		return LOGGED_IN;
	}
	
	
	public String doLogout() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)ctx.getExternalContext().getSession(false);
		session.invalidate();
		return LOGGED_OUT;
	}
}
