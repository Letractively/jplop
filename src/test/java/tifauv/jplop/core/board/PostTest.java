/**
 * Dec 14, 2007
 */
package tifauv.jplop.core.board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tifauv.jplop.core.board.BadPostException;
import tifauv.jplop.core.board.Post;


/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class PostTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.core.board.Post#Post(long, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testPostLongStringStringString() {
		// First simple test
		Date now = new Date();
		Post post = new Post(0, null, "message", null);
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		assertEquals(0, post.getId());
		assertEquals("", post.getInfo());
		assertEquals("message", post.getMessage());
		assertEquals(Post.ANONYMOUS_LOGIN, post.getLogin());
		String postStr = " <post id=\"0\" time=\"" + formatter.format(now) + "\">\n"
			+ "  <info><![CDATA[]]></info>\n"
			+ "  <message><![CDATA[message]]></message>\n"
			+ "  <login><![CDATA[" + Post.ANONYMOUS_LOGIN + "]]></login>\n"
			+ " </post>\n";
		assertEquals(postStr, post.toString());

		// Check with a non-null login
		post = new Post(1, "info", "message", "login");
		assertEquals(1, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("message", post.getMessage());
		assertEquals("login", post.getLogin());

		// Check with a well-formatted message
		post = new Post(2, "info", "<i>italic</i>", "login");
		assertEquals(2, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic</i>", post.getMessage());
		assertEquals("login", post.getLogin());
	
		// Check with a bad-formatted message
		post = new Post(1, "info", "<i>italic", "login");
		assertEquals(1, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic</i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(1, "info", "italic</i>", "login");
		assertEquals(1, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("italic&lt;/i&gt;", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(3, "info", "<i>italic<b>bold", "login");
		assertEquals(3, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic<b>bold</b></i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(4, "info", "<i>italic</b>bold", "login");
		assertEquals(4, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic&lt;/b&gt;bold</i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(5, "info", "<i>italic<u>underline</b>bold</u>", "login");
		assertEquals(5, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic<u>underline&lt;/b&gt;bold</u></i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(6, "info", "<b>bold<i>italic<u>underline</b>normal", "login");
		assertEquals(6, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<b>bold<i>italic<u>underline</u></i></b>normal", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(7, "info", "plop http://www.example.com toto", "login");
		assertEquals(7, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"http://www.example.com\">[http]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an https url in the message
		post = new Post(8, "info", "plop https://www.example.com toto", "login");
		assertEquals(8, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"https://www.example.com\">[https]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an ftp url in the message
		post = new Post(9, "info", "plop ftp://www.example.com toto", "login");
		assertEquals(9, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"ftp://www.example.com\">[ftp]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(10, "info", "plop http://www.example.com00:11:22 toto", "login");
		assertEquals(10, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"http://www.example.com00:11:22\">[http]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(11, "info", "<i>http://www.example.com</i>", "login");
		assertEquals(11, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i><a href=\"http://www.example.com\">[http]</a></i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(12, "info", "http://www.example.com[:totoz]", "login");
		assertEquals(12, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<a href=\"http://www.example.com[:totoz]\">[http]</a>", post.getMessage());
		assertEquals("login", post.getLogin());
	}

	
	private Element buildPost(Document p_doc, long p_id, String p_info, String p_message, String p_login) {
		Element post = p_doc.createElement("post");
		post.setAttribute("id", Long.toString(p_id));
		post.setAttribute("time", "20071215022330");
		
		Element info = p_doc.createElement("info");
		info.appendChild(p_doc.createCDATASection(p_info));
		post.appendChild(info);
		
		Element message = p_doc.createElement("message");
		message.appendChild(p_doc.createCDATASection(p_message));
		post.appendChild(message);
		
		Element login = p_doc.createElement("login");
		login.appendChild(p_doc.createCDATASection(p_login));
		post.appendChild(login);
		
		return post;
	}
	
	
	/**
	 * Test method for {@link tifauv.jplop.core.board.Post#Post(org.w3c.dom.Element)}.
	 */
	public void testPostElement() {
		// Build the DocumentBuilder
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException e) {
			fail("Couldn't create the document builder :-/");
			return;
		}

		// First simple test
		try {
			Post post = new Post(buildPost(doc, 0, "info", "message", null));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("message", post.getMessage());
			assertEquals(Post.ANONYMOUS_LOGIN, post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}
		
		// Check with a non-null login
		try {
			Post post = new Post(buildPost(doc, 0, "info", "message", "login"));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("message", post.getMessage());
			assertEquals("login", post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}
		
		// Check with a well-formatted message
		try {
			Post post = new Post(buildPost(doc, 0, "info", "<i>italic</i>", "login"));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("<i>italic</i>", post.getMessage());
			assertEquals("login", post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}
		
		// Check with a bad-formatted message
		try {
			Post post = new Post(buildPost(doc, 0, "info", "<i>italic", "login"));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("<i>italic", post.getMessage());
			assertEquals("login", post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}
		
		// Try with a bad element root element
		try {
			Element bad = doc.createElement("plop");
			new Post(bad);
			fail("A post should not be built (even with default values) from a bad DOM element.");
		} catch (BadPostException e) {
			// Nothing, this is expected
		}

		// Try with no id attribute
		try {
			Element bad = buildPost(doc, 1, "inf", "msg", "user");
			bad.removeAttribute("id");
			new Post(bad);
			fail("A post should not be built (even with default values) if the <post> element has no id.");
		} catch (NumberFormatException e) {
			fail("The id attribute is missing, but there should not be a NumberFormatException");
		} catch (BadPostException e) {
			// Nothing, this is expected
		}

		// Try with a bad id attribute
		try {
			Element bad = buildPost(doc, 1, "inf", "msg", "user");
			bad.setAttribute("id", "toto");
			new Post(bad);
			fail("Didn't detect the id attribute is not a number");
		} catch (NumberFormatException e) {
			fail("The id attribute is missing, but there should not be a NumberFormatException");
		} catch (BadPostException e) {
			// Nothing, this is expected
		}

		// Try with no time attribute
		try {
			Element bad = buildPost(doc, 1, "inf", "msg", "user");
			bad.removeAttribute("time");
			new Post(bad);
			fail("A post should not be built (even with default values) if the <post> element has no time.");
		} catch (BadPostException e) {
			// This is expected
		}

		// Try with no time attribute
		try {
			Element bad = buildPost(doc, 1, "inf", "msg", "user");
			bad.setAttribute("time", "toto");
			new Post(bad);
			fail("Didn't detect the time attribute is wrong");
		} catch (BadPostException e) {
			// Nothing, the exception is expected
		}

		// Try with no info element
		try {
			Element bad = buildPost(doc, 2, "inf", "msg", "user");
			bad.removeChild(bad.getElementsByTagName("info").item(0));
			Post post = new Post(bad);
			assertEquals(2, post.getId());
			assertEquals("", post.getInfo());
			assertEquals("msg", post.getMessage());
			assertEquals("user", post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}

		// Try with no message element
		try {
			Element bad = buildPost(doc, 3, "inf", "msg", "user");
			bad.removeChild(bad.getElementsByTagName("message").item(0));
			Post post = new Post(bad);
			assertEquals(3, post.getId());
			assertEquals("inf", post.getInfo());
			assertEquals("", post.getMessage());
			assertEquals("user", post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}

		// Try with no login element
		try {
			Element bad = buildPost(doc, 2, "inf", "msg", "user");
			bad.removeChild(bad.getElementsByTagName("login").item(0));
			Post post = new Post(bad);
			assertEquals(2, post.getId());
			assertEquals("inf", post.getInfo());
			assertEquals("msg", post.getMessage());
			assertEquals(Post.ANONYMOUS_LOGIN, post.getLogin());
		} catch (BadPostException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test method for {@link tifauv.jplop.core.board.Post#setMaxLength(int)}.
	 */
	public void testSetMaxLength() {
		// Check the setter and getter work
		Post.setMaxLength(10);
		assertEquals(10, Post.getMaxLength());
		
		// Check negative values are not taken
		Post.setMaxLength(-10);
		assertEquals(10, Post.getMaxLength());
		
		// Check zero is not taken
		Post.setMaxLength(0);
		assertEquals(10, Post.getMaxLength());
		
		// Check smaller messages
		Post post = new Post(0, "info", "0123456", null);
		assertEquals("0123456", post.getMessage());
		
		// Check max sized messages
		post = new Post(1, "info", "0123456789", null);
		assertEquals("0123456789", post.getMessage());
		
		// Check longer messages
		post = new Post(1, "info", "01234567890123", null);
		assertEquals("0123456789", post.getMessage());
		
		// Check max sized messages with extra tag needed
		post = new Post(1, "info", "012<b>3456", null);
		assertEquals("012<b>3456</b>", post.getMessage());

	}

}
