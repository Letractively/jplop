package tifauv.jplop.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import javax.persistence.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import tifauv.jplop.exceptions.PasswordException;

/**
 * Entity implementation class for Entity: Password
 *
 */
@Embeddable
public class Password implements Serializable {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = -6217705281512375260L;

	/** The secure SHA1 password prefix. */
	public static final String SSHA_PREFIX = "{SSHA}";
	
	/** The length of the salt data. */
	public static final int SSHA_SALT_LENGTH = 6;
	
	/** The length of a SHA1 hash. */
	public static final int SHA1_LENGTH = 20;

	
	// FIELDS \\
	/** The salt generator. */
	private static Random s_saltGen;
	
	/** The user password. */
	@Column(name="password", nullable=false)
	private String m_password;
	
	/** The password salt. */
	@Transient
	private byte[] m_salt;
	
	/** The password hash. */
	@Transient
	private byte[] m_hash;

	public Password() {
		super();
	}
   
	
	
	// GETTERS \\
	/**
	 * Gives the user password.
	 */
	public String getPassword() {
		return m_password;
	}
	

	/**
	 * Gives the decoded password salt.
	 */
	private byte[] getSalt() {
		return m_salt;
	}
	
	
	/**
	 * Gives the decoded password hash.
	 */
	private byte[] getHash() {
		return m_hash;
	}

	
	/**
	 * Gives the generator of SSHA salts.
	 * Builds it if needed.
	 */
	private static Random getSaltGenerator() {
		if (s_saltGen == null)
			s_saltGen = new Random();
		return s_saltGen;
	}
	
	
	// SETTERS \\
	public void setPassword(String p_password) {
		m_password = p_password;
	}
	
	
	private void setSalt(byte[] p_salt) {
		m_salt = p_salt;
	}
	
	
	private void setHash(byte[] p_hash) {
		m_hash = p_hash;
	}
	
	
	// METHODS \\
	/**
	 * Extracts the password salt and hash.
	 */
	@PostLoad
	@PostPersist
	@PostUpdate
	public void explodePassword() {
		if (getPassword().startsWith(SSHA_PREFIX)) {
			try {
				// Decoding to ASCII is sufficient because of the Base64 alphabet
				byte[] binPswWithSalt = Base64.decodeBase64(getPassword().substring(SSHA_SALT_LENGTH).getBytes("US-ASCII"));
				// If we have a salt
				if (binPswWithSalt.length > SHA1_LENGTH) {
					int saltLength = binPswWithSalt.length - SHA1_LENGTH;
					
					byte[] salt = new byte[saltLength];
					System.arraycopy(binPswWithSalt, SHA1_LENGTH, salt, 0, saltLength);

					byte[] hash = new byte[SHA1_LENGTH];
					System.arraycopy(binPswWithSalt, 0, hash, 0, SHA1_LENGTH);
					
					setSalt(salt);
					setHash(hash);
				}
				else
					throw new PasswordException("The given password is too short to be Salted SHA");
			} catch (UnsupportedEncodingException e) {
				throw new PasswordException("The given password could not be decoded as US-ASCII", e);
			}
		}

	}
	
	
	/**
	 * Checks if the given password matches the user's one.
	 * 
	 * @param p_password
	 *            the password to check
	 * 
	 * @return <tt>true</tt> iff the password matches
	 */
	public boolean matchPassword(String p_password) {
		if (p_password == null)
			return getHash() == null;
		try {
			return Arrays.equals(hashPassword(getSalt(), p_password), getHash());
		} catch (UnsupportedEncodingException e) {
			throw new PasswordException("UTF-8 is not supported", e);
		}
	}
	
	
	/**
	 * Generates a SSHA salt of {@link #SSHA_SALT_LENGTH} bytes.
	 */
	private static byte[] generateSalt() {
		byte[] salt = new byte[SSHA_SALT_LENGTH];
		getSaltGenerator().nextBytes(salt);
		return salt;
	}

	
	/**
	 * 
	 * @param p_password
	 *            the cleartext password
	 */
	public void hashPassword(String p_password) {
		try {
			byte[] salt = generateSalt();
			byte[] hash = hashPassword(salt, p_password);
			byte[] password = Arrays.copyOf(hash, hash.length + salt.length);
			System.arraycopy(salt, 0, password, hash.length, salt.length);
			setPassword(SSHA_PREFIX + new String(Base64.encodeBase64(password)));
		} catch (UnsupportedEncodingException e) {
			throw new PasswordException("The given password could not be decoded as UTF-8", e);
		}
	}

	
	/**
	 * Gives the hash of the given password with the given salt.
	 * <p>The password is first converted to a byte array (using UTF-8).
	 * Then the given salt is appended to that byte array.
	 * Eventually, the array is digested with SHA1.</p>
	 *  
	 * @param p_salt
	 *            the salt data
	 * @param p_password
	 *            the cleartext password
	 * 
	 * @return sha1(password + salt)
	 * 
	 * @throws UnsupportedEncodingException
	 *            if UTF-8 is not supported
	 */
	private byte[] hashPassword(byte[] p_salt, String p_password)
	throws UnsupportedEncodingException {
		byte[] binPassword = p_password.getBytes("UTF-8");
		byte[] saltedPassword = Arrays.copyOf(binPassword, binPassword.length + p_salt.length);
		System.arraycopy(p_salt, 0, saltedPassword, binPassword.length, p_salt.length);
		return DigestUtils.sha(saltedPassword);
	}
}
