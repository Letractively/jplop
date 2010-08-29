/** ************************************************************************** **/
/**  This file contains methods to used to manage the backend frame.           **/
/**                                                                            **/
/**  Version: 1.0                                                              **/
/**  Author : Olivier Serve <tifauv@gmail.com>                                 **/
/**  License: see the COPYING file                                             **/
/**  Depends: utils.js                                                         **/
/** ************************************************************************** **/


/**
 * Adds a string to the 'message' input of the post form.
 */
function addStrToMessage(p_string) {
	var input = window.top.document.getElementById('message');
	input.focus();
	
	// Gecko and the like...
	if (typeof input.selectionStart != 'undefined') {
		/* Insertion de la chaine */
		var start = input.selectionStart;
		var end = input.selectionEnd;
		input.value = input.value.substr(0, start) + p_string + input.value.substr(end);

		/* Ajustement de la position du curseur */
		var pos = start + p_string.length;
		input.selectionStart = pos;
		input.selectionEnd = pos;
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
 * Initialization function. Tags the clock references
 * and sets the mouse event listeners over the clocks and clock references.
 */
function initBackend() {
	var clockRegex = /(^| )(((?:[01]?[0-9])|(?:2[0-3]))(:[0-5][0-9])(:[0-5][0-9])(?:(?:[\\^:][0-9])|\\xB9|\\xB2|\\xB3)?(?:@[0-9A-Za-z]+)?)/g;

	//add_highlight_div();

	// If authenticated, get the current username (will be null otherwise)
	var user = null;
	var userElement = window.top.document.getElementById('currentUser');
	if (userElement != null)
		user = userElement.innerHTML + '&gt;';

	// Find the clock references in the messages
	var items = searchItems("//*[@class='post']/*[@class='message']");
	for (var i=0; i<items.snapshotLength; ++i) {
		item = items.snapshotItem(i);
		var message = item.innerHTML
				.replace(/&lt;/g, '<')
				.replace(/&gt;/g, '>')
				.replace(/&amp;/g, '&')
				.replace(/&quot;/g, '"');
		item.innerHTML = message.replace(clockRegex, '$1<span class="ref">$2</span>');

		// If we are authenticated, apply a special style for my messages
		if (user != null) {
			var spans = item.parentNode.getElementsByTagName('span');
			for (var j=0; j<spans.length; ++j) {
				var span = spans.item(j);
				if (span.className == 'login' && span.innerHTML == user)
					item.parentNode.className += ' my';
			}
		}
	}
	
	// Add event listeners message -> responses
	var items = searchItems("//*[contains(@class, 'clock')]");
	for (var i=0; i<items.snapshotLength; ++i) {
		var item = items.snapshotItem(i);
		var highlightFn = function (event) {
				highlightPost(event.target, 'highlighted');
				//GFloatPanel.style.display='none'; 
			};
		addEvent(item, 'mouseover', highlightFn);

		var unhighlightFn = function (event) {
				highlightPost(event.target, '');
				//GFloatPanel.style.display='none';
			};
		addEvent(item, 'mouseout', unhighlightFn);
	}
	
	// Add event listeners references -> message
	items = searchItems("//*[contains(@class, 'ref')]");
	for (var i=0; i<items.snapshotLength; ++i) {
		var item = items.snapshotItem(i);
		var highlightFn = function (event) { 
				highlightRef(event.target, 'highlighted'); 
				//if (GPageYOffset <= window.pageYOffset) 
				//	GFloatPanel.style.display='block'; 
			};
		addEvent(item, 'mouseover', highlightFn);

		var unhighlightFn = function (event) { 
				highlightRef(event.target, '');
				//GFloatPanel.style.display='none'; 
			};
		addEvent(item, 'mouseout', unhighlightFn);
	}
}


/**
 * Highlights a post and its clock-references.
 *
 * @param p_clock
 *            the clock item
 * @param p_class
 *            the class to add to the elements
 */
function highlightPost(p_clock, p_class) {
	// Extract the clock from the given clock [hh:mm:ss]
	var clockValue = p_clock.innerHTML.substring(1, 9);
	
	// Highlight the post
	var className = p_clock.parentNode.className;
	var newClassName = 'post';
	if (className.indexOf('my') > -1)
		newClassName += ' my';
	newClassName += ' ' + p_class;
	p_clock.parentNode.className = newClassName;

	// Highlight the clock-references
	var refs = searchItems("//*[contains(@class, 'ref')]");
	for (var i=0; i<refs.snapshotLength; ++i) {
		var ref = refs.snapshotItem(i);
		if (ref.innerHTML.indexOf(clockValue) > -1)
			ref.className = 'ref ' + p_class;
	}
}


/**
 * Highlights the referenced post and the similar references.
 *
 * @param p_ref
 *            the ref item
 * @param p_class
 *            the class to add to the elements
 */
function highlightRef(p_ref, p_class) {
	// Extract the clock from the given reference
	var clockValue = p_ref.innerHTML;
	
	// Highlight the referenced post
	var clocks = searchItems("//*[contains(@class, 'clock')]");
	for (var i=0; i<clocks.snapshotLength; ++i) {
		var clock = clocks.snapshotItem(i);
		if (clock.innerHTML.indexOf(clockValue) > -1) {
			var className = clock.parentNode.className;
			var newClassName = 'post';
			if (className.indexOf('my') > -1)
				newClassName += ' my';
			newClassName += ' ' + p_class;
			clock.parentNode.className = newClassName;
		}
	}
	
	// Highlight the same references
	var refs = searchItems("//*[contains(@class, 'ref')]");
	for (var i=0; i<refs.snapshotLength; ++i) {
		var ref = refs.snapshotItem(i);
		if (ref.innerHTML.indexOf(clockValue) > -1)
			ref.className = 'ref ' + p_class;
	}
}


// Auto-load the initBackend() function
addEvent(window, 'load', initBackend);