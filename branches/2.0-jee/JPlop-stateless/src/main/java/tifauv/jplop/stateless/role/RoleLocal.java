/**
 * 5 juil. 2008
 */
package tifauv.jplop.stateless.role;

import javax.ejb.Local;

import tifauv.jplop.entity.Role;

/**
 * This is the local interface to the Role bean.
 *
 * @author Olivier Serve <tifauv@gmail.com>
 *
 * @version 1.0
 */
@Local
public interface RoleLocal {

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
	Role createRole(Role p_role, String p_name);
	
	
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
	Role updateRole(Role p_role, String p_name);
	
	
	/**
	 * Deletes a role.
	 * 
	 * @param p_role
	 *            the role to delete
	 */
	void deleteRole(Role p_role);
}
