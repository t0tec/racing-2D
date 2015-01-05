<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="home.title" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
		<link href="css/home.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/home.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div class="container center-block text-center">
			<p>
				<a class="btn btn-default" href="app/launcher.jnlp"><fmt:message
						key="home.play" /></a>
			</p>
			<p>
				<a class="btn btn-default" href="circuitBuilder.jsp"><fmt:message
						key="home.design" /></a>
			</p>         
			<p>
				<a class="btn btn-default" href="myCircuits.jsp"><fmt:message
						key="home.edit" /></a>
			</p>
			<p>
				<a class="btn btn-default" href="tournaments.jsp"><fmt:message
						key="home.tournaments" /></a>
			</p>
			<p>
				<a class="btn btn-default" href="createTournament.jsp"><fmt:message
						key="tm.organise" /></a>
			</p>

            <div class="well">
                <h3>
					<fmt:message key="home.events" />
				</h3>
            	<ul id="recentEvents" class="clearfix"> 
                	<span id="userId">${userId}</span>
                	<li><span class="action" style="font-weight: bold"><fmt:message
								key="home.action" /></span><span class="time" style="font-weight: bold"><fmt:message
								key="home.when" /></span></li>
            	</ul>
            	<button type="submit" id="more" class="btn btn-default">
					<fmt:message key="home.loadMore" />
				</button>
            </div>
        </div>
        <div id="dialog" title="Toernooi melding">
			<p></p>
		</div>
    </jsp:body>
</t:genericpage>
