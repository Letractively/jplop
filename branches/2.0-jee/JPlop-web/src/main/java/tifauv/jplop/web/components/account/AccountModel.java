/**
 * 29 juil. 08
 */
package tifauv.jplop.web.components.account;

import java.io.Serializable;

import tifauv.jplop.exceptions.ValidationException;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class AccountModel implements Serializable {

	// CONSTANTS \\
	/** The serialization id. */
	private static final long serialVersionUID = -5725566477304082470L;

	
	// FIELDS \\
	/** The login. */
	private String m_login;
	
	/** The password. */
	private String m_password;
	
	/** The password confirmation. */
	private String m_confirm;
	
	
	// GETTERS \\
	public String getLogin() {
		return m_login;
	}
	
	
	public String getPassword() {
		return m_password;	
	}
	
	
	public String getConfirm() {
		return m_confirm;
	}
	
	
	// SETTERS \\
	public void setLogin(String p_login) {
		m_login = p_login;
	}
	
	
	public void setPassword(String p_password) {
		m_password = p_password;
	}
	
	
	public void setConfirm(String p_confirm) {
		m_confirm = p_confirm;
	}
	
	
	// METHODS \\
	public void validate()
	throws ValidationException {
		if (getLogin() == null || getLogin().isEmpty())
			throw new ValidationException("The login cannot be empty");
		
		if (getPassword() == null || getPassword().isEmpty())
			throw new ValidationException("The password cannot be empty");
		
		if (getConfirm() == null || getConfirm().isEmpty())
			throw new ValidationException("The password confirmation cannot be empty");
		
		if (!getPassword().equals(getConfirm()))
			throw new ValidationException("The password must match its confirmation");
	}
}
