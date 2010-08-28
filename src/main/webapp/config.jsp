<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page session="true"      %>
<%@ page import="tifauv.jplop.core.CommonConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="backend" type="tifauv.jplop.core.Backend" scope="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>${backend.config.name}::Configuration</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/config.css" />
	</head>
	<body>
		<%@ include file="header.inc.jsp" %>

		<h2>Configuration des coincoins</h2>
		<div class="config">
			<h3>Coincoins compatibles <a href="http://tifauv.homeip.net/koinkoin/trac/wiki/BoardConfigSpec" title="La spec de config">BoardConfigSpec</a></h3>
			<p>L'URL d'auto-configuration est <a href="${backend.config.URL}/discover">${backend.config.URL}/discover</a>.</p>
			<p>Si votre coincoin n'a pas de système d'auto-configuration, débrouillez-vous pour lui faire manger ça&nbsp;:</p>
			<pre class="cc-config">
&lt;site name="${backend.config.name}" title="${backend.config.fullName}" baseurl="${backend.config.URL}" version="1.1"&gt;
	&lt;account&gt;
		&lt;login method="post" path="/logon"&gt;
			&lt;field name="<%= CommonConstants.LOGIN_PARAM %>"&gt;$l&lt;/field&gt;
			&lt;field name="<%= CommonConstants.PASSWORD_PARAM %>"&gt;$p&lt;/field&gt;
		&lt;/login&gt;
		&lt;logout method="get" path="/logout"/&gt;
	&lt;/account&gt;
	&lt;module name="board" title="Tribune" type="application/board+xml"&gt;
		&lt;backend path="/backend" public="true" tags_encoded="false" refresh="60"/&gt;
		&lt;post method="post" path="/post" anonymous="true" max_length="${backend.config.maxPostLength}"&gt;
			&lt;field name="<%= CommonConstants.MESSAGE_PARAM %>"&gt;$m&lt;/field&gt;
		&lt;/post&gt;
	&lt;/module&gt;
&lt;/site&gt;</pre>
		</div>

		<div class="config">
			<h3><a href="http://hules.free.fr/wmcoincoin" title="Le VRAI coincoin">wmcc</a></h3>
			<p>Ajoutez les lignes suivantes dans le fichier <span class="file">.wmcoincoin/options</span> de votre dossier personnel&nbsp;:</p>
<c:choose>
	<c:when test="${not empty sessionScope.subject}">
			<pre class="cc-config">
board_site:                "${backend.config.name}"
.backend_flavour:          2
.palmipede.userlogin:      ${subject.login}
.backend.url:              ${backend.config.URL}/backend
.post.url:                 ${backend.config.URL}/post
.tribune.delay:            60
.palmipede.msg_max_length: ${backend.config.maxPostLength}</pre>
			<p>Ajoutez également la ligne suivante dans le fichier <span class="file">.wmcoincoin/options.auth</span> de votre dossier personnel&nbsp;:</p>
			<pre class="cc-config">
"${backend.config.name}" cookie: "JSESSIONID=<%=request.getSession().getId() %>"</pre>
	</c:when>
	<c:otherwise>
			<pre class="cc-config">
board_site:                "${backend.config.name}"
.backend_flavour:          2
.backend.url:              ${backend.config.URL}/backend
.post.url:                 ${backend.config.URL}/post
.tribune.delay:            60
.palmipede.msg_max_length: ${backend.config.maxPostLength}</pre>
	</c:otherwise>
</c:choose>
		</div>

		<div class="config">
			<h3><a href="http://chrisix.free.fr/dotclear/" title="Le coincoin pythonneux">pycc</a></h3>
			<p>Le fichier de configuration d'une tribune est trop complexe, j'abandonne.</p>
		</div>

		<hr/>

		<%@ include file="footer.inc.jsp" %>
	</body>
</html>
