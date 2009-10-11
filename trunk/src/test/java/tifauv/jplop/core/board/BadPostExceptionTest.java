/**
 * Dec 14, 2007
 */
package tifauv.jplop.core.board;

import tifauv.jplop.core.board.BadPostException;
import junit.framework.TestCase;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class BadPostExceptionTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.core.board.BadPostException#BadPostException(java.lang.String)}.
	 */
	public void testBadPostExceptionString() {
		// Check without message
		Exception e = new BadPostException(null);
		assertNull(e.getMessage());

		// Check with a message
		e = new BadPostException("plop pikaron");
		assertEquals("plop pikaron", e.getMessage());
	}
	
	
	/**
	 * Test method for {@link tifauv.jplop.core.board.BadPostException#BadPostException(java.lang.String, java.lang.Throwable)}.
	 */
	public void testBadPostExceptionStringThrowable() {
		// Check without message and cause
		Exception e = new BadPostException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());

		// Check with a message only
		e = new BadPostException("plop pikaron", null);
		assertEquals("plop pikaron", e.getMessage());
		assertNull(e.getCause());

		// Check with a cause only
		Exception cause = new Exception();
		e = new BadPostException(null, cause);
		assertNull(e.getMessage());
		assertEquals(cause, e.getCause());

		// Check with a message only
		e = new BadPostException("plop pikaron", cause);
		assertEquals("plop pikaron", e.getMessage());
		assertEquals(cause, e.getCause());
	}
}
