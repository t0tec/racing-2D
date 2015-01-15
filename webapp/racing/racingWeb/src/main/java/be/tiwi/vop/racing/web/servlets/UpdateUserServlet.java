package be.tiwi.vop.racing.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.web.service.client.UserServiceClient;

@WebServlet("/UpdateUserServlet")
public class UpdateUserServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(UpdateUserServlet.class);

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get username and authString from session
    String authString = (String) request.getSession().getAttribute("authString");
    String username = (String) request.getSession().getAttribute("username");

    // get parameters from request
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    boolean isPublic = request.getParameter("isPublic") != null ? true : false;

    // log info
    logger.info("User " + username + " is trying to update his user account");

    // try to update user
    UserServiceClient client = new UserServiceClient(authString);

    if (client.updateUser(username, fullName, email, password, isPublic)) {
      request.getSession(true).setAttribute("info", "Update Succeeded!");
      response.sendRedirect("getProfile.do");
    } else {
      request.getSession(true).setAttribute("info", "Update failed!");
      response.sendRedirect("getProfile.do");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }
}
