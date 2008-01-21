/**
 * Dec 20, 2007
 */
package tifauv.jplop.auth;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * The Salted SHA1 password algorithm implementation.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class SSHAPassword implements Password {

	// CONSTANTS \\
	/** The secure SHA1 password prefix. */
	public static final String SSHA_PREFIX = "{SSHA}";
	
	/** The length of the salt data. */
	public static final int SSHA_SALT_LENGTH = 6;
	
	/** The length of a SHA1 hash. */
	public static final int SHA1_LENGTH = 20;

	
	// FIELDS \\
	/** The salt generator. */
	private static Random s_saltGen;
	
	/** The password salt. */
	private byte[] m_salt;
	
	/** The password hash. */
	private byte[] m_hash;
	

	// GETTERS \\
	/**
	 * Gives the {@link #SSHA_PREFIX}ed string representation of this password
	 * 
	 * @return the string representation of the password,
	 *         or <code>null</code> if no password has been set yet
	 */
	public String getPassword() {
		if (m_hash != null && m_salt != null) {
			byte[] password = Arrays.copyOf(m_hash, m_hash.length + m_salt.length);
			System.arraycopy(m_salt, 0, password, m_hash.length, m_salt.length);
			return SSHA_PREFIX + new String(Base64.encodeBase64(password));
		}
		return null;
	}
	
	
	// SETTERS \\
	/**
	 * Decodes the password from a Base64 form (with or without SSHA prefix)
	 * and sets it.
	 * 
	 * @throws PasswordException
	 *            if the given parameter is not a Salted SHA
	 */
	public void setPassword(String p_password)
	throws PasswordException {
		if (p_password == null || p_password.length() == 0) {
			m_salt = null;
			m_hash = null;
		}
		else if (p_password.startsWith(SSHA_PREFIX)) {
			try {
				// Decoding to ASCII is sufficient because of the Base64 alphabet
				byte[] binPswWithSalt = Base64.decodeBase64(p_password.substring(SSHA_SALT_LENGTH).getBytes("US-ASCII"));
				// If we have a salt
				if (binPswWithSalt.length > SHA1_LENGTH) {
					int saltLength = binPswWithSalt.length - SHA1_LENGTH;
					
					byte[] salt = new byte[saltLength];
					System.arraycopy(binPswWithSalt, SHA1_LENGTH, salt, 0, saltLength);

					byte[] hash = new byte[SHA1_LENGTH];
					System.arraycopy(binPswWithSalt, 0, hash, 0, SHA1_LENGTH);
					
					m_salt = salt;
					m_hash = hash;
				}
				else
					throw new PasswordException("The given password is too short to be Salted SHA");
			}
			catch (UnsupportedEncodingException e) {
				throw new PasswordException("The given password could not be decoded as US-ASCII", e);
			}
		}
		else {
			try {
				byte[] salt = generateSalt();
				byte[] hash = hashPassword(salt, p_password);
				
				m_salt = salt;
				m_hash = hash;
			}
			catch (UnsupportedEncodingException e) {
				throw new PasswordException("The given password could not be decoded as UTF-8", e);
			}
		}
	}
	
	
	// METHODS \\
	/**
	 * Gives the generator of SSHA salts.
	 * Builds it if needed.
	 */
	private static Random getSaltGenerator() {
		if (s_saltGen == null)
				s_saltGen = new Random();
		return s_saltGen;
	}
	
	
	/**
	 * Generates a SSHA salt.
	 */
	private static byte[] generateSalt() {
		byte[] salt = new byte[SSHA_SALT_LENGTH];
		getSaltGenerator().nextBytes(salt);
		return salt;
	}

	
	/**
	 * Hashes a password with the given salt.
	 * The hash algorithm is a Salted SHA1.
	 */
	private byte[] hashPassword(byte[] p_salt, String p_password)
	throws UnsupportedEncodingException {
		byte[] binPassword = p_password.getBytes("UTF-8");
		byte[] saltedPassword = Arrays.copyOf(binPassword, binPassword.length + p_salt.length);
		System.arraycopy(p_salt, 0, saltedPassword, binPassword.length, p_salt.length);
		return DigestUtils.sha(saltedPassword);
	}	


	/**
	 * Checks the given password matches this user's one.
	 */
	public boolean check(String p_password) {
		if (p_password == null)
			return m_hash == null;
		else if (m_salt != null) {
			try {
				return Arrays.equals(hashPassword(m_salt, p_password), m_hash);
			}
			catch (UnsupportedEncodingException e) {
				// UTF-8 is not supported ?!?
			}
		}
		return false;
	}
}
