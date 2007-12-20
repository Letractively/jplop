/**
 * Dec 20, 2007
 */
package tifauv.jplop.auth;

import junit.framework.TestCase;

/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class SSHAPasswordTest extends TestCase {

	/**
	 * Test method for raw passwords.
	 */
	public void testSetClearPassword()
	throws PasswordException {
		Password psw = new SSHAPassword();
		psw.setPassword("password");
		assertTrue(psw.check("password"));
	}
	
	
	public void testSetHashedPassword()
	throws PasswordException {
		Password psw = new SSHAPassword();
		psw.setPassword("{SSHA}VV8wdb4sGLy7Lroi4gxXaj33jCm9tGtP");
		assertTrue(psw.check("LamePassword"));
	}
	
	
	public void testGetPassword()
	throws PasswordException {
		Password psw = new SSHAPassword();
		psw.setPassword("{SSHA}VV8wdb4sGLy7Lroi4gxXaj33jCm9tGtP");
		assertEquals("{SSHA}VV8wdb4sGLy7Lroi4gxXaj33jCm9tGtP", psw.getPassword());
	}
}
