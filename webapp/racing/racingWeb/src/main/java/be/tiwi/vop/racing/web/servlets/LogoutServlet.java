package be.tiwi.vop.racing.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if ((session.getAttribute("username") != null || session.getAttribute("userid") != null || session
            .getAttribute("authString") != null)) {
      session.removeAttribute("username");
      session.removeAttribute("userid");
      session.removeAttribute("authString");
      session.invalidate();
      response.sendRedirect("index.jsp");
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
