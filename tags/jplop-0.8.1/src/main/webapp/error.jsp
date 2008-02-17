<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true"  %>
<%@ page isThreadSafe="true" %>
<%@ page session="true"      %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:useBean id="backend" type="tifauv.jplop.Backend" scope="application"/>
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::Erreur</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta http-equiv="Author"       content="Tifauv'"/>
		<link rel="stylesheet" type="text/css" href="styles/common.css"/>
	</head>
	<body>
		<%@ include file="header.inc.jsp" %>
		
		<h2>Erreur</h2>
		<p>Une erreur a eu lieu&nbsp;:</p>
		<div class="display-exception">
			<p class="exception-name"><%= exception.getClass().getName() %></p>
			<p class="exception-msg"><%= exception.getMessage() %></p>
		</div>

		<hr/>
		
		<%@ include file="footer.inc.jsp" %>
	</body>
</html>