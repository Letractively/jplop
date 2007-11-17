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
		<title><%= name %>::APropos</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/about.css" />
	</head>
	<body>
		<div id="header">
			<h1><%= name %> - <%= fullname %></h1>
			<div class="links">
				<a href="<%= url %>" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>
		
		<h2>Version</h2>
		<div id="version">This is JPlop version 0.6-beta1.</div>
		
		<h2>Crédits</h2>
		<div id="credits">
		Copyright &copy; 2007 - Olivier Serve aka <a href="http://tifauv.homeip.net">Tifauv'</a>.
		Le nom a été trouvé par <a href="http://leguyader.eu">LLG</a> au terme d'une longue consultation des moules.
		</div>

		<h2>Licence</h2>		
		<div id="license">
		JPlop est distribué sous la licence <a href="COPYING">GPL version 2</a> ou ultérieure. 
		</div>

		<hr/>

		<%@ include file="footer.html.inc" %>
	</body>
</html>