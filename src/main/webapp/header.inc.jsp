		<div id="header">
			<h1>${backend.config.name}- ${backend.config.fullName}</h1>
			<div class="links">
				<a href="board.jsp" title="Je veux ploper !">Tribune</a>
				::
				<a href="config.jsp" title="Configuration pour les coincoins">Coincoins</a>
				::
				<c:choose>
					<c:when test="${not empty sessionScope.subject}">
						<jsp:useBean id="subject" type="tifauv.jplop.core.auth.User" scope="session" />
						<span id="currentUser"><jsp:getProperty name="subject" property="login" /></span>
						::
						<a href="logout">Logout</a>
					</c:when>
					<c:otherwise>
						<a href="register.jsp" title="Je veux un compte">Cr&eacute;er un compte</a>
						::
						<a href="logon.jsp"    title="Authentifiez-vous">Logon</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
