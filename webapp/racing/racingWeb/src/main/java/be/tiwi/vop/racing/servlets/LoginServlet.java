package be.tiwi.vop.racing.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.pojo.User;
import be.tiwi.vop.racing.service.client.UserServiceClient;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get username and password from request
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // log info
    logger.info("User " + username + " is trying to login");
    logger.info("servername: " + request.getServerName());
    logger.info("port: " + request.getServerPort());
    logger.info(request.getRequestURI());
    logger.info(request.getRequestURI().substring(request.getContextPath().length()));

    // try to authenticate user
    UserServiceClient client = new UserServiceClient(username, password);
    User user = client.authenticateUser();
    if (user != null) {
      request.getSession(true).setAttribute("userId", user.getId());
      request.getSession(true).setAttribute("username", user.getUsername());
      request.getSession(true).setAttribute("authString", client.getAuthString());
      response.sendRedirect("home.jsp");
    } else {
      request.getSession(true).setAttribute("info", "Login failed");
      response.sendRedirect("index.jsp");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

}
