<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="title" fragment="true"%>
<%@attribute name="stylesheets" fragment="true"%>
<%@attribute name="scripts" fragment="true"%>
<!DOCTYPE html PUBLIC>
<html>
<head>
<fmt:setBundle basename="ResourceBundle.messages" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:invoke fragment="title" />
<link href="css/bootstrap.min.css" rel="stylesheet">
<jsp:invoke fragment="stylesheets" />
</head>
<body>
	<div class="page-container">
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<ul class="nav navbar-nav navbar-left">
					<li><a href="home.jsp"><fmt:message key="nav.home" /></a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><fmt:message key="nav.circuits" /><b
							class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="circuits.jsp"><fmt:message
										key="nav.circuits" /></a></li>
							<li><a href="circuitBuilder.jsp"><fmt:message
										key="nav.createCircuit" /></a></li>
							<li><a href="myCircuits.jsp"><fmt:message
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
					<li><a href="getProfile.do"><fmt:message key="nav.hello" />&nbsp;${username}</a></li>
					<li><a href="logout.do"><fmt:message key="nav.logout" /></a></li>
				</ul>
			</div>
		</nav>
		<div id="body">
			<jsp:doBody />
		</div>
		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script
			src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script
			src='//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js'></script>
		<script src="js/bootstrap.min.js"></script>
		<jsp:invoke fragment="scripts" />
	</div>
</body>
</html>
