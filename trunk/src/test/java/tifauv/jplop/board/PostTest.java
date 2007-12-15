/**
 * Dec 14, 2007
 */
package tifauv.jplop.board;

import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * .
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class PostTest extends TestCase {

	/**
	 * Test method for {@link tifauv.jplop.board.Post#Post(long, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testPostLongStringStringString() {
		// First simple test
		Post post = new Post(0, "info", "message", null);
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("message", post.getMessage());
		assertEquals(Post.ANONYMOUS_LOGIN, post.getLogin());

		// Check with a non-null login
		post = new Post(0, "info", "message", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("message", post.getMessage());
		assertEquals("login", post.getLogin());

		// Check with a well-formatted message
		post = new Post(0, "info", "<i>italic</i>", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic</i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(0, "info", "<i>italic", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic</i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(0, "info", "<i>italic<b>bold", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic<b>bold</b></i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(0, "info", "<i>italic</b>bold", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic&lt;/b&gt;bold</i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with a bad-formatted message
		post = new Post(0, "info", "<i>italic<u>underline</b>bold</u>", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i>italic<u>underline&lt;/b&gt;bold</u></i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(0, "info", "plop http://www.example.com toto", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"http://www.example.com\">[http]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(0, "info", "plop http://www.example.com toto", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"http://www.example.com\">[http]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an https url in the message
		post = new Post(0, "info", "plop https://www.example.com toto", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"https://www.example.com\">[https]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an ftp url in the message
		post = new Post(0, "info", "plop ftp://www.example.com toto", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"ftp://www.example.com\">[ftp]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(0, "info", "plop http://www.example.com00:11:22 toto", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("plop <a href=\"http://www.example.com00:11:22\">[http]</a> toto", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(0, "info", "<i>http://www.example.com</i>", "login");
		assertEquals(0, post.getId());
		assertEquals("info", post.getInfo());
		assertEquals("<i><a href=\"http://www.example.com\">[http]</a></i>", post.getMessage());
		assertEquals("login", post.getLogin());
		
		// Check with an http url in the message
		post = new Post(0, "info", "http://www.example.com[:totoz]", "login");
		assertEquals(0, post.getId());
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
	 * Test method for {@link tifauv.jplop.board.Post#Post(org.w3c.dom.Element)}.
	 */
	public void testPostElement() {
		// Build the DocumentBuilder
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
		}
		catch (ParserConfigurationException e) {
			fail("Couldn't create the document builder :-/");
		}
		
		// First simple test
		try {
			Post post = new Post(buildPost(doc, 0, "info", "message", null));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("message", post.getMessage());
			assertEquals(Post.ANONYMOUS_LOGIN, post.getLogin());
		}
		catch (ParseException e) {
			fail(e.getMessage());
		}
		
		// Check with a non-null login
		try {
			Post post = new Post(buildPost(doc, 0, "info", "message", "login"));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("message", post.getMessage());
			assertEquals("login", post.getLogin());
		}
		catch (ParseException e) {
			fail(e.getMessage());
		}
		
		// Check with a well-formatted message
		try {
			Post post = new Post(buildPost(doc, 0, "info", "<i>italic</i>", "login"));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("<i>italic</i>", post.getMessage());
			assertEquals("login", post.getLogin());
		}
		catch (ParseException e) {
			fail(e.getMessage());
		}
		
		// Check with a bad-formatted message
		try {
			Post post = new Post(buildPost(doc, 0, "info", "<i>italic", "login"));
			assertEquals(0, post.getId());
			assertEquals("info", post.getInfo());
			assertEquals("<i>italic", post.getMessage());
			assertEquals("login", post.getLogin());
		}
		catch (ParseException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test method for {@link tifauv.jplop.board.Post#setMaxLength(int)}.
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
