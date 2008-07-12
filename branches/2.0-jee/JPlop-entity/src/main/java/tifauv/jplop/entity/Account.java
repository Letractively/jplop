/**
 * 5 juil. 2008
 */
package tifauv.jplop.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import tifauv.jplop.exceptions.PasswordException;
import tifauv.jplop.exceptions.ValidationException;

/**
 * This is a user.
 * A user has a unique identifier (automatically generated),
 * a login (that can be changed) and roles.
 * The roles tell what a user can do within the application.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Entity
public final class Account implements Serializable {

	// CONSTANTS \\
	/** The serialization identifier. */
	private static final long serialVersionUID = 4191199079107454445L;

	/** The secure SHA1 password prefix. */
	public static final String SSHA_PREFIX = "{SSHA}";
	
	/** The length of the salt data. */
	public static final int SSHA_SALT_LENGTH = 6;
	
	/** The length of a SHA1 hash. */
	public static final int SHA1_LENGTH = 20;

	
	// FIELDS \\
	/** The salt generator. */
	private static Random s_saltGen;
	
	/** The user unique identifier (an integer). */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int m_id;
	
	/** The user login. */
	@Column(name="login", nullable=false)
	private String m_login;
	
	/** The user password. */
	@Column(name="password", nullable=false)
	private String m_password;
	
	/** The password salt. */
	@Transient
	private byte[] m_passwordSalt;
	
	/** The password hash. */
	@Transient
	private byte[] m_passwordHash;
   
	/** The user roles. */
	@OneToMany
	private Set<Role> m_roles;
	
	/** The user's posts. */
	@OneToMany(mappedBy="m_author")
	@JoinColumn(name="user_id")
	private List<Post> m_posts;
	
	
	// GETTERS \\
	/**
	 * Gives the user unique identifier. 
	 */
	public int getId() {
		return m_id;
	}
	
	
	/**
	 * Gives the user login.
	 */
	public String getLogin() {
		return m_login;
	}
	
	
	/**
	 * Gives the user password.
	 */
	public String getPassword() {
		return m_password;
	}
	

	/**
	 * Gives the decoded password salt.
	 */
	public byte[] getPasswordSalt() {
		return m_passwordSalt;
	}
	
	
	/**
	 * Gives the decoded password hash.
	 */
	public byte[] getPasswordHash() {
		return m_passwordHash;
	}
	
	
	/**
	 * Tells whether the user has the given role.
	 * 
	 * @param p_role
	 *            the role 
	 * 
	 * @return <tt>true</tt> iff the user has the given role
	 */
	public boolean hasRole(Role p_role) {
		return m_roles.contains(p_role);
	}
	
	
	/**
	 * Gives all the user's posts.
	 */
	public List<Post> getPosts() {
		return m_posts;
	}
	
	
	// SETTERS \\
	/**
	 * Sets the user's login.
	 */
	public void setLogin(String p_login) {
		m_login = p_login;
	}
	
	
	/**
	 * Sets the user's password.
	 */
	public void setPassword(String p_password) {
		m_password = p_password;
	}
	
	
	/**
	 * Adds a role to the user.
	 * 
	 * @param p_role
	 *            the role to add
	 */
	public void addRole(Role p_role) {
		m_roles.add(p_role);
	}
	
	
	/**
	 * Removes a role from the user.
	 * 
	 * @param p_role
	 *            the role to remove
	 */
	public void removeRole(Role p_role) {
		m_roles.remove(p_role);
	}
	
	
	// METHODS \\
	/**
	 * Validates the data before storing the entity.
	 * We just check that the login is not null or empty.
	 */
	@PrePersist
	@PreUpdate
	protected void validateData() {
		if (getLogin() == null || getLogin().trim().isEmpty())
			throw new ValidationException("The user login cannot be null or empty");

		if (getPassword() == null || getPassword().trim().isEmpty())
			throw new ValidationException("The user password cannot be null or empty");
	}

	
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
					
					m_passwordSalt = salt;
					m_passwordHash = hash;
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
	 * @return
	 */
	public boolean matchPassword(String p_password) {
		if (p_password == null)
			return getPasswordHash() == null;
		try {
			return Arrays.equals(hashPassword(getPasswordSalt(), p_password), getPasswordHash());
		} catch (UnsupportedEncodingException e) {
			throw new PasswordException("UTF-8 is not supported", e);
		}
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
	
	
	/**
	 * Generates a SSHA salt.
	 */
	private static byte[] generateSalt() {
		byte[] salt = new byte[Account.SSHA_SALT_LENGTH];
		getSaltGenerator().nextBytes(salt);
		return salt;
	}

	
	public void hashPassword(String p_password) {
		try {
			byte[] salt = generateSalt();
			byte[] hash = hashPassword(salt, p_password);
			byte[] password = Arrays.copyOf(hash, hash.length + salt.length);
			System.arraycopy(salt, 0, password, hash.length, salt.length);
			setPassword(Account.SSHA_PREFIX + new String(Base64.encodeBase64(password)));
		} catch (UnsupportedEncodingException e) {
			throw new PasswordException("The given password could not be decoded as UTF-8", e);
		}
	}

	
	private byte[] hashPassword(byte[] p_salt, String p_password)
	throws UnsupportedEncodingException {
		byte[] binPassword = p_password.getBytes("UTF-8");
		byte[] saltedPassword = Arrays.copyOf(binPassword, binPassword.length + p_salt.length);
		System.arraycopy(p_salt, 0, saltedPassword, binPassword.length, p_salt.length);
		return DigestUtils.sha(saltedPassword);
	}

	
	/**
	 * A User is equal to an object o if and only if
	 * o is another non-null User with the same id.
	 */
	@Override
	public boolean equals(Object p_object) {
		return p_object != null
				&& p_object instanceof Account
				&& getId() == ((Account)p_object).getId();
	}
	
	
	/**
	 * Returns the following string "<login>".
	 */
	@Override
	public String toString() {
		return getLogin();
	}
	
	
	/**
	 * The hashcode is the user's identifier.
	 */
	@Override
	public int hashCode() {
		return getId();
	}
}
