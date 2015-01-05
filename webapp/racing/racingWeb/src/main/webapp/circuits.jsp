<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="circuits.title" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
		<link href="css/home.css" rel="stylesheet">
		<link href="css/circuitmanagement.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/getAllCircuits.js"></script>
	</jsp:attribute>
	<jsp:body>
        <div class="container center-block text-center">        
	        <div class="well">
                <table class="table">
                    <thead>
                        <tr>
                            <th><fmt:message key="circuit.name" /></th>
                            <th><fmt:message key="circuit.designer" /></th>
                            <th><fmt:message key="circuit.details" /></th>
                            <th><fmt:message key="circuit.favorite" /></th>
                        </tr>
                    </thead>
                    <tbody id="circuitsView">
                    </tbody>
                </table>
            </div>      
        </div>
    </jsp:body>
</t:genericpage>

