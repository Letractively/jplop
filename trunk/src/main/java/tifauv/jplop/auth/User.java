/**
 * 04 dec. 2007
 */
package tifauv.jplop.auth;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * The description of a user.
 *
 * @version 1.0
 *
 * @author Olivier Serve <tifauv@gmail.com>
 */
public class User {

	// CONSTANTS \\
	/** The name of the secure random used to generate the seed. */
	private static final String SEED_GENERATOR = "SHA1PRNG";
	
	/** The secure SHA1 password prefix. */
	public static final String SSHA_PREFIX = "{ssha}";
	
	/** The length of the seed data. */
	public static final int SSHA_SEED_LENGTH = 6;
	
	
	// FIELDS \\
	/** The seed generator. */
	private static SecureRandom s_seedGen;
	
	/** The user's login. */
	private String m_login;
	
	/** The user's email address. */
	private String m_email;
	
	/** The user's password. */
	private byte[] m_password;
	
	private Collection<String> m_roles;

	
	// GETTERS \\
	/**
	 * Gives the user's login.
	 */
	public String getLogin() {
		return m_login;
	}

	
	/**
	 * Gives the user's email address.
	 */
	public String getEmail() {
		return m_email;
	}

	
	/**
	 * Gives the user's password.
	 */
	public byte[] getPassword() {
		return m_password;
	}
	

	/**
	 * Gives the Seeded SHA version of the password.
	 */
	public String getSSHAPassword() {
		return SSHA_PREFIX + new String(Base64.encodeBase64(getPassword()));
	}


	/**
	 * Gives the user's roles (comma-separated list).
	 */
	public String getRoles() {
		StringBuffer buffer = new StringBuffer();
		for (String role : m_roles)
			buffer.append(role).append(" ");
		return buffer.toString();
	}
	

	// SETTERS \\
	/**
	 * @param p_login
	 *            the user's login
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}

	
	/**
	 * @param p_email
	 *            the user's email address
	 */
	public void setEmail(String p_email) {
		m_email = p_email;
	}
	
	
	/**
	 * Sets the raw password.
	 *
	 * @param p_password
	 *            the user's password
	 */
	public void setPassword(byte[] p_password) {
		m_password = p_password;
	}
	
	
	/**
	 * Decodes the password from a Base64 form (with or without SSHA prefix)
	 * and sets it.
	 */
	public void setPassword(String p_password)
	throws UnsupportedEncodingException {
		if (p_password.startsWith(SSHA_PREFIX))
			setPassword(Base64.decodeBase64(p_password.substring(SSHA_SEED_LENGTH).getBytes("UTF-8")));
		else
			setPassword(Base64.decodeBase64(p_password.getBytes("UTF-8")));
	}

	
	// METHODS
	/**
	 * Generates a hashed password from the given value.
	 */
	public void generatePassword(String p_password)
	throws UnsupportedEncodingException {
		setPassword(hashPassword(p_password));
	}
	
	
	/**
	 * If the given password is prefixed with {@link #SSHA_PREFIX},
	 * assumes it is hashed using Seeded SHA1. Otherwise, take it as plain text.
	 * 
	 * @param p_password
	 * 
	 * @throws UnsupportedEncodingException
	 */
	protected byte[] hashPassword(String p_password)
	throws UnsupportedEncodingException {
		return hashPassword(generateSeed(), p_password);
	}
	
	
	/**
	 * Hashes a password with the given seed.
	 * The hash algorithm is a Seeded SHA1.
	 */
	protected byte[] hashPassword(byte[] p_seed, String p_password)
	throws UnsupportedEncodingException {
		byte[] binPassword = p_password.getBytes("UTF-8");
		byte[] seededPassword = Arrays.copyOf(p_seed, p_seed.length + binPassword.length);
		System.arraycopy(binPassword, 0, seededPassword, p_seed.length, binPassword.length);
		byte[] passwordDigest = DigestUtils.sha(seededPassword);
		byte[] seededPasswordDigest = Arrays.copyOf(p_seed, p_seed.length + passwordDigest.length);
		System.arraycopy(passwordDigest, 0, seededPasswordDigest, p_seed.length, passwordDigest.length);
		return seededPasswordDigest;
	}	


	/**
	 * Gives the generator of SSHA seeds.
	 * Builds it if needed.
	 */
	private static SecureRandom getSeedGenerator() {
		if (s_seedGen == null) {
			try {
				s_seedGen = SecureRandom.getInstance(SEED_GENERATOR);
			}
			catch (NoSuchAlgorithmException e) {
				// Mouhahahaha, SHA1 unknown, hahahaha !
			}
		}
		return s_seedGen;
	}
	
	
	/**
	 * Generates a SSHA seed.
	 */
	public static byte[] generateSeed() {
		byte[] seed = new byte[SSHA_SEED_LENGTH];
		getSeedGenerator().nextBytes(seed);
		return seed;
	}
	
	
	/**
	 * Checks the given password matches this user's one.
	 */
	public boolean checkPassword(String p_password) {
		try {
			byte[] seed = Arrays.copyOf(getPassword(), SSHA_SEED_LENGTH);
			byte[] given = hashPassword(seed, p_password);
			return given.equals(getPassword());
		}
		catch (UnsupportedEncodingException e) {
			// UTF-8 is not supported ?!?
		}
		return false;
	}
	
	
	/**
	 * Sets the roles.
	 *
	 * @param p_roles
	 *            a comma-separated list of roles
	 */
	public void setRoles(String p_roles) {
		clearRoles();
		addRoles(p_roles);
	}
	

	/**
	 * Add the roles defined in the given comma-separated list.
	 *
	 * @param p_roles
	 *            a comma-separated list of roles
	 */
	public void addRoles(String p_roles) {
		String[] roles = p_roles.split(",");
		for (String role : roles)
			addRole(role.trim());
	}
	

	/**
	 * Removes all the roles of this user.
	 */
	public void clearRoles() {
		m_roles.clear();
	}
	

	/**
	 * Adds a role to this user.
	 *
	 * @param p_role
	 *            the role to add
	 */
	public void addRole(String p_role) {
		m_roles.add(p_role);
	}
}
