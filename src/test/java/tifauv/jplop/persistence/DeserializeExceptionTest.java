/**
 * Dec 14, 2007
 */
package tifauv.jplop.persistence;

import tifauv.jplop.persistence.DeserializeException;
import junit.framework.TestCase;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class DeserializeExceptionTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.board.DeserializeException#DeserializeException(java.lang.String)}.
	 */
	public void testDeserializeExceptionString() {
		// Check without message
		Exception e = new DeserializeException(null);
		assertNull(e.getMessage());

		// Check with a message
		e = new DeserializeException("plop pikaron");
		assertEquals("plop pikaron", e.getMessage());
	}
	
	
	/**
	 * Test method for {@link tifauv.jplop.board.DeserializeException#DeserializeException(java.lang.String, java.lang.Throwable)}.
	 */
	public void testDeserializeExceptionStringThrowable() {
		// Check without message and cause
		Exception e = new DeserializeException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());

		// Check with a message only
		e = new DeserializeException("plop pikaron", null);
		assertEquals("plop pikaron", e.getMessage());
		assertNull(e.getCause());

		// Check with a cause only
		Exception cause = new Exception();
		e = new DeserializeException(null, cause);
		assertNull(e.getMessage());
		assertEquals(cause, e.getCause());

		// Check with a message only
		e = new DeserializeException("plop pikaron", cause);
		assertEquals("plop pikaron", e.getMessage());
		assertEquals(cause, e.getCause());
	}
}
