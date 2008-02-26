<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page session="true"      %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="backend" type="tifauv.jplop.Backend" scope="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><jsp:getProperty name="backend" property="name"/>::Configuration</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/config.css" />
	</head>
	<body>
		<%@ include file="header.inc.jsp" %>
		
		<h2>Configuration</h2>
		<div class="config">
			<h3>Coincoins compatibles <a href="http://tifauv.homeip.net/koinkoin/trac/wiki/BoardConfigSpec" title="La spec de config">BoardConfigSpec</a></h3>
			<p>L'URL d'auto-configuration est <a href="<jsp:getProperty name="backend" property="URL"/>/discover"><jsp:getProperty name="backend" property="URL"/>/discover</a>.</p>
			<p>Si votre coincoin n'a pas de système d'auto-configuration, débrouillez-vous pour lui faire manger ça&nbsp;:</p>
			<pre class="cc-config">
&lt;site name="<jsp:getProperty name="backend" property="name"/>" title="<jsp:getProperty name="backend" property="fullName"/>" baseurl="<jsp:getProperty name="backend" property="URL"/>" version="1.0"&gt;
	&lt;account&gt;
		&lt;login method="post" path="/logon"&gt;
			&lt;field name="login"&gt;$l&lt;/field&gt;
			&lt;field name="password"&gt;$p&lt;/field&gt;
		&lt;/login&gt;
		&lt;logout method="get" path="/logout"/&gt;
	&lt;/account&gt;
	&lt;board name="board" title="Tribune"&gt;
		&lt;backend path="/backend" public="true" tags_encoded="false" refresh="60"/&gt;
		&lt;post method="post" path="/post" anonymous="true" max_length="<jsp:getProperty name="backend" property="maxPostLength"/>"&gt;
			&lt;field name="message"&gt;$m&lt;/field&gt;
		&lt;/post&gt;
	&lt;/board&gt;
&lt;/site&gt;</pre>
		</div>
		
		<div class="config">
			<h3><a href="http://hules.free.fr/wmcoincoin" title="Le VRAI coincoin">wmcc</a></h3>
			<p>Ajoutez les lignes suivantes dans le fichier .wmcoincoin/options de votre dossier personnel&nbsp;:</p>
			<pre class="cc-config">
board_site:                <jsp:getProperty name="backend" property="name"/>
.backend_flavour:          2
.palmipede.userlogin:      Anonymous
.backend.url:              <jsp:getProperty name="backend" property="URL"/>/backend
.post.url:                 <jsp:getProperty name="backend" property="URL"/>/post
.tribune.delay:            60
.palmipede.msg_max_length: <jsp:getProperty name="backend" property="maxPostLength" /></pre>
		</div>
		
		<div class="config">
			<h3><a href="http://chrisix.free.fr/dotclear/" title="Le coincoin pythonneux">pycc</a></h3>
			<p>Le fichier de configuration d'une tribune est trop complexe, j'abandonne.</p>
		</div>
		
		<hr/>
		
		<%@ include file="footer.inc.jsp" %>
	</body>
</html>
