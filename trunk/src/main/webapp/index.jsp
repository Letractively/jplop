<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page session="true"      %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="backend" type="tifauv.jplop.Backend" scope="application"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::Accueil</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta http-equiv="Author"       content="Tifauv'"/>
		<link rel="stylesheet" type="text/css" href="styles/common.css"/>
	</head>
	<body>
		<%@ include file="header.inc.jsp" %>
		
<c:choose>
	<c:when test="${not empty sessionScope.subject}">
		<div class="message welcome">Content de vous revoir, <jsp:getProperty name="subject" property="login"/> _o/</div>
	</c:when>
	<c:otherwise>
		<div class="message welcome">Plop, bienvenue ici _o/</div>
	</c:otherwise>
</c:choose>

<c:if test="${not empty errorMsg}">
	<jsp:useBean id="errorMsg" type="java.lang.String" scope="request" />
		<div class="message error"><%= errorMsg %></div>
</c:if>

		<ul id="board-access" >
			<li><a href="board.jsp"    title="Je veux ploper !">Voir la tribune</a></li>
<c:choose>
	<c:when test="${not empty sessionScope.subject}">
			<li><a href="logout"       title="Je veux partir !">D&eacute;connexion</a></li>
	</c:when>
	<c:otherwise>
			<li><a href="logon.jsp"    title="J'ai rien &agrave; cacher !">Authentification</a></li>
			<li><a href="register.jsp" title="Je veux devenir c&eacute;l&egrave;bre !">Cr&eacute;er un compte</a></li>
	</c:otherwise>
</c:choose>

			<li><a href="config.jsp"   title="Je veux configurer mon coincoins !">Configuration</a></li>
		</ul>
		
		<hr/>
		
		<%@ include file="footer.inc.jsp" %>
	</body>
</html>
