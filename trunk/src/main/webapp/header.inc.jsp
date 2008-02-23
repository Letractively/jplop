		<div id="header">
			<h1><jsp:getProperty name="backend" property="name"/> - <jsp:getProperty name="backend" property="fullName"/></h1>
			<div class="links">
				<c:choose>
					<c:when test="${not empty sessionScope.subject}">
						<jsp:useBean id="subject" type="tifauv.jplop.auth.User" scope="session" />
						<jsp:getProperty name="subject" property="login" />
						::
						<a href="logout">Logout</a>
					</c:when>
					<c:otherwise>
						<a href="logon.jsp"    title="Authentifiez-vous">Logon</a>
						::
						<a href="register.jsp" title="Je veux un compte">Cr&eacute;er un compte</a>
					</c:otherwise>
				</c:choose>
				::
				<a href="index.jsp" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>