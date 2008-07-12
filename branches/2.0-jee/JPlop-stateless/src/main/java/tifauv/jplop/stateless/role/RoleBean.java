package tifauv.jplop.stateless.role;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tifauv.jplop.entity.Role;
import tifauv.jplop.exceptions.ValidationException;

/**
 * This is the stateless bean that manages the roles.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Stateless(name="RoleSB", mappedName="ejb/stateless/Role")
public class RoleBean implements RoleLocal {

	// FIELDS \\
	/** The Role entities manager. */
	@PersistenceContext(unitName="jplop-board")
	private EntityManager m_entities;
	
	
	// METHODS \\
	/**
	 * Initializes the given role and make it persistent.
	 * 
	 * @param p_role
	 *            the uninitialized role
	 * @param p_name
	 *            the role name
	 * 
	 * @return the initialized role
	 */
	@Override
	public Role createRole(Role p_role, String p_name) {
		if (p_role == null || p_name == null || p_name.isEmpty())
			throw new ValidationException("");

		p_role.setName(p_name);
		m_entities.persist(p_role);
		return p_role;
	}


	/**
	 * Updates a role.
	 * 
	 * @param p_role
	 *            the role
	 * @param p_name
	 *            the new role name
	 * 
	 * @return the updated role
	 */
	@Override
	public Role updateRole(Role p_role, String p_name) {
		if (p_role == null || p_name == null || p_name.isEmpty())
			throw new ValidationException("");

		p_role.setName(p_name);
		m_entities.merge(p_role);
		return p_role;
	}

	
	/**
	 * Deletes a role.
	 * 
	 * @param p_role
	 *            the role to delete
	 */
	@Override
	public void deleteRole(Role p_role) {
		if (p_role == null)
			throw new ValidationException("");

		m_entities.remove(m_entities.merge(p_role));
	}
}
