<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:useBean id="backend" type="tifauv.jplop.model.Backend" scope="application" />
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::APropos</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/about.css" />
	</head>
	<body>
		<div id="header">
			<h1><jsp:getProperty name="backend" property="name"/> - <jsp:getProperty name="backend" property="fullName"/></h1>
			<div class="links">
				<a href="<jsp:getProperty name="backend" property="URL"/>" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>
		
		<h2>Version</h2>
		<div id="version">JPlop version 0.6.</div>
		
		<h2>Crédits</h2>
		<div id="credits">
			<p>
				Copyright &copy; 2007 - Olivier Serve aka <a href="http://tifauv.homeip.net">Tifauv'</a>.
				Pour essayer la bête ou insulter l'auteur, c'est <a href="http://code.google.com/p/jplop/">par là</a>.
			</p>
		
			<p>Le nom a été trouvé par <a href="http://leguyader.eu">LLG</a> au terme d'une longue consultation des moules.</p>
		</div>

		<h2>Licence</h2>		
		<div id="license">
		JPlop est distribué sous la licence <a href="COPYING">GPL version 2</a> ou ultérieure. 
		</div>

		<hr/>

		<%@ include file="footer.html.inc" %>
	</body>
</html>