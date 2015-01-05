<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<%	if (null != session.getAttribute("info")) {
    	out.print(session.getAttribute("info"));
    	session.removeAttribute("info");
	}
%>
<% request.setAttribute("userId", request.getSession().getAttribute("userId").toString()); %>
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="tm.organise" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
       	<link href="css/validation.css" rel="stylesheet">
        <link href="css/createtournament.css" rel="stylesheet">
        <link
			href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"
			rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
        <script src="js/createTournament.js"></script>
        <script src="js/jquery.validate.min.js"></script>
        <script src="js/validation.tournament.config.js"></script>
	</jsp:attribute>
	<jsp:body>
		<div id="formcenter">
            <h1>Organiseer een toernooi</h1>
            <div class="well">
                <div id="message" class="${messageClass}"
					style="display: none;${messageStyle}">${message}</div>
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#tabToernooi"
						data-toggle="tab"><fmt:message key="tm.details" /></a></li>
                    <li><a href="#tabRaces" data-toggle="tab"><fmt:message
								key="tm.races" /></a></li>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane active in" id="tabToernooi">
                        <form action="createTournament.do" method="post"
							id="tournament-form" class="tournamentForm">
                            <!-- Name -->
                            <div class="form-group">
                                <label for="txtName"
									class="control-label"><fmt:message key="tm.name" /></label>
                                <div class="controls">
                                    <input type="text" id="txtName"
										class="form-control" name="name">
                                </div>
                            </div>

                            <!-- Formule -->
                            <div class="form-group">
                                <label for="radioFormule"
									class="control-label"><fmt:message key="tm.modus" /></label>
                                <div id="radioFormule" class="controls">
                                    <input type="radio" name="formule"
										id="totalFormule" value="TOTAL" checked="checked" /><label
										for="totalFormule"> Totale score</label><br />
                                    <input type="radio" name="formule"
										id="longestFormule" value="LONGEST" /><label
										for="longestFormule"> Langste afstand</label><br />
                                    <input type="radio" name="formule"
										id="fastestFormule" value="FASTEST" /><label
										for="fastestFormule"> Snelste tijd</label>
                                </div>
                            </div>

                            <!-- Date -->
                            <div class="form-group">
                                <label for="txtDate"
									class="control-label"><fmt:message key="tm.startTime" /></label>
                                <div id="txtDate" class="controls">
                                    <input type="text" id="datepicker"
										name="datepicker"> <br />
                                    <select name="hours">
                                        <option value="0">00</option>
                                        <option value="1">01</option>
                                        <option value="2">02</option>
                                        <option value="3">03</option>
                                        <option value="4">04</option>
                                        <option value="5">05</option>
                                        <option value="6">06</option>
                                        <option value="7">07</option>
                                        <option value="8">08</option>
                                        <option value="9">09</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                        <option value="13">13</option>
                                        <option value="14">14</option>
                                        <option value="15">15</option>
                                        <option value="16">16</option>
                                        <option value="17">17</option>
                                        <option value="18">18</option>
                                        <option value="19">19</option>
                                        <option value="20">20</option>
                                        <option value="21">21</option>
                                        <option value="22">22</option>
                                        <option value="23">23</option>
                                    </select> :
                                    <select name="minutes">
                                        <option value="00">00</option>
                                        <option value="15">15</option>
                                        <option value="30">30</option>
                                        <option value="45">45</option>
                                    </select>
                                </div>

                            </div>

                            <!-- Max Players -->
                            <div class="form-group">
                                <label for="txtPlayers"
									class="control-label"><fmt:message key="tm.maxPlayers" /></label>
                                <div class="controls">
                                    <input type="text" id="txtPlayers"
										name="max_players">
                                </div>
                            </div>

                            <!-- User id -->
                            <input type="hidden" id="txtUserId"
								name="user_id" value="${userId}">
                            <div>
                                <input type="submit"
									class="btn btn-primary" value=<fmt:message key="tm.create"/>>
                            </div>
                        </form>
                    </div>
                    <div class="tab-pane fade" id="tabRaces">
                        <form id="races-form" class="tournamentForm">
                            <h3>${tournament_name}</h3>
                            <div id="addCircuits">
                                <div class="form-group">
                                    <label for="circuits"
										class="control-label"><fmt:message
											key="tm.selectCircuit" /></label>
                                    <div class="controls">
                                        <select id="circuits"></select>
                                    </div>    
                                </div>
                                <div class="form-group">
                                    <label for="txtLaps"
										class="control-label"><fmt:message key="tm.laps" /></label>
                                    <div class="controls">
                                        <input type="text" id="txtLaps"
											name="laps">
                                        <button id="addCircuit"
											type="button">
											<fmt:message key="tm.add" />
										</button>
                                    </div>
                                </div>
                            </div>
                            <ul id="overviewCircuits" class="list-group"></ul>
                            <!-- Hidden data -->
                            
                            <input type="hidden" id="txtUserId"
								name="user_id" value="${userId}">
                            <input type="hidden" id="txtTournamentId"
								name="tournament_id" value="${tournament_id}">
                            
                            <div>
                                <input type="submit" id="btnRaces"
									class="btn btn-primary" value=<fmt:message key="tm.addToTour"/>>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpage>
