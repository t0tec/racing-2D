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
        <script src='js/circuitBuilderCanvas.js'></script>
	</jsp:attribute>
	<jsp:body>
		<div class="container">
            <div class="row row-offcanvas row-offcanvas-left">
                <!-- sidebar -->
                <div class="col-xs-6 col-sm-3 sidebar-offcanvas"
					id="sidebar">
                    <h3>Dimensions</h3>
                    <form id="gridsize">
                        <div class="form-group">
                            <input type="text" class="form-control"
								id="inputRows" placeholder="Rows">
                            <input type="text" class="form-control"
								id="inputCols" placeholder="Columns">
                            <input type="submit"
								class="btn btn-primary form-control" value="Generate"
								id="generateCanvas">
                        </div>
                    </form>

                    <div id="components"> 
                        <h3>Components</h3>
                        <img id="1" src="./img/EARTH.jpg" alt="earth"
							width="55" height="55">
                        <img id="2" src="./img/START.jpg" alt="earth"
							width="55" height="55">
                        <img id="3" src="./img/STRAIGHT.jpg" alt="earth"
							width="55" height="55">
                        <img id="4" src="./img/STRAIGHT_UP.jpg"
							alt="earth" width="55" height="55">
                        <img id="5" src="./img/L_TURN.jpg" alt="earth"
							width="55" height="55">
                        <img id="6" src="./img/L_TURN_90.jpg"
							alt="earth" width="55" height="55">
                        <img id="7" src="./img/L_TURN_180.jpg"
							alt="earth" width="55" height="55">
                        <img id="8" src="./img/L_TURN_270.jpg"
							alt="earth" width="55" height="55">
                        <img id="9" src="./img/CROSS.jpg" alt="earth"
							width="55" height="55">
                        <img id="checkpoint" class="masterTooltip"
							title="checkpoint" src="./img/checkpoint.png" alt="earth"
							width="55" height="55">
                        <img id="obstacle0" class="masterTooltip"
							title="bonus +5" src="./img/melon-bonus.png" alt="obst"
							width="32" height="32">
                        <img id="obstacle1" class="masterTooltip"
							title="bonus +10" src="./img/strawberry-bonus.png" alt="obst"
							width="32" height="32">
                        <img id="obstacle2" class="masterTooltip"
							title="obstacle -5" src="./img/paddo-obstacle.png" alt="obst"
							width="32" height="32">
                        <img id="obstacle3" class="masterTooltip"
							title="obstacle -10" src="./img/eggplant-obstacle.png" alt="obst"
							width="32" height="32">
                    </div>

                    <form id="saveCircuit" action="#">
                        <h3>
							<fmt:message key="cm.save" />
						</h3>
                        <div id="loader">
							<img src="./img/ajax-loader.gif" alt="loader">
						</div>
                        <div id="message" style="display: none;"></div>


                        <div class="form-group">
                            <label for="place_holder"
								class="control-label">Checkpoints</label>
                            <ol id="checks_holder">
                            </ol>
                            <input type="hidden" id="userId"
								value="${userId}" />
                            <label for="direction" class="control-label"><fmt:message
									key="cm.direction" /></label>
                            <br />
                            <div id="direction" class="btn-group"
								data-toggle="buttons">
                                <label class="btn btn-default active"><input
									type="radio" name="direction" value="left" id="clockwise">
								<fmt:message key="cm.left" /></label>
                                <label class="btn btn-default"><input
									type="radio" name="direction" value="right" id="counter">
								<fmt:message key="cm.right" /></label>
                            </div>
                            <br />
                            <label for="circuitName"
								class="control-label"><fmt:message key="cm.name" /></label>
                            <input type="text" class="form-control"
								id="circuitName" placeholder="Default Circuit">
                            <input type="submit"
								class="btn btn-primary form-control" disabled="disabled"
								value="Save" id="saveCircuitButton">
                        </div>
                    </form>

                </div>

                <!-- main area -->
                <div class="col-xs-12 col-sm-9">
                    <img id="tiles" src="./img/tile_sprite.png"
						alt="tiles">
                    <img id="checks" src="./img/check_sprite.png"
						alt="checks">
                    <div id="centerCanvas">
                        <canvas id="builder"></canvas>
                    </div>
                </div>
				<!-- /.col-xs-12 main -->
            </div>
			<!--/.row-->
    	</div>
		<!--/.container-->
    </jsp:body>
</t:genericpage>
<% if (null != request.getParameter("edit")) { %>
<script type="text/javascript">
    circuitId = <%out.print(request.getParameter("edit"));%>;
</script>
<% }%>
