<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:useBean id="backend" type="tifauv.jplop.Backend" scope="application" />
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::Tribune</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/board.css" />
		<script type="text/javascript" src="scripts/utils.js"></script>
		<script type="text/javascript" src="scripts/ajax.js"></script>
		<script type="text/javascript" src="scripts/board.js"></script>
	</head>
	<body>
		<div id="header">
			<h1><jsp:getProperty name="backend" property="name"/> - <jsp:getProperty name="backend" property="fullName"/></h1>
			<div class="links">
				<a href="<jsp:getProperty name="backend" property="URL"/>" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>
		
		<div id="board-container">
			<object id="board" data="backend" type="text/html"></object>
		</div>
		
		<form method="post" action="post" onsubmit="javascript: return !sendMessage('post');">
			<div id="postForm">
				<div id="toolbar">
					<input type="button" onclick="javascript: addTagToMessage('b');"  value="Gras"     accesskey="g" />
					<input type="button" onclick="javascript: addTagToMessage('i');"  value="Italique" accesskey="i" />
					<input type="button" onclick="javascript: addTagToMessage('u');"  value="Souligné" accesskey="s" />
					<input type="button" onclick="javascript: addTagToMessage('s');"  value="Barré"    accesskey="b" />
					<input type="button" onclick="javascript: addTagToMessage('tt');" value="TeleType" accesskey="t" />
				</div>
				<div id="edition">
					<input type="reset" value="" />
					<input type="text"  id="message" name="message" maxlength="512" accesskey="m" />
					<input type="submit" />
					<input type="button" id="reloadBtn" onclick="javascript: reloadBackend();" value="Reload" accesskey="r" />
					<img id="reloadIndicator" src="images/reloading.gif" alt="Reloading..."/>
				</div>
			</div>
		</form>

		<hr/>
		
		<%@ include file="footer.html.inc" %>
	</body>
</html>