/**
 * Dec 14, 2007
 */
package tifauv.jplop.core.backend.file.persistence;

import junit.framework.TestCase;
import tifauv.jplop.core.backend.file.persistence.SerializeException;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class SerializeExceptionTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.board.SerializeException#SerializeException(java.lang.String)}.
	 */
	public void testSerializeExceptionString() {
		// Check without message
		Exception e = new SerializeException(null);
		assertNull(e.getMessage());

		// Check with a message
		e = new SerializeException("plop pikaron");
		assertEquals("plop pikaron", e.getMessage());
	}
	
	
	/**
	 * Test method for {@link tifauv.jplop.board.SerializeException#SerializeException(java.lang.String, java.lang.Throwable)}.
	 */
	public void testSerializeExceptionStringThrowable() {
		// Check without message and cause
		Exception e = new SerializeException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());

		// Check with a message only
		e = new SerializeException("plop pikaron", null);
		assertEquals("plop pikaron", e.getMessage());
		assertNull(e.getCause());

		// Check with a cause only
		Exception cause = new Exception();
		e = new SerializeException(null, cause);
		assertNull(e.getMessage());
		assertEquals(cause, e.getCause());

		// Check with a message only
		e = new SerializeException("plop pikaron", cause);
		assertEquals("plop pikaron", e.getMessage());
		assertEquals(cause, e.getCause());
	}
}
