/**
 * Mar 16, 2008
 */
package tifauv.jplop.util;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;

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
	 * Test method for {@link tifauv.jplop.util.Serializable#setDataDir(java.lang.String, java.lang.String)}
	 * without Tomcat environment (no CATALINA_HOME and CATALINA_BASE defined).
	 * This only tests basic border-values null and empty strings.
	 */
	public final void testSetDataDir1() {
		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
		
		// default
		assertNull(Serializable.getDataDir());

		// null / null
		Serializable.setDataDir(null, null);
		assertNull(Serializable.getDataDir());

		// empty / null
		Serializable.setDataDir("", null);
		assertNull(Serializable.getDataDir());
		
		// null / empty
		Serializable.setDataDir(null, "");
		assertEquals(new File(""), Serializable.getDataDir());
		
		// empty / empty
		Serializable.setDataDir("", "");
		assertEquals(new File(""), Serializable.getDataDir());
	}
	
	/**
	 * Test method for {@link tifauv.jplop.util.Serializable#setDataDir(java.lang.String, java.lang.String)}
	 * without Tomcat environment (no CATALINA_HOME and CATALINA_BASE defined)
	 */
	public final void testSetDataDir2() {
		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
		
		// null / absolute w/o ${catalina.}
		Serializable.setDataDir(null, "/plop");
		assertEquals(new File("/plop"), Serializable.getDataDir());
		
		// null / relative w/o ${catalina.}
		Serializable.setDataDir(null, "plop");
		assertEquals(new File("plop"), Serializable.getDataDir());
		
		// !empty / absolute w/o ${catalina.}
		Serializable.setDataDir("/toto", "/plop");
		assertEquals(new File("/plop"), Serializable.getDataDir());
		
		// !empty / relative w/o ${catalina.}
		Serializable.setDataDir("/toto", "plop");
		assertEquals(new File("/toto/WEB-INF/plop"), Serializable.getDataDir());

		
		// null / w/ ${catalina.home}
		Serializable.setDataDir(null, "${catalina.home}/plop");
		assertEquals(new File("${catalina.home}/plop"), Serializable.getDataDir());
		
		// null / w/ ${catalina.base}
		Serializable.setDataDir(null, "${catalina.base}/plop");
		assertEquals(new File("${catalina.base}/plop"), Serializable.getDataDir());
		
		// !empty / w/ ${catalina.home}
		Serializable.setDataDir("/toto", "${catalina.home}/plop");
		assertEquals(new File("/toto/WEB-INF/${catalina.home}/plop"), Serializable.getDataDir());

		// !empty / w/ ${catalina.base}
		Serializable.setDataDir("/toto", "${catalina.base}/plop");
		assertEquals(new File("/toto/WEB-INF/${catalina.base}/plop"), Serializable.getDataDir());
	}

	
	/**
	 * Test method for {@link tifauv.jplop.util.Serializable#setDataDir(java.lang.String, java.lang.String)}
	 * with a classic Tomcat environment (CATALINA_HOME == CATALINA_BASE)
	 */
	public final void testSetDataDir3() {
		System.setProperty("catalina.home", CATALINA_HOME);
		System.setProperty("catalina.base", CATALINA_HOME);
		
		// null / absolute w/o ${catalina.}
		Serializable.setDataDir(null, "/plop");
		assertEquals(new File("/plop"), Serializable.getDataDir());
		
		// null / relative w/o ${catalina.}
		Serializable.setDataDir(null, "plop");
		assertEquals(new File("plop"), Serializable.getDataDir());
		
		// !empty / absolute w/o ${catalina.}
		Serializable.setDataDir("/toto", "/plop");
		assertEquals(new File("/plop"), Serializable.getDataDir());
		
		// !empty / relative w/o ${catalina.}
		Serializable.setDataDir("/toto", "plop");
		assertEquals(new File("/toto/WEB-INF/plop"), Serializable.getDataDir());

		
		// null / w/ ${catalina.home}
		Serializable.setDataDir(null, "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), Serializable.getDataDir());
		
		// null / w/ ${catalina.base}
		Serializable.setDataDir(null, "${catalina.base}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), Serializable.getDataDir());
		
		// !empty / w/ ${catalina.home}
		Serializable.setDataDir("/toto", "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), Serializable.getDataDir());

		// !empty / w/ ${catalina.base}
		Serializable.setDataDir("/toto", "${catalina.base}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), Serializable.getDataDir());

		System.clearProperty("catalina.home");
		System.clearProperty("catalina.base");
	}

	
	/**
	 * Test method for {@link tifauv.jplop.util.Serializable#setDataDir(java.lang.String, java.lang.String)}
	 * with a classic Tomcat environment (CATALINA_HOME != CATALINA_BASE)
	 */
	public final void testSetDataDir4() {
		System.setProperty("catalina.home", CATALINA_HOME);
		System.setProperty("catalina.base", CATALINA_BASE);
		
		// null / absolute w/o ${catalina.}
		Serializable.setDataDir(null, "/plop");
		assertEquals(new File("/plop"), Serializable.getDataDir());
		
		// null / relative w/o ${catalina.}
		Serializable.setDataDir(null, "plop");
		assertEquals(new File("plop"), Serializable.getDataDir());
		
		// !empty / absolute w/o ${catalina.}
		Serializable.setDataDir("/toto", "/plop");
		assertEquals(new File("/plop"), Serializable.getDataDir());
		
		// !empty / relative w/o ${catalina.}
		Serializable.setDataDir("/toto", "plop");
		assertEquals(new File("/toto/WEB-INF/plop"), Serializable.getDataDir());

		
		// null / w/ ${catalina.home}
		Serializable.setDataDir(null, "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), Serializable.getDataDir());
		
		// null / w/ ${catalina.base}
		Serializable.setDataDir(null, "${catalina.base}/plop");
		assertEquals(new File(CATALINA_BASE + "/plop"), Serializable.getDataDir());
		
		// !empty / w/ ${catalina.home}
		Serializable.setDataDir("/toto", "${catalina.home}/plop");
		assertEquals(new File(CATALINA_HOME + "/plop"), Serializable.getDataDir());

		// !empty / w/ ${catalina.base}
		Serializable.setDataDir("/toto", "${catalina.base}/plop");
		assertEquals(new File(CATALINA_BASE + "/plop"), Serializable.getDataDir());

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
