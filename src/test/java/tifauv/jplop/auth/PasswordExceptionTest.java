/**
 * Dec 14, 2007
 */
package tifauv.jplop.auth;

import tifauv.jplop.core.auth.PasswordException;
import tifauv.jplop.core.auth.tifauv;
import junit.framework.TestCase;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class PasswordExceptionTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.board.PasswordException#PasswordException(java.lang.String)}.
	 */
	public void testPasswordExceptionString() {
		// Check without message
		Exception e = new PasswordException(null);
		assertNull(e.getMessage());

		// Check with a message
		e = new PasswordException("plop pikaron");
		assertEquals("plop pikaron", e.getMessage());
	}
	
	
	/**
	 * Test method for {@link tifauv.jplop.board.PasswordException#PasswordException(java.lang.String, java.lang.Throwable)}.
	 */
	public void testPasswordExceptionStringThrowable() {
		// Check without message and cause
		Exception e = new PasswordException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());

		// Check with a message only
		e = new PasswordException("plop pikaron", null);
		assertEquals("plop pikaron", e.getMessage());
		assertNull(e.getCause());

		// Check with a cause only
		Exception cause = new Exception();
		e = new PasswordException(null, cause);
		assertNull(e.getMessage());
		assertEquals(cause, e.getCause());

		// Check with a message only
		e = new PasswordException("plop pikaron", cause);
		assertEquals("plop pikaron", e.getMessage());
		assertEquals(cause, e.getCause());
	}
}
