/**
 * Adds a string to the 'message' input of the post form.
 */
function addStrToMessage(p_string) {
	var input = document.getElementById('message');
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
 * Adds a tag pair to the 'message' input of the post form.
 * If some text is selected, it tags the selection.
 */
function addTagToMessage(p_tagName) {
	var input = document.getElementById('message');
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
 * Gets an XmlHttpRequest object. This method is not meant
 * to be called directly. Use httpPost() instead.
 *
 * @param p_url
 *            the remote URL to call
 */
function getRequest(p_url) {
	var request;

	// For Gecko, Opera and WebKit/KHTML
	if (typeof(XMLHttpRequest) != 'undefined')
		request = new XMLHttpRequest()
	// For IE
	else
		request = new ActiveXObject('Microsoft.XMLHTTP');

	request.open('POST', p_url, true);
	request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	request.setRequestHeader('Accept'      , 'text/xml');
	return request;
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
	var xmlhttp = getRequest(p_url);

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
	alert(request);
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
		// And the status is 202 (ACCEPTED)
		if (p_request.status == 202) {
			document.getElementById('message').select();
			reloadBackend();
		}
	}
}


/**
 * Reloads the backend.
 */
function reloadBackend() {
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
function load() {
	var clockRegex = /(((?:[01]?[0-9])|(?:2[0-3]))(:[0-5][0-9])(:[0-5][0-9])(?:(?:[\\^:][0-9])|\\xB9|\\xB2|\\xB3)?(?:@[0-9A-Za-z]+)?)/g;

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
		item.innerHTML = message.replace(clockRegex, '<span class="ref">$1</span>');
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
	
	// Set the focus on the message input
	document.getElementById('message').focus();
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


// Auto-load the load() function
addEvent(window, 'load', load);