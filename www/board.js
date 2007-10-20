/**
 *
 */
function cleanMessage() {
	var input = document.getElementById('message');
	input.value = '';
	input.focus();
}


/**
 *
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
 *
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