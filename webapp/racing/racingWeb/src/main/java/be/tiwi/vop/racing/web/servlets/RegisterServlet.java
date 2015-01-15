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

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get user info from request
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String email = request.getParameter("email");
    String fullName = request.getParameter("fullname");
    logger.info("User " + username + " is trying to register");

    if (new UserServiceClient().registerUser(username, password, email, fullName)) {
      response.sendRedirect("index.jsp");
    } else {
      request.getSession(true).setAttribute("info", "Registration failed");
      response.sendRedirect("register.jsp");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }
}
