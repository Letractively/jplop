		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<div id="header">
			<h1><jsp:getProperty name="backend" property="name"/> - <jsp:getProperty name="backend" property="fullName"/></h1>
			<div class="links">
				<c:choose>
					<c:when test="${not empty sessionScope.subject}">
						<jsp:useBean id="subject" type="tifauv.jplop.auth.User" scope="session" />
						<jsp:getProperty name="subject" property="login" />
					</c:when>
					<c:otherwise>
						<a href="logon" title="Authentifiez-vous">Logon</a>
					</c:otherwise>
				</c:choose>
				::
				<a href="<jsp:getProperty name="backend" property="URL"/>" title="Accueil de la tribune">Accueil</a>
			</div>
		</div>