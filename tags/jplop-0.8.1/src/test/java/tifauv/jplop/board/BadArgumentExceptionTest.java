/**
 * Dec 14, 2007
 */
package tifauv.jplop.board;

import junit.framework.TestCase;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class BadArgumentExceptionTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.board.BadArgumentException#BadArgumentException(java.lang.String)}.
	 */
	public void testBadArgumentExceptionString() {
		// Check without message
		Exception e = new BadArgumentException(null);
		assertNull(e.getMessage());

		// Check with a message
		e = new BadArgumentException("plop pikaron");
		assertEquals("plop pikaron", e.getMessage());
	}
	
	
	/**
	 * Test method for {@link tifauv.jplop.board.BadArgumentException#BadArgumentException(java.lang.String, java.lang.Throwable)}.
	 */
	public void testBadArgumentExceptionStringThrowable() {
		// Check without message and cause
		Exception e = new BadArgumentException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());

		// Check with a message only
		e = new BadArgumentException("plop pikaron", null);
		assertEquals("plop pikaron", e.getMessage());
		assertNull(e.getCause());

		// Check with a cause only
		Exception cause = new Exception();
		e = new BadArgumentException(null, cause);
		assertNull(e.getMessage());
		assertEquals(cause, e.getCause());

		// Check with a message only
		e = new BadArgumentException("plop pikaron", cause);
		assertEquals("plop pikaron", e.getMessage());
		assertEquals(cause, e.getCause());
	}
}
