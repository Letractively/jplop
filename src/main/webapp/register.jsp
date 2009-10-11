<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page session="true"      %>
<%@ page import="tifauv.jplop.core.CommonConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="backend" type="tifauv.jplop.core.Backend" scope="application"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::Nouveau compte</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta http-equiv="Author"       content="Tifauv'"/>
		<link rel="stylesheet" type="text/css" href="styles/form.css"/>
		<script type="text/javascript" src="scripts/utils.js"></script>
		<script type="text/javascript" src="scripts/register.js"></script>
	</head>
	<body>
		<%@ include file="header.inc.jsp" %>
		
		<h2>Nouveau compte</h2>

<c:if test="${not empty errorMsg}">
	<jsp:useBean id="errorMsg" type="java.lang.String" scope="request" />
		<div class="message error"><%= errorMsg %></div>
</c:if>
		
<c:choose>
	<c:when test="${not empty sessionScope.subject}">
		<div class="message error">Vous &ecirc;tes d&eacute;j&agrave; authentifi&eacute;.
		<a href="board.jsp">Retour &agrave; l'accueil</a></div>
	</c:when>
	<c:otherwise>
		<form method="post" action="register">
			<div class="formBox">
				<div class="field">
					<label for="username">Login&nbsp;:</label>
					<input type="text" id="username" name="<%= CommonConstants.LOGIN_PARAM %>"/>
				</div>
				<div class="field">
					<label for="password">Mot de passe&nbsp;:</label>
					<input type="password" id="password" name="<%= CommonConstants.PASSWORD_PARAM %>"/>
				</div>
				<div class="field">
					<label for="password-confirm">Confirmation&nbsp;:</label>
					<input type="password" id="password-confirm" name="<%= CommonConstants.PASSWORD_CONFIRM_PARAM %>"/>
				</div>
				<div class="action">
					<input type="submit"/>
				</div>
			</div>
		</form>
	</c:otherwise>
</c:choose>
		
		<hr/>
		
		<%@ include file="footer.inc.jsp" %>
	</body>
</html>
