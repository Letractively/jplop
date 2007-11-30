/** ************************************************************************** **/
/**  This file contains methods to used to manage the board.                   **/
/**                                                                            **/
/**  Version: 1.0                                                              **/
/**  Author : Olivier Serve <tifauv@gmail.com>                                 **/
/**  License: see the COPYING file                                             **/
/**  Depends: ajax.js, utils.js                                                **/
/** ************************************************************************** **/


/**
 * Adds a tag pair to the 'message' input of the post form.
 * If some text is selected, it tags the selection.
 */
function addTagToMessage(p_tagName) {
	var input = window.document.getElementById('message');
	input.focus();
	if (typeof input.selectionStart != 'undefined') {
		/* Insertion des tags */
		var start = input.selectionStart;
		var end = input.selectionEnd;
		var insText = input.value.substring(start, end);
    	input.value = input.value.substr(0, start) + '<' + p_tagName + '>' + insText + '</' + p_tagName + '>' + input.value.substr(end);

		/* Ajustement de la position du curseur */
		var pos;
		if (insText.length == 0) {
			pos = start + p_tagName.length + 2;
		}
		else {
			pos = start + p_tagName.length * 2 + 5 + insText.length;
		}
		input.selectionStart = pos;
		input.selectionEnd = pos;
	}
}


/**
 * Sends the value of the #message input field to the given
 * URL.
 *
 * @param p_url
 *            the URL to send the message
 *
 * @return true if the request has been done,
 *         false otherwise
 */
function sendMessage(p_url) {
	var input = document.getElementById('message');
	var request = input.getAttribute('name') + '=' + encodeURIComponent(input.value);
	return httpPost(p_url, request, handlePostResponse);
}


/**
 * Handles the response. In case of success (readyState == 4 and status == 202),
 * selects the input's text and calls reloadBackend();
 *
 * @param p_request
 *            the request to get its status
 */
function handlePostResponse(p_request) {
	// If request is complete...
	if (p_request.readyState == 4) {
		// And the status is 204 (NO_CONTENT)
		if (p_request.status == 204) {
			reloadBackend();
			document.getElementById('message').select();
		}
	}
}


/**
 * Scrolls the given object to the bottom.
 */
function scrollToBottom(p_obj) {
	var height = p_obj.contentDocument.getElementsByTagName('body').item(0).scrollHeight;
	p_obj.contentDocument.defaultView.scrollTo(0, height);
}


/**
 * Reloads the backend.
 */
function reloadBackend() {
	var button = document.getElementById('reloadBtn');
	var indicator = document.getElementById('reloadIndicator');
	var object = document.getElementById('board');

	if (object && object.contentDocument
				&& object.contentDocument.defaultView
				&& object.contentDocument.defaultView.location) {
		button.style.visibility = 'hidden';
		indicator.style.visibility = 'visible';
		object.contentDocument.defaultView.location.reload();
		scrollToBottom(object);
		indicator.style.visibility = 'hidden';
		button.style.visibility = 'visible';
	}
}


/**
 * Executes an XPath search on the document.
 */
function searchItems(xpath) {
	return document.evaluate(
			xpath,
			document,
			null,
			XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE,
			null);
}


/**
 * Initialization function.
 */
function initBoard() {
	// Set the focus on the message input
	document.getElementById('message').focus();
	scrollToBottom(document.getElementById('board'));
	// Auto reload the backend every 10s
 	setInterval('reloadBackend();', 30000);
}


// Auto-load the initBackend() function
addEvent(window, 'load', initBoard);