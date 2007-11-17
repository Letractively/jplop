<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page import="tifauv.jplop.model.Backend" %>
<%! String name = Backend.getInstance().getName(); %>
<%! String fullname = Backend.getInstance().getFullName(); %>
<%! String url = Backend.getInstance().getURL(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><%= name %>::Board</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/board.css" />
		<script type="text/javascript" src="scripts/ajax.js"></script>
		<script type="text/javascript" src="scripts/board.js"></script>
	</head>
	<body>
		<div id="header">
			<h1><%= name %> - <%= fullname %></h1>
			<div class="links">
				<a href="<%= url %>" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>
		
		<div id="board">
			<object id="backend" data="backend" type="text/html"></object>
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
					<input type="button" onclick="javascript: reloadBackend();" value="Reload" accesskey="r" />
				</div>
			</div>
		</form>

		<hr/>
		
		<%@ include file="footer.html.inc" %>
	</body>
</html>