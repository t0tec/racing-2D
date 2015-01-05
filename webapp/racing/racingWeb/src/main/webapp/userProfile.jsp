<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<fmt:setBundle basename="ResourceBundle.messages" />
<t:genericpage>
	<jsp:attribute name="title">
		<title><fmt:message key="profile.title" /></title>
	</jsp:attribute>
	<jsp:attribute name="stylesheets">
        <link
			href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"
			rel="stylesheet">
        <link href="css/validation.css" rel="stylesheet">
        <link href="css/userProfile.css" rel="stylesheet">
	</jsp:attribute>
	<jsp:attribute name="scripts">
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {				
			    if ($('input#chkIsPublic').val() == "true") {           
		         	$('input#chkIsPublic').prop('checked', true);		         	
			    } else {
			        $('input#chkIsPublic').prop('checked', false);
			    }
			    
			    $('input#chkIsPublic').change(function(){
			        cb = $(this);
			        cb.val(cb.prop('checked'));
			    });
			});
		</script>
        <script src="js/jquery.validate.min.js"></script>
        <script src="js/validation.update-profile.config.js"></script>
	</jsp:attribute>
	<jsp:body>
    	<div id="formcenter">
        	<h1>
				<fmt:message key="profile.h1" />
			</h1>
           	<div class="well">
	            <h2>
					<fmt:message key="profile.h2" />
				</h2>
				${info}
	            <form action="updateUser.do" method="post"
					id="update-user-form">
	                <div class="form-group">
	                    <label for="txtfullname"><fmt:message
								key="profile.username" /></label>
	                   	<span class="input-xlarge uneditable-input">${username}</span>
	                </div> 
	                <div class="form-group">
	                    <label for="txtfullname"><fmt:message
								key="profile.fullname" /></label>
	                    <input type="text" class="form-control"
							id="txtfullname" name="fullName" value="${fullName}">
	                </div>                
	                <div class="form-group">
	                    <label for="txtemail"><fmt:message
								key="profile.email" /></label>
	                    <input type="email" class="form-control"
							id="txtemail" name="email" value="${email}">
	                </div>
	                <div class="form-group">
	                    <label for="txtpassword"><fmt:message
								key="profile.password" /></label>
	                    <input type="password" class="form-control"
							id="txtpassword" name="password" value="${password}">
	                </div>
	                <div class="form-group">
						<label for="txtpassword"><fmt:message key="profile.public" /></label>
					  	<input type="checkbox" id="chkIsPublic" name="isPublic"
							value="${isPublic}">
	                </div>
	                <input type="submit" class="btn btn-default"
						value="<fmt:message key="profile.save"/>">
	            </form>   
            </div>    
        </div>
    </jsp:body>
</t:genericpage>
<% 
	if (null != session.getAttribute("info")) {
		session.removeAttribute("info");
	}
%>
