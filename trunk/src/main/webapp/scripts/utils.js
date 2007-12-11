/** ************************************************************************** **/
/**  This file contains utility methods.                                       **/
/**                                                                            **/
/**  Version: 1.0                                                              **/
/**  Author : Olivier Serve <tifauv@gmail.com>                                 **/
/**  License: see the COPYING file                                             **/
/** ************************************************************************** **/


/**
 * Add an event listener, whatever the browser.
 *
 * @param p_element
 *            the element on which to set the event
 * @param p_eventType
 *            the type of event 
 * @param p_function
 *            the function to call when the event is triggered
 */
function addEvent(p_element, p_eventType, p_function) {
	var addEvent;
	if (p_element.addEventListener)
		addEvent = myAddEventListener;
	else if (obj.attachEvent)
		addEvent = myAddEventAttach;
	else
		addEvent = myAddEventOld;
	addEvent(p_element, p_eventType, p_function);
}


/**
 * Add an event listener for DOM2 browsers.
 *
 * @param p_element
 *            the element on which to set the event
 * @param p_eventType
 *            the type of event 
 * @param p_function
 *            the function to call when the event is triggered
 */
function myAddEventListener(p_element, p_eventType, p_function) {
	p_element.addEventListener(p_eventType, p_function, false);
}


/**
 * Add an event listener for Internet Explorer <= 6.
 *
 * @param p_element
 *            the element on which to set the event
 * @param p_eventType
 *            the type of event 
 * @param p_function
 *            the function to call when the event is triggered
 */
function myAddEventAttach(p_element, p_eventType, p_function) {
	p_element.attachEvent('on' + p_eventType, p_function);
}


/**
 * Add an event listener for old browsers.
 *
 * @param p_element
 *            the element on which to set the event
 * @param p_eventType
 *            the type of event 
 * @param p_function
 *            the function to call when the event is triggered
 */
function myAddEventOld(p_element, p_eventType, p_function) {
    var oldEventListener = p_element['on' + p_eventType];
    if (typeof oldEventListener != 'function') {
        p_element['on' + p_eventType] = p_function;
    }
    else {
        p_element['on' + p_eventType] = function() {
            oldEventListener();
            p_function();
        }
    }
}