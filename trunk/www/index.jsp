<%@ page contentType="text/html" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page import="tifauv.jboard.model.Backend" %>
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>JBoard</title>
		<link rel="stylesheet" type="text/css" href="index.css" />
	</head>
	<body>
		<div id="header">
			<h1>JBoard - Da J2EE Board</h1>
		</div>
		
		<p>Ceci est une tribune 100% backend-oriented.
		La présentation pour les navigateurs est réalisée par une feuille de style XSLT.</p>

		<div id="board-access" >
			<a href="backend">Voir la tribune</a>
		</div>

<%! String url = Backend.getInstance().getURL(); %>
		<fieldset id="config">
			<legend>Configuration</legend>
			<ul>
				<li>URL du backend&nbsp;: <tt><%=  url %>/backend</tt></li>
				<li>URL de post&nbsp;: <tt><%=  url %>/post</tt></li>
				<li>Champ du message&nbsp;: <tt>message</tt></li>
				<li>Taille max. de l'historique&nbsp;: <%= Backend.getInstance().getMaxSize() %> posts</li>
				<li>Taille max. d'un post&nbsp;: <%= Backend.getInstance().getMaxPostLength() %> caractères</li>
			</ul>
		</fieldset>
		
		<hr/>
		
		<div id="footer">Cette tribune est dédiée à la mémoire de Pierre Tramo.</div>
	</body>
</html>