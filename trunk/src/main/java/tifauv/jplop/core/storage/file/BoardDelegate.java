/**
 * 27 mars 2010
 */
package tifauv.jplop.core.storage.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tifauv.jplop.core.board.BadPostException;
import tifauv.jplop.core.board.History;
import tifauv.jplop.core.board.Post;
import tifauv.jplop.core.storage.StorageException;

/**
 * This is...
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class BoardDelegate extends FileStorage<History> {

	// CONSTANTS \\
	/** The default cache file. */
	public static final String FILE_NAME = "history.xml";
	
	
	// FIELDS \\
	/** The logger. */
	private final Logger m_logger = Logger.getLogger(BoardDelegate.class); 
	
	
	// CONSTRUCTOR \\
	public BoardDelegate(History p_history) {
		super(p_history);
	}
	
	
	// GETTERS \\
	@Override
	public String getFileName() {
		return FILE_NAME;
	}

	
	// METHODS \\
	@Override
	public void load()
	throws StorageException {
		File file = getFile();

		if (file != null && file.exists()) {
			m_logger.info("Loading the user base from '" + file + "'...");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				load(builder.parse(file));
				m_logger.info(getObject().size() + " users loaded.");
			} catch (Exception e) {
				throw new StorageException("Could not load the user base file", e);
			}
		}
		else
			m_logger.debug("The users base file does not exist.");
	}
	
	
	/**
	 * Loads the history from a DOM Document.
	 * 
	 * @param p_history
	 *            the DOM document
	 */
	private void load(Document p_history)
	throws BadPostException {
		// Load the roles
		Element board = p_history.getDocumentElement();
		if (board != null) {
			// <post> elements
			NodeList posts = board.getElementsByTagName(Post.POST_TAGNAME);
			Post post = null;
			for (int i=posts.getLength()-1; i>=0; --i) {
				post = new Post((Element)posts.item(i));
				getObject().addPost(post);
			}
			
			// We have the past post, update the id counter
			if (post != null)
				getObject().setNextId(post.getId() + 1);
		}
	}
	
	
	@Override
	public void save() {
		File file = getFile();
		if (file == null)
			return;
		
		// Create the file if needed
		if (!file.exists()) {
			try {
				file.createNewFile();
				m_logger.info("The file '" + file + "' has been created (empty).");
			} catch (IOException e) {
				m_logger.error("The file '" + file + "' could not be created.");
			}
		}

		// Check whether the file is writable
		if (!file.canWrite()) {
			m_logger.error("The history file '" + file + "' is not writable.");
			return;
		}
		
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(getObject().toString().getBytes("UTF-8"));
			m_logger.info("History saved to '" + file + "'.");
		} catch (FileNotFoundException e) {
			m_logger.error("The history file does not exist.");
		} catch (IOException e) {
			m_logger.error("Cannot write the history file", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					m_logger.error("An error occured while closing the history file.");
				}
			}
		}
	}
}
