<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="false" %>
<%@ page isThreadSafe="true" %>
<%@ page import="tifauv.jplop.model.Backend" %>
<%! String name = Backend.getInstance().getName(); %>
<%! String fullname = Backend.getInstance().getFullName(); %>
<%! String url = Backend.getInstance().getURL(); %>
<%! int maxPostLength = Backend.getInstance().getMaxPostLength(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><%= name %>::Configuration</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/config.css" />
	</head>
	<body>
		<div id="header">
			<h1><%= name %> - <%= fullname %></h1>
			<div class="links">
				<a href="<%= url %>" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>
		
		<h2>Configuration</h2>
		<div class="config">
			<h3><a href="http://hules.free.fr/wmcoincoin" title="Le VRAI coincoin">wmcc</a></h3>
			<p>Ajoutez les lignes suivantes dans le fichier .wmcoincoin/options de votre dossier personnel&nbsp;:</p>
			<pre class="cc-config">
board_site:                <%= name %>
.backend_flavour:          2
.palmipede.userlogin:      Anonymous
.backend.url:              <%= url %>/backend
.post.url:                 <%=  url %>/post
.tribune.delay:            60
.palmipede.msg_max_length: <%= maxPostLength %></pre>
		</div>
		
		<div class="config">
			<h3><a href="http://chrisix.free.fr/dotclear/" title="Le coincoin pythonneux">pycc</a></h3>
			<p>Le fichier de configuration d'une tribune est trop gros, j'abondonne.</p>
		</div>
		
		
		<div class="config">
			<h3>Les coincoins compatibles <a href="http://tifauv.homeip.net/koinkoin/trac/wiki/BoardConfigSpec" title="La spec de config">BoardConfigSpec</a></h3>
			<p>Si votre coincoin n'a pas de système d'auto-configuration, débrouillez-vous pour lui faire manger ça&nbsp;:</p>
			<pre class="cc-config">
&lt;site name="<%= name %>" title="<%= fullname %>" baseurl="<%= url %>" version="1.0"&gt;
	&lt;board name="board" title="Tribune"&gt;
		&lt;backend path="/backend" public="true" tags_encoded="false" refresh="60"/&gt;
		&lt;post method="post" path="/post" anonymous="true" max_length="<%= maxPostLength %>"&gt;
			&lt;field name="message"&gt;$m&lt;/field&gt;
		&lt;/post&gt;
	&lt;/board&gt;
&lt;/site&gt;</pre>
		</div>
		
		<hr/>
		
		<%@ include file="footer.html.inc" %>
	</body>
</html>