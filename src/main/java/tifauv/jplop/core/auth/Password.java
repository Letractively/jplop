/**
 * Dec 20, 2007
 */
package tifauv.jplop.core.auth;

/**
 * The interface of password containers/checkers.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public interface Password {

	// GETTERS \\
	/**
	 * Gives the password's public value (ciphered, hashed, whatever...)
	 * that can be stored in the users file.
	 */
	public String getPassword();
	
	
	// SETTERS \\
	/**
	 * Stores the given password as the reference one.
	 * 
	 * @param p_password
	 *            the reference password
	 * 
	 * @throws PasswordException
	 *            if the given password does not match some implementation-specific
	 *            criteria
	 */
	public void setPassword(String p_password)
	throws PasswordException;
	
	
	// METHODS \\
	/**
	 * Checks a given password against the reference one.
	 * 
	 * @param p_otherPassword
	 *            the password to check against the reference
	 * 
	 * @return <code>true</code> if the two passwords match,
	 *         <code>false</code> otherwise
	 * 
	 * @throws PasswordException
	 *            if some error occured while checking the passwords
	 *
	 * @see #setPassword(String)
	 */
	public boolean check(String p_otherPassword)
	throws PasswordException;
}
