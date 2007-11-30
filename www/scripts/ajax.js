/** ************************************************************************** **/
/**  This file contains methods to do an Ajax request, either by GET or POST.  **/
/**                                                                            **/
/**  Version: 1.0                                                              **/
/**  Author : Olivier Serve <tifauv@gmail.com>                                 **/
/**  License: see the COPYING file                                             **/
/** ************************************************************************** **/


/**
 * Gets an XmlHttpRequest object. You'll rather want to use
 * httpGet() or httpPost().
 */
function newRequest() {
	var request;

	// For Gecko, Opera and WebKit/KHTML
	if (typeof(XMLHttpRequest) != 'undefined')
		request = new XMLHttpRequest()
	// For IE
	else
		request = new ActiveXObject('Microsoft.XMLHTTP');
	
	return request;
}


/**
 * Sends a GET request.
 *
 * @param p_url
 *            the URL to send the request to
 * @param p_callback
 *            the function to call when to process the response
 *
 * @return true if the request has been sent,
 *         false otherwise
 */
function httpGet(p_url, p_callback) {
	var callback = p_callback;
	var xmlhttp = newRequest();

	xmlhttp.open('GET', p_url, true);
	xmlhttp.setRequestHeader('Accept', 'text/xml');

	function bindCallback() {
		if (callback)
			callback(xmlhttp);
		else
			alert('No callback defined');
	}

	if (xmlhttp) {
		// bind the callback
		xmlhttp.onreadystatechange = bindCallback;

		// send the request
		xmlhttp.send(null);
		return true;
	}
	return false;
}


/**
 * Sends a POST request.
 *
 * @param p_url
 *            the URL to send the request to
 * @param p_request
 *            the request data
 * @param p_callback
 *            the function to call when to process the response
 *
 * @return true if the request has been sent,
 *         false otherwise
 */
function httpPost(p_url, p_request, p_callback) {
	var callback = p_callback;
	var xmlhttp = newRequest();

	xmlhttp.open('POST', p_url, true);
	xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xmlhttp.setRequestHeader('Accept'      , 'text/xml');

	function bindCallback() {
		if (callback)
			callback(xmlhttp);
		else
			alert('No callback defined');
	}

	if (xmlhttp) {
		// bind the callback
		xmlhttp.onreadystatechange = bindCallback;

		// send the request
		xmlhttp.send(p_request);
		return true;
	}
	return false;
}
