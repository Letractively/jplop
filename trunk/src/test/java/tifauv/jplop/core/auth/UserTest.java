/**
 * Dec 14, 2007
 */
package tifauv.jplop.core.auth;

import tifauv.jplop.core.auth.PasswordException;
import tifauv.jplop.core.auth.User;
import junit.framework.TestCase;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class UserTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.core.auth.User#User()}.
	 */
	public void testUser() {
		User user = new User();
		assertNull(user.getLogin());
		assertEquals("", user.getEmail());
		assertNull(user.getPassword());
		assertEquals("", user.getRoles());
	}


	public void testSetLogin() {
		User user = new User();
		assertNull(user.getLogin());
		user.setLogin("tramo-p");
		assertEquals("tramo-p", user.getLogin());
	}


	public void testSetEmail() {
		User user = new User();
		assertEquals("", user.getEmail());
		user.setEmail("tramo-p@plop.org");
		assertEquals("tramo-p@plop.org", user.getEmail());
	}


	public void testSetPassword() {
		User user = new User();
		assertNull(user.getPassword());

		try {
			user.setPassword(null);
			assertNull(user.getPassword());

			user.setPassword("");
			assertNull(user.getPassword());

			user.setPassword("pikaron");
			assertNotNull(user.getPassword());
		} catch (PasswordException e) {
			fail(e.getMessage());
		}
	}


	public void testCheckPassword() {
		User user = new User();
		assertTrue(user.checkPassword(null));
		assertFalse(user.checkPassword(""));
		assertFalse(user.checkPassword(" "));
		assertFalse(user.checkPassword("  "));
		assertFalse(user.checkPassword("sdfgdsfgsd"));

		try {
			user.setPassword("pikaron");
			assertFalse(user.checkPassword(null));
			assertFalse(user.checkPassword(""));
			assertTrue(user.checkPassword("pikaron"));
			assertFalse(user.checkPassword("pikaron1"));
			assertFalse(user.checkPassword("1pikaron"));
		} catch (PasswordException e) {
			fail(e.getMessage());
		}
	}


	public void testAddRole() {
		User user = new User();
		assertEquals("", user.getRoles());
	
		user.addRole(null);
		assertEquals("", user.getRoles());
	
		user.addRole("");
		assertEquals("", user.getRoles());

		user.addRole("toto");
		assertEquals("toto", user.getRoles());

		user.addRole("plop ");
		assertEquals("plop,toto", user.getRoles());

		user.addRole(" pika");
		assertEquals("pika,plop,toto", user.getRoles());

		user.addRole("pika");
		assertEquals("pika,plop,toto", user.getRoles());

		user.addRole(" pikaron , ronron ");
		assertEquals("pika,plop,toto", user.getRoles());

		user.addRole("ronron ,");
		assertEquals("pika,plop,toto", user.getRoles());

		user.addRole(",ronron,");
		assertEquals("pika,plop,toto", user.getRoles());
	}


	public void testAddRoles() {
		User user = new User();
		assertEquals("", user.getRoles());

		user.addRoles(null);
		assertEquals("", user.getRoles());

		user.addRoles("");
		assertEquals("", user.getRoles());

		user.addRoles("toto");
		assertEquals("toto", user.getRoles());

		user.addRoles("toto,plop");
		assertEquals("plop,toto", user.getRoles());

		user.addRoles("pika , ron,zorro");
		assertEquals("pika,plop,ron,toto,zorro", user.getRoles());
	}


	public void testClearRoles() {
		User user = new User();
		assertEquals("", user.getRoles());

		user.addRole("toto");
		assertEquals("toto", user.getRoles());
		user.clearRoles();
		assertEquals("", user.getRoles());

		user.addRoles("toto,plop");
		assertEquals("plop,toto", user.getRoles());
		user.clearRoles();
		assertEquals("", user.getRoles());
	}


	public void testSetRoles() {
		User user = new User();
		assertEquals("", user.getRoles());

		user.setRoles(null);
		assertEquals("", user.getRoles());

		user.setRoles("");
		assertEquals("", user.getRoles());

		user.setRoles("toto");
		assertEquals("toto", user.getRoles());

		user.setRoles("plop");
		assertEquals("plop", user.getRoles());

		user.setRoles("plop, toto ,pika");
		assertEquals("pika,plop,toto", user.getRoles());

		user.setRoles("ron,coin");
		assertEquals("coin,ron", user.getRoles());

		user.setRoles(null);
		assertEquals("", user.getRoles());
	}
}
