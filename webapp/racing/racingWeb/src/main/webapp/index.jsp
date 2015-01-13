<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<fmt:setBundle basename="ResourceBundle.messages" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><fmt:message key="index.title" /></title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/login.css" rel="stylesheet">
<link href="css/validation.css" rel="stylesheet">
</head>
<body>
	<div id="formcenter">
		<h1>
			<fmt:message key="index.h1" />
		</h1>
		<a href="changeLanguage.do?lang=fr"><img align="right"
			src="./img/fra.png" width="18" height="11" alt="french" /></a> <a
			href="changeLanguage.do?lang=en"><img align="right"
			src="./img/gbr.png" width="18" height="11" alt="english" /></a> <a
			href="changeLanguage.do?lang=nl"><img align="right"
			src="./img/ned.png" width="18" height="11" alt="dutch" /></a>

		<% 
                if (null != session.getAttribute("info")) {
                    out.print(session.getAttribute("info"));
                    session.removeAttribute("info");
                }                                                                     
            %>
		<form action="login.do" method="post" id="login-form">
			<div class="form-group">
				<label for="txtusername"><fmt:message key="index.username" /></label>
				<input type="text" name="username" class="form-control"
					id="txtusername"
					placeholder="<fmt:message key="index.enterUsername"/>">
			</div>
			<div class="form-group">
				<label for="txtpassword"><fmt:message key="index.password" /></label>
				<input type="password" name="password" class="form-control"
					id="txtpassword"
					placeholder="<fmt:message key="index.enterPassword"/>">
			</div>
			<a href="register.jsp" class="navbar-brand"><fmt:message
					key="index.create" /></a>
			<button type="submit" class="btn btn-default ">
				<fmt:message key="index.submit" />
			</button>
			<div class="group"></div>
		</form>

		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script
			src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery.validate.min.js"></script>
		<script src="js/validation.login.config.js"></script>
	</div>
</body>
</html>
