/**
 * Dec 14, 2007
 */
package tifauv.jplop.board;

import org.apache.log4j.BasicConfigurator;

import junit.framework.TestCase;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class HistoryTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.board.History#History(java.lang.String)}.
	 */
	public void testHistoryString() {
		System.setProperty("log4j.defaultInitOverride", "true");
		BasicConfigurator.configure();

		// Check without url
		History history = new History(null);
		assertNull(history.getURL());
		assertEquals(0, history.size());
		assertEquals(History.DEFAULT_SIZE, history.maxSize());
		assertTrue(history.isEmpty());

		// Check with an url
		history = new History("http://www.example.com/app");
		assertEquals("http://www.example.com/app", history.getURL());
		assertEquals(0, history.size());
		assertEquals(History.DEFAULT_SIZE, history.maxSize());
		assertTrue(history.isEmpty());
	}
}
