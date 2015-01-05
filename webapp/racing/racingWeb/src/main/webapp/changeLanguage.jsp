
<%
String sessionLangAttribute = "javax.servlet.jsp.jstl.fmt.locale.session";  
String lang = request.getParameter("lang");  
request.getSession().setAttribute(sessionLangAttribute, lang);  
String previousURL = request.getHeader("referer");  
response.sendRedirect(previousURL);
%>