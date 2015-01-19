<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="error.title" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
		<link href="css/home.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script src="js/home.js"></script>
	</jsp:attribute>
  <jsp:body>
    <div class="container center-block text-center well">
      <h1><fmt:message key="error.h1"/></h1>
      <p>
        <fmt:message key="error.message"/>
        <a class="btn-link" href="home.jsp"><fmt:message key="nav.home"/></a>
      </p>
    </div>
  </jsp:body>
</t:genericpage>
