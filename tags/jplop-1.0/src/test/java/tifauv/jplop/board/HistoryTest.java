/**
 * Dec 14, 2007
 */
package tifauv.jplop.board;

import java.util.TimeZone;

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
		assertEquals(History.FILE_NAME, history.getFilename());
		assertNotNull(history.getLastModified());
		assertEquals(0, history.size());
		assertEquals(History.DEFAULT_SIZE, history.maxSize());
		assertTrue(history.isEmpty());

		// Check with an url
		history = new History("http://www.example.com/app");
		assertEquals("http://www.example.com/app", history.getURL());
		assertEquals(History.FILE_NAME, history.getFilename());
		assertNotNull(history.getLastModified());
		assertEquals(0, history.size());
		assertEquals(History.DEFAULT_SIZE, history.maxSize());
		assertTrue(history.isEmpty());
	}
	
	
	public void testGetTimezone() {
		// get the default value to restore it later
		TimeZone tz = TimeZone.getDefault();
		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		assertEquals("UTC", History.getTimezone());

		// Check with positive hour decay 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1:00"));
		assertEquals("UTC+0100", History.getTimezone());

		// Check with negative hour decay 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-1:00"));
		assertEquals("UTC-0100", History.getTimezone());

		// Check with positive minute decay 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+0:25"));
		assertEquals("UTC+0025", History.getTimezone());

		// Check with negative minute decay 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-0:45"));
		assertEquals("UTC-0045", History.getTimezone());

		// Check with positive hour and minute decay 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+1:25"));
		assertEquals("UTC+0125", History.getTimezone());

		// Check with negative hour and minute decay 
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-1:45"));
		assertEquals("UTC-0145", History.getTimezone());
		
		// reset the original value
		TimeZone.setDefault(tz);
	}
	
	
	public void testAddMessage() {
		System.setProperty("log4j.defaultInitOverride", "true");
		BasicConfigurator.configure();
		History history = new History(null);

		// Empty
		assertEquals(0, history.size());
		assertTrue(history.isEmpty());

		// Add one message
		assertEquals(0, history.addMessage("info", "message", "login"));
		assertEquals(1, history.size());
		assertFalse(history.isEmpty());
	}
	
	
	public void testIsModifiedSince() {
		System.setProperty("log4j.defaultInitOverride", "true");
		BasicConfigurator.configure();
		History history = new History(null);

		// Empty
		String lastModified = history.getLastModified();
		
		// isModifiedSince should always return true if the parameter is badly formatted
		assertTrue(history.isModifiedSince(null));
		assertTrue(history.isModifiedSince("pikaron"));
		
		// Sleep at least 1 second as it is the LastModified resolution
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// Don't bother, however it can make the next test fail
		}

		// Add one message
		assertEquals(0, history.addMessage("info", "message", "login"));
		assertTrue(history.isModifiedSince(lastModified));
	}
	
	
	public void testTruncate() {
		System.setProperty("log4j.defaultInitOverride", "true");
		BasicConfigurator.configure();
		History history = new History(null, 1);

		// Empty
		assertEquals(0, history.size());
		assertEquals(1, history.maxSize());
		String lastModified = history.getLastModified();
		
		// Sleep at least 1 second as it is the LastModified resolution
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// Don't bother, however it can make the next test fail
		}

		// Add one message
		assertEquals(0, history.addMessage("info", "message 1", "login"));
		assertEquals(1, history.size());
		assertTrue(history.isModifiedSince(lastModified));
		
		// Sleep at least 1 second as it is the LastModified resolution
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// Don't bother, however it can make the next test fail
		}

		// Add another message, the size should still be one
		assertEquals(1, history.addMessage("info", "message 2", "login"));
		assertEquals(1, history.size());
		assertTrue(history.isModifiedSince(lastModified));
	}
	
	
	public void testToString() {
		System.setProperty("log4j.defaultInitOverride", "true");
		BasicConfigurator.configure();
		History history = new History(null);

		assertEquals("<?xml-stylesheet type=\"text/xsl\" href=\"web.xslt\"?>\n<board site=\"null\" timezone=\"UTC+0100\">\n</board>", history.toString());
	}
}
