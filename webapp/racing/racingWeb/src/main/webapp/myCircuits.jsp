<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="cm.title" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
		<link href="css/home.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/getMyCircuits.js"></script>
	</jsp:attribute>
	<jsp:body>
	    <h1 align="center">
			<fmt:message key="cm.circuits" />
		</h1>
	    <div class="container circuitlist text-center">
   	        <a class="btn btn-default" href="circuitBuilder.jsp"><fmt:message
					key="cm.create" /></a>
   	        <hr>
	        <div class="list-group">      
	        </div>
	    </div>


    </jsp:body>
</t:genericpage>
