package be.tiwi.vop.racing.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.tiwi.vop.racing.model.Tournament;
import be.tiwi.vop.racing.service.client.TournamentServiceClient;

import com.google.gson.Gson;

@WebServlet("/TournamentServlet")
public class TournamentServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    String action = req.getParameter("action");
    if (action.equals("upcoming")) {
      getUpcomingTournaments(req, resp);
    }

  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    String action = req.getParameter("action");
    if (action.equals("enroll")) {
      enrollTournament(req, resp);
    } else if (action.equals("resign")) {
      resignTournament(req, resp);
    }
  }

  public void enrollTournament(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // get parameters
    int id = Integer.parseInt(request.getParameter("tournamentId"));
    String authString = request.getSession().getAttribute("authString").toString();
    int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

    // request api
    TournamentServiceClient client = new TournamentServiceClient(authString);
    int status = client.enrollTournament(userId, id);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(id);
    response.getWriter().write(status);
  }

  public void resignTournament(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // get parameters
    int id = Integer.parseInt(request.getParameter("tournamentId"));
    String authString = request.getSession().getAttribute("authString").toString();
    int userId = Integer.parseInt(request.getSession().getAttribute("userId").toString());

    // request api
    TournamentServiceClient client = new TournamentServiceClient(authString);
    int status = client.resignTournament(userId, id);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = new Gson().toJson(id);
    response.getWriter().write(status);
  }

  private void getUpcomingTournaments(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // get parameters
    String limit1 = request.getParameter("limit1");
    String limit2 = request.getParameter("limit2");
    String authString = request.getSession().getAttribute("authString").toString();
    String userId = request.getSession().getAttribute("userId").toString();

    // request api
    TournamentServiceClient client = new TournamentServiceClient(authString);
    ArrayList<Tournament> tournaments = client.getUpcomingTournaments(limit1, limit2);
    String jsonTournaments = new Gson().toJson(tournaments);
    ArrayList<Tournament> enrolled = client.getEnrolledByParticipantId(userId);
    String jsonEnrolled = new Gson().toJson(enrolled);
    int[] tIds = new int[tournaments.size()];
    for (int i = 0; i < tournaments.size(); i++) {
      tIds[i] = tournaments.get(i).getId();
    }
    Map<Integer, Boolean> statuses = client.getTournamentStatus(tIds);
    String jsonStatuses = new Gson().toJson(statuses);

    // write response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String json = "[" + jsonTournaments + "," + jsonEnrolled + "," + jsonStatuses + "]";
    response.getWriter().write(json);
  }

}
