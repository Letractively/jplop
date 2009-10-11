/**
 * Mar 16, 2008
 */
package tifauv.jplop.core.backend.file.persistence;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;

import tifauv.jplop.core.backend.file.persistence.PersistenceManager;

/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class SerializableTest extends TestCase {
	
	private static final String CATALINA_HOME = "/usr/local/tomcat";
	private static final String CATALINA_BASE = "/var/www/tomcat";

	
	/**
	 * Test method for {@link tifauv.jplop.persistence.Persistent#setDataDir(java.lang.String, java.lang.String)}
	 * without Tomcat environment (no CATALINA_HOME and CATALINA_BASE defined).
	 * This only tests basic border-values null and empty strings.
	 */
	public final void testSetDataDir1() {
		PersistenceManager manager = new PersistenceManager();
		
		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
		
		// default
		assertNull(manager.getDataDir());

		// null / null
		manager.setDataDir(null, null);
		assertNull(manager.getDataDir());

		// empty / null
		manager.setDataDir("", null);
		assertNull(manager.getDataDir());
		
		// null / empty
		manager.setDataDir(null, "");
		assertEquals(new File(""), manager.getDataDir());
		
		// empty / empty
		manager.setDataDir("", "");
		assertEquals(new File(""), manager.getDataDir());
	}
	
	/**
	 * Test method for {@link tifauv.jplop.persistence.Persistent#setDataDir(java.lang.String, java.lang.String)}
	 * without Tomcat environment (no CATALINA_HOME and CATALINA_BASE defined)
	 */
	public final void testSetDataDir2() {
		PersistenceManager manager = new PersistenceManager();
		
		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
		
		// null / absolute w/o ${catalina.}
		manager.setDataDir(null, "/plop");
		assertEquals(new File("/plop"), manager.getDataDir());
		
		// null / relative w/o ${catalina.}
		manager.setDataDir(null, "plop");
		assertEquals(new File("plop"), manager.getDataDir());
		
		// !empty / absolute w/o ${catalina.}
		manager.setDataDir("/toto", "/plop");
		assertEquals(new File("/plop"), manager.getDataDir());
		
		// !empty / relative w/o ${catalina.}
		manager.setDataDir("/toto", "plop");
		assertEquals(new File("/toto/WEB-INF/plop"), manager.getDataDir());

		
		// null / w/ ${catalina.home}
		manager.setDataDir(null, "${catalina.home}/plop");
		assertEquals(new File("${catalina.home}/plop"), manager.getDataDir());
		
		// null / w/ ${catalina.base}
		manager.setDataDir(null, "${catalina.base}/plop");
		assertEquals(new File("${catalina.base}/plop"), manager.getDataDir());
		
		// !empty / w/ ${catalina.home}
		manager.setDataDir("/toto", "${catalina.home}/plop");
		assertEquals(new File("/toto/WEB-INF/${catalina.home}/plop"), manager.getDataDir());

		// !empty / w/ ${catalina.base}
		manager.setDataDir("/toto", "${catalina.base}/plop");
		assertEquals(new File("/toto/WEB-INF/${catalina.base}/plop"), manager.getDataDir());
	}

	
	/**
	 * Test method for {@link tifauv.jplop.persistence.Persistent#setDataDir(java.lang.String, java.lang.String)}
	 * with a classic Tomcat environment (CATALINA_HOME == CATALINA_BASE)
	 */
	public final void testSetDataDir3() {
		PersistenceManager manager = new PersistenceManager();
		
		System.setProperty("catalina.home", CATALINA_HOME);
		System.setProperty("catalina.base", CATALINA_HOME);
		
		// null / absolute w/o ${catalina.}
		manager.setDataDir(null, "/plop");
		assertEquals(new File("/plop"), manager.getDataDir());
		
		// null / relative w/o ${catalina.}
		manager.setDataDir(null, "plop");
		assertEquals(new File("plop"), manager.getDataDir());
		
		// !empty / absolute w/o ${catalina.}
		manager.setDataDir("/toto", "/plop");
		assertEquals(new File("/plop"), manager.getDataDir());
		
		// !empty / relative w/o ${catalina.}
		manager.setDataDir("/toto", "plop");
		assertEquals(new File("/toto/WEB-INF/plop"), manager.getDataDir());

		
		// null / w/ ${catalina.home}
		manager.setDataDir(null, "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), manager.getDataDir());
		
		// null / w/ ${catalina.base}
		manager.setDataDir(null, "${catalina.base}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), manager.getDataDir());
		
		// !empty / w/ ${catalina.home}
		manager.setDataDir("/toto", "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), manager.getDataDir());

		// !empty / w/ ${catalina.base}
		manager.setDataDir("/toto", "${catalina.base}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), manager.getDataDir());

		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
	}

	
	/**
	 * Test method for {@link tifauv.jplop.persistence.Persistent#setDataDir(java.lang.String, java.lang.String)}
	 * with a classic Tomcat environment (CATALINA_HOME != CATALINA_BASE)
	 */
	public final void testSetDataDir4() {
		PersistenceManager manager = new PersistenceManager();
		
		System.setProperty("catalina.home", CATALINA_HOME);
		System.setProperty("catalina.base", CATALINA_BASE);
		
		// null / absolute w/o ${catalina.}
		manager.setDataDir(null, "/plop");
		assertEquals(new File("/plop"), manager.getDataDir());
		
		// null / relative w/o ${catalina.}
		manager.setDataDir(null, "plop");
		assertEquals(new File("plop"), manager.getDataDir());
		
		// !empty / absolute w/o ${catalina.}
		manager.setDataDir("/toto", "/plop");
		assertEquals(new File("/plop"), manager.getDataDir());
		
		// !empty / relative w/o ${catalina.}
		manager.setDataDir("/toto", "plop");
		assertEquals(new File("/toto/WEB-INF/plop"), manager.getDataDir());

		
		// null / w/ ${catalina.home}
		manager.setDataDir(null, "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), manager.getDataDir());
		
		// null / w/ ${catalina.base}
		manager.setDataDir(null, "${catalina.base}/plop");
		assertEquals(new File(CATALINA_BASE + "/plop"), manager.getDataDir());
		
		// !empty / w/ ${catalina.home}
		manager.setDataDir("/toto", "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), manager.getDataDir());

		// !empty / w/ ${catalina.base}
		manager.setDataDir("/toto", "${catalina.base}/plop");
		assertEquals(new File(CATALINA_BASE + "/plop"), manager.getDataDir());

		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
	}
	
	
	@Override
	public final void setUp() {
		Appender console = new ConsoleAppender();
		console.setName("console");
		LogManager.getRootLogger().addAppender(console);
	}
	
	@Override
	public final void tearDown() {
		LogManager.shutdown();
	}
}
