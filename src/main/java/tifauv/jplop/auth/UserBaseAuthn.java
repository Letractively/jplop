/**
 * 
 */
package tifauv.jplop.auth;

import javax.security.auth.Subject;

/**
 * @author tifauv
 *
 */
public class UserBaseAuthn implements Authenticator {
	
	public UserBaseAuthn(String p_username, String p_password) {
		
	}

	/**
	 * @see tifauv.jplop.auth.Authenticator#authenticate()
	 */
	public boolean authenticate() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see tifauv.jplop.auth.Authenticator#getSubject()
	 */
	public Subject getSubject() {
		// TODO Auto-generated method stub
		return null;
	}
}
