package be.tiwi.vop.racing.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.core.model.User;
import be.tiwi.vop.racing.web.service.client.BasicAuthFeature;
import be.tiwi.vop.racing.web.service.client.UserServiceClient;

@WebServlet("/GetUserServlet")
public class GetUserServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(GetUserServlet.class);

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // get username and authString from session
    String authString = (String) request.getSession().getAttribute("authString");
    String username = (String) request.getSession().getAttribute("username");

    logger.info("Getting profile of {}", username);

    // try to get user
    if (authString != null && username != null) {
      UserServiceClient client = new UserServiceClient(authString);
      User user = client.authenticateUser();

      request.setAttribute("userId", user.getId());
      request.setAttribute("username", user.getUsername());
      request.setAttribute("fullName", user.getFullName());
      request.setAttribute("email", user.getEmail());
      request.setAttribute("isPublic", user.getIsPublic());

      request.getRequestDispatcher("userProfile.jsp").forward(request, response);
    } else {
      response.sendRedirect("home.jsp");
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }
}
