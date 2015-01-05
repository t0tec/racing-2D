<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<% request.setAttribute("userId", request.getSession().getAttribute("userId").toString()); %>
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="cc.title" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
        <link href="css/circuitBuilder.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
        <script src='js/jcanvas.min.js'></script>
        <script src='js/circuitViewerCanvas.js'></script>
	</jsp:attribute>
	<jsp:body>
		<div class="container">
			<div class="row row-offcanvas row-offcanvas-left">
            	<div class="col-xs-12 col-sm-9">
                  	<img id="tiles" src="./img/tile_sprite.png"
						alt="tiles">
                  	<img id="checks" src="./img/check_sprite.png"
						alt="checks">
                	<div id="centerCanvas">
                      <canvas id="builder"></canvas>
					</div>
				</div>
			</div>
        </div>
    </jsp:body>
</t:genericpage>
<% if (null != request.getParameter("id")) { %>
<script type="text/javascript">
    circuitId = <%out.print(request.getParameter("id"));%>;
</script>
<% }%>
