<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="tm.tournamentsOverview" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
		<link href="css/home.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/tournaments.js"></script>
	</jsp:attribute>
	<jsp:body>
   	    <h1 align="center">
			<fmt:message key="tm.tournamentsOverview" />
		</h1>
		<div class="container center-block text-center">
       		<p>
				<a class="btn btn-default" href="createTournament.jsp"><fmt:message
						key="tm.organise" /></a>
			</p>
			<hr>
            <div class="well">
                <table class="table">
                    <thead>
                        <tr>
                            <th><fmt:message key="tm.name" /></th>
                            <th><fmt:message key="tm.modus" /></th>
                            <th><fmt:message key="tm.date" /></th>
                            <th><fmt:message key="tm.enrollment" /></th>
                            <th><fmt:message key="tm.details" /></th>
                        </tr>
                    </thead>
                    <tbody id="tournamentsView">
                    </tbody>
                </table>
            </div>
        </div>
    </jsp:body>
</t:genericpage>
