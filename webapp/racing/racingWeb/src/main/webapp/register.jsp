<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<fmt:setBundle basename="ResourceBundle.messages" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><fmt:message key="register.title" /></title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/validation.css" rel="stylesheet">
<link href="css/login.css" rel="stylesheet">
</head>
<body>
	<div id="formcenter">
		<h1>
			<fmt:message key="register.h1" />
		</h1>
		<h2>
			<fmt:message key="register.h2" />
		</h2>
		<% if (null != session.getAttribute("info")) {
                	out.print(session.getAttribute("info"));
            		session.removeAttribute("info");
				}
			%>
		<form action="createUser.do" method="post" id="register-form">
			<div class="form-group">
				<label for="txtfullname"><fmt:message
						key="register.fullname" /></label> <input type="text" class="form-control"
					id="txtfullname"
					placeholder="<fmt:message key="register.fullnamePH"/>"
					name="fullname">
			</div>
			<div class="form-group">
				<label for="txtusername"><fmt:message
						key="register.username" /></label> <input type="text" class="form-control"
					id="txtusername"
					placeholder="<fmt:message key="register.usernamePH"/>"
					name="username">
			</div>

			<div class="form-group">
				<label for="txtemail"><fmt:message key="register.email" /></label> <input
					type="email" class="form-control" id="txtemail"
					placeholder="<fmt:message key="register.emailPH"/>" name="email">
			</div>
			<div class="form-group">
				<label for="txtpassword"><fmt:message
						key="register.passwordPH" /></label> <input type="password"
					class="form-control" id="txtpassword"
					placeholder="<fmt:message key="register.passwordPH"/>"
					name="password">
			</div>
			<a href="index.jsp" class="navbar-brand"><fmt:message
					key="register.index" /></a>
			<button type="submit" class="btn btn-default ">
				<fmt:message key="register.submit" />
			</button>
			<div class="group"></div>
		</form>
	</div>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.validate.min.js"></script>
	<script src="js/validation.register.config.js"></script>
</body>
</html>
