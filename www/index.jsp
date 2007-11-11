<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page import="tifauv.jboard.model.Backend" %>
<%! String name = Backend.getInstance().getName(); %>
<%! String fullname = Backend.getInstance().getFullName(); %>
<%! String url = Backend.getInstance().getURL(); %>
<%! int maxPostLength = Backend.getInstance().getMaxPostLength(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><%= name %>::Accueil</title>
		<link rel="stylesheet" type="text/css" href="styles/index.css" />
	</head>
	<body>
		<div id="header">
			<h1><%= name %> - <%= fullname %></h1>
		</div>
		
		<p>Ceci est une tribune 100% backend-oriented.
		La présentation pour les navigateurs est réalisée par une feuille de style XSLT.</p>

		<ul id="board-access" >
			<li><a href="backend">Voir la tribune</a></li>
			<li><a href="config.jsp">Configuration</a></li>
		</ul>
		
		<hr/>
		
		<div id="footer">Cette tribune est dédiée à la mémoire de Pierre Tramo.</div>
	</body>
</html>