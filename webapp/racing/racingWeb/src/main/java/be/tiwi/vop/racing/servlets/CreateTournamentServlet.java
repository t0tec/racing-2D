package be.tiwi.vop.racing.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.pojo.Tournament;
import be.tiwi.vop.racing.service.client.TournamentServiceClient;

@WebServlet(name = "CreateTournamentServlet", urlPatterns = {"/CreateTournamentServlet"})
public class CreateTournamentServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(CreateTournamentServlet.class);

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String userId = request.getSession().getAttribute("userId").toString();

    // get user info from request
    String name = request.getParameter("name");
    String formule = request.getParameter("formule");
    String strDate = request.getParameter("datepicker");
    String hours = request.getParameter("hours");
    String minutes = request.getParameter("minutes");
    strDate += " " + hours + ":" + minutes;
    String maxPlayers = request.getParameter("max_players");

    logger.info("Trying to insert tournament " + name);
    Tournament t =
        new TournamentServiceClient().insertTournament(name, userId, formule, maxPlayers, strDate);
    if (t != null) {
      request.setAttribute("messageClass", "alert alert-success");
      request.setAttribute("messageStyle", "display: block;");
      request.setAttribute("message", "Het toernooi " + name
          + " is succesvol aangemaakt. Gelieve races aan het toernooi toe te voegen.");
      request.setAttribute("tournament_id", t.getId());
      request.setAttribute("tournament_name", t.getName());
      request.getRequestDispatcher("createTournament.jsp").forward(request, response);
    } else {
      request.setAttribute("messageClass", "alert alert-warning");
      request.setAttribute("messageStyle", "display: block;");
      request.setAttribute("message", "Er is iets foutgelopen bij het opslaan van het toernooi "
          + name);
      request.getRequestDispatcher("createTournament.jsp").forward(request, response);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

}
