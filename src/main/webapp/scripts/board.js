/** ************************************************************************** **/
/**  This file contains methods to manage the board.                           **/
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
 * Displays or hides a block, and updates the toggle button.
 *
 * @param p_id
 *            the id of the block to display/hide
 * @param p_button
 *            the toggle button to update
 * @param p_caption
 *            the 'normal' button caption
 */
function toggleVisibility(p_id, p_button, p_caption) {
	var target = window.document.getElementById(p_id);
	
	// If hidden, display
	if (!target.style.display || target.style.display == 'none') {
		target.style.display = 'block';
		p_button.value = p_caption + ' <<';
	}
	// Else, hide
	else {
		target.style.display = 'none';
		p_button.value = p_caption + ' >>';
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
 * Handles the post response. In case of success (readyState == 4 and status == 201),
 * selects the input's text and calls reloadBackend().
 * If the message was a command (status == 200), clears the input's text and call
 * updateSettings().
 *
 * @param p_request
 *            the request to get its status
 */
function handlePostResponse(p_request) {
	// If request is complete...
	if (p_request.readyState == 4) {
		// And the status is 200 (OK)
		if (p_request.status == 200) {
			updateSettings();
			document.getElementById('message').select();
		}
		// And the status is 201 (CREATED)
		else if (p_request.status == 201) {
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
 * Updates the user's settings display.
 */
function updateSettings() {
	return httpGet('my-settings',handleSettingsResponse);
}


/**
 * Handles the updateSettings response. In case of success (readyState == 4 and status == 200),
 * update the displayed user settings.
 *
 * @param p_request
 *            the request to get its status
 */
function handleSettingsResponse(p_request) {
	// If request is complete...
	if (p_request.readyState == 4) {
		// And the status is 200 (OK)
		if (p_request.status == 200) {
			// Get the display list <UL>
			var paramsList = document.getElementById('params');
			
			// Remove its children
			while (paramsList.firstChild != null)
				paramsList.removeChild(paramsList.firstChild);
			
			var params = p_request.responseXML.getElementsByTagName('param');
			if (params.length > 0) {
				for (var i=0; i<params.length; ++i) {
					var param = document.createElement('li');
					param.textContent = params.item(i).getAttribute('name') + ' : ' + params.item(i).textContent;
					paramsList.appendChild(param);
				}
			}
		}
	}
}


/**
 * Executes an XPath search on the document.
 *
 * @param p_xpath
 *            the XPath search expression
 */
function searchItems(p_xpath) {
	return document.evaluate(
			p_xpath,
			document,
			null,
			XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE,
			null);
}


/**
 * Initialization function.
 */
function initBoard() {
	// Update the user's settings
	updateSettings();

	// Set the focus on the message input
	document.getElementById('message').focus();
	scrollToBottom(document.getElementById('board'));
	// Auto reload the backend every 10s
 	setInterval('reloadBackend();', 30000);
}


// Auto-load the initBoard() function
addEvent(window, 'load', initBoard);