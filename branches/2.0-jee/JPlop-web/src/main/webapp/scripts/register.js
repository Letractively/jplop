/** ************************************************************************** **/
/**  This file contains methods to manage the registration page.               **/
/**                                                                            **/
/**  Version: 1.0                                                              **/
/**  Author : Olivier Serve <tifauv@gmail.com>                                 **/
/**  License: see the COPYING file                                             **/
/**  Depends: utils.js                                                         **/
/** ************************************************************************** **/

/**
 * Initialization function.
 */
function initRegister() {
	// Set the focus on the username input
	document.getElementById('username').focus();
}


// Auto-load the initRegister() function
addEvent(window, 'load', initRegister);