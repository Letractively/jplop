<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page import="tifauv.jplop.model.Backend" %>
<%! String name = Backend.getInstance().getName(); %>
<%! String fullname = Backend.getInstance().getFullName(); %>
<%! String url = Backend.getInstance().getURL(); %>
<%! int maxPostLength = Backend.getInstance().getMaxPostLength(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><%= name %>::Accueil</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/common.css" />
	</head>
	<body>
		<div id="header">
			<h1><%= name %> - <%= fullname %></h1>
		</div>
		
		<p>Plop, heureux de vous voir ici _o/</p>
		<p></p>

		<ul id="board-access" >
			<li><a href="board.jsp">Voir la tribune</a></li>
			<li><a href="config.jsp">Configuration</a></li>
		</ul>
		
		<hr/>
		
		<%@ include file="footer.html.inc" %>
	</body>
</html>