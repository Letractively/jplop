/** ************************************************************************** **/
/**  This file contains methods to used to manage the backend frame.           **/
/**                                                                            **/
/**  Version: 1.0                                                              **/
/**  Author : Olivier Serve <tifauv@gmail.com>                                 **/
/**  License: see the COPYING file                                             **/
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

		// Special style for my messages
		/*for (var j=0; j<GMyPosts.length; ++j) {
			if (item.innerHTML == GMyPosts[j]) {
				item.style.color = MyHorlogeColor;
			}
		}*/
	}
    
	// Special highlight for 'my' messages
	/*items = searchItems("//*[@class='login']");
	for (var i=0; i<items.snapshotLength; ++i) {
		var item = items.snapshotItem(i);
		if (item.innerHTML == GLogin) {
			item.className = 'login my';
		}
	}*/
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
	p_clock.parentNode.className = 'post ' + p_class;
	
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
		if (clock.innerHTML.indexOf('[' + clockValue + ']') > -1)
			clock.parentNode.className = 'post ' + p_class;
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