<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Author"       content="Tifauv'" />
		<link rel="stylesheet" type="text/css" href="styles/about.css" />
		<title>JPlop</title>
	</head>
	<body>
	<f:view>
		<%@ include file="common/header.jspf"%>

		<div id="main">
			<h2>Logon</h2>
			
			<h:messages/>
			
			<div>
				<h3>I already have an account</h3>
				<h:form>
				Login :    <h:inputText   value="#{account.login}"    maxlength="24" size="12"/>
				Password : <h:inputSecret value="#{account.password}" maxlength="24" size="12"/>
				<h:commandButton value="Log in" action="#{account.doLogon}" type="submit"/>
				</h:form>
			</div>
			
			<div>
				<h3>I want to create an account</h3>
				<h:form>
				Login :    <h:inputText   value="#{account.account.login}"    maxlength="24" size="12"/>
				Password : <h:inputSecret value="#{account.account.password}" maxlength="24" size="12"/>
				Confirmation : <h:inputSecret value="#{account.passwordConfirm}" maxlength="24" size="12"/>
				<h:commandButton value="Create" action="#{account.doCreate}" type="submit"/>
				</h:form>
			</div>
		</div>

		<%@ include file="common/footer.jspf"%>
	</f:view>
	</body>
</html>