<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page session="true"      %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:useBean id="backend" type="tifauv.jplop.Backend" scope="application" />
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::Accueil</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/common.css" />
	</head>
	<body>
		<%@ include file="header.inc.jsp" %>
		
		<p>Plop, heureux de vous voir ici _o/</p>

		<ul id="board-access" >
			<li><a href="board.jsp">Voir la tribune</a></li>
			<li><a href="config.jsp">Configuration</a></li>
		</ul>
		
		<h2>Authentification</h2>
		
		<form method="post" action="logon">
			<div>
				<label for="username">Login&nbsp;:</label>
				<input type="text" id="username" name="login" />
				<label for="password">Mot de passe&nbsp;:</label>
				<input type="password" id="password" name="password" />
				<input type="submit" />
			</div>
		</form>
		
		<hr/>
		
		<%@ include file="footer.inc.jsp" %>
	</body>
</html>