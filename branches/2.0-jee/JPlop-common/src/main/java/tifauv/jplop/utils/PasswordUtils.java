/**
 * 29 juil. 08
 */
package tifauv.jplop.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * This is a little password hash utility.
 *
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
public final class PasswordUtils {

	// FIELDS \\
	/** The SHA-256 message digest algorithm. */
	private static MessageDigest s_sha256;
	
	
	// CONSTRUCTORS \\
	/**
	 * This is a utility class that must not be instantiated.
	 * A private default constructor enforces that.
	 */
	private PasswordUtils() {
		// Nothing to do
	}
	
	
	// METHODS \\
	/**
	 * Builds (if needed) a SHA256 message digester.
	 */
	private static MessageDigest getSHA256() {
		if (s_sha256 == null) {
			try {
				s_sha256 = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				// Can't happen : SHA256 is part of the default SunJCE provider
			}			
		}
		return s_sha256;
	}
	
	
	/**
	 * Creates a Base64-encoded digest of some data.
	 * The digest algorithm is SHA-256.
	 * 
	 * @param p_data
	 *            the input data
	 * 
	 * @return the Base64-encoded result of a SHA-256 digest of the input data
	 */
	public static String sha256B64(String p_data) {
		String digest = null;
		try {
			byte[] hashedData = null;
			MessageDigest sha256 = getSHA256();
			synchronized (sha256) {
				hashedData = sha256.digest(p_data.getBytes("UTF-8"));
			}
			digest = new String(Base64.encodeBase64(hashedData));
		} catch (UnsupportedEncodingException e) {
			// How can this happen ? UTF-8 is built inside Java
		}
		return digest;
	}

}
