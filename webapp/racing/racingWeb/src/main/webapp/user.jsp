<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="Error.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<fmt:setBundle basename="ResourceBundle.messages" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Home</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/home.css" rel="stylesheet">
<link href="css/user.css" rel="stylesheet">
<script src="js/user.js"></script>
</head>
<c:if test="${empty param.username}">
	<body onload="loadUser('${username}', '${authString}')">
</c:if>
<c:if test="${not empty param.username}">
	<body onload="loadUser('${param.username}', '${authString}')">
</c:if>
<nav class="navbar navbar-default" role="navigation">
	<div class="container-fluid">
		<ul class="nav navbar-nav navbar-left">
			<li><a href="home.jsp"><fmt:message key="nav.home" /></a></li>
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><fmt:message key="nav.circuits" /><b
					class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="circuits.jsp"><fmt:message key="nav.circuits" /></a></li>
					<li><a href="circuitBuilder.jsp"><fmt:message
								key="nav.createCircuit" /></a></li>
					<li><a href="editcircuit.jsp"><fmt:message
								key="nav.myCircuits" /></a></li>
				</ul></li>
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"><fmt:message key="nav.tournaments" /><b
					class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="tournaments.jsp"><fmt:message
								key="nav.tournaments" /></a></li>
					<li><a href="createTournament.jsp"><fmt:message
								key="nav.createTournament" /></a></li>
				</ul></li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="user.jsp"><fmt:message key="nav.hello" />
					${username}</a></li>
			<li><a href="logout.do"><fmt:message key="nav.logout" /></a></li>
			<a href="changeLanguage.jsp?lang=fr_FR"><img align="right"
				src="./img/fra.png" width="18" height="11" alt="french" /></a>
			<a href="changeLanguage.jsp?lang=en"><img align="right"
				src="./img/gbr.png" width="18" height="11" alt="english" /></a>
			<a href="changeLanguage.jsp?lang=nl"><img align="right"
				src="./img/ned.png" width="18" height="11" alt="dutch" /></a>
		</ul>
	</div>
</nav>
<div class="container center-block text-center">

	<div id="userinfo">
		<h3>User info:</h3>
		<c:if test="${empty param.mode}">
			<table>
				<tr>
					<td>Username:</td>
					<td id="tdusername"></td>
				</tr>
				<tr>
					<td>E-mail address:</td>
					<td id="tdemail"></td>
				</tr>
				<c:if test="${empty param.username}">
					<tr>
						<td>password:</td>
						<td>********</td>
					</tr>
				</c:if>
				<tr>
					<td>Public:</td>
					<td id="tdispublic"></td>
				</tr>
			</table>
		</c:if>

		<c:if test="${empty param.username}">
			<c:if test="${empty param.mode}">
				<form action="user.jsp?mode=edit" method="POST">

					<button type="submit" class="btn btn-default">Change
						profile</button>
				</form>
			</c:if>
			<c:if test="${not empty param.mode}">
				<form action="user.jsp" method="POST">
					<table>
						<tr>
							<td>Username:</td>
							<td id="tdusername">
						</tr>
						<tr>
							<td>E-mail address:</td>
							<td><input type="text" /></td>
						</tr>
						<tr>
							<td>Password</td>
							<td>******** <a href="changepassword.do">change password</a></td>
						</tr>
						<tr>
							<td></td>
							<td><button type="submit" class="btn btn-default"
									onclick="saveUser()">Save</button>
					</table>
				</form>
			</c:if>
		</c:if>



	</div>
	<div id="enumerateusers">
		<h3>All users</h3>
		<ul id="users-list" class="list-group" role="menu"
			aria-labelledby="dropdownMenu3">

		</ul>
	</div>



</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>