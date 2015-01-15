package be.tiwi.vop.racing.api.webservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import be.tiwi.vop.racing.api.model.ProjectManager;
import be.tiwi.vop.racing.api.transformer.FeedTransformer;
import be.tiwi.vop.racing.core.model.Race;
import be.tiwi.vop.racing.core.model.Result;
import be.tiwi.vop.racing.core.model.Tournament;
import be.tiwi.vop.racing.core.model.Tournament.Formule;
import be.tiwi.vop.racing.core.model.User;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Component
@Path("tournaments")
public class TournamentService {

  private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

  @GET
  @Produces("application/json")
  public String getAllTournaments() {
    String feeds = null;
    try {
      ArrayList<Tournament> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getAllTournaments();
      feeds = FeedTransformer.getTournaments(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @POST
  @Produces("application/json")
  @Path("upcoming")
  public String getUpcomingTournaments(@FormParam("limit1") int limit1,
      @FormParam("limit2") int limit2) {
    String feeds = null;
    try {
      ArrayList<Tournament> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getUpcomingTournaments(limit1, limit2);
      feeds = FeedTransformer.getTournaments(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Produces("application/json")
  @Path("create")
  @POST
  public String insertTournament(@FormParam("user_id") int userId,
      @FormParam("max_players") int maxPlayers, @FormParam("formule") String formule,
      @FormParam("name") String name, @FormParam("date") String strDate) throws ParseException {
    Tournament tournament = new Tournament();
    ProjectManager pm = new ProjectManager();
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
    Date date = sdf.parse(strDate);
    if (name != null || !date.after(now)) {
      logger.info(strDate);
      tournament.setDate(date);
      tournament.setFormule(Formule.valueOf(formule));
      tournament.setMaxPlayers(maxPlayers);
      tournament.setUserId(userId);
      tournament.setName(name);
      Tournament t = pm.insertTournament(tournament);
      return FeedTransformer.getTournament(t);
    }
    return null;

  }

  @Path("{tournament_id}/insert")
  @POST
  public void insertRaces(@FormParam("user_id") int userId, @FormParam("races") String races) {
    ProjectManager pm = new ProjectManager();
    ArrayList<Tournament> tournaments = pm.getTournamentsByUserId(userId);

    JsonArray jArray = new JsonParser().parse(races).getAsJsonArray();
    ArrayList<Race> racesArr = new ArrayList<Race>();

    Tournament t = new Tournament();
    for (int i = 0; i < jArray.size(); i++) {
      Race r = new Race();
      JsonObject jsonObject = jArray.get(i).getAsJsonObject();
      t.setId(jsonObject.get("tournament_id").getAsInt());
      if (tournaments.contains(t)) {
        r.setTournamentId(t.getId());
        r.setCircuitId(jsonObject.get("circuit_id").getAsInt());
        r.setLaps(jsonObject.get("laps").getAsInt());
        racesArr.add(r);
      }

    }

    logger.info("size of races: " + racesArr.size());

    if (racesArr.size() > 0) {
      pm.insertRaces(racesArr, t);
    }

  }

  @Produces("application/json")
  @Path("{tournament_id}/status")
  @GET
  public String isTournamentFull(@PathParam("tournament_id") int id) {
    String feeds = null;
    try {
      ProjectManager projectManager = new ProjectManager();
      boolean feedData = projectManager.isTournamentFull(id);
      feeds = new Gson().toJson(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Produces("application/json")
  @Path("{tournament_id}/enroll/{user_id}")
  @GET
  public void enrollTournament(@PathParam("tournament_id") int tournamentId,
      @PathParam("user_id") int userId) {
    try {
      ProjectManager projectManager = new ProjectManager();
      if (!projectManager.isTournamentFull(tournamentId)) {
        projectManager.enrollTournament(userId, tournamentId);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

  }

  @Path("{tournament_id}/resign/{user_id}")
  @GET
  public void resignTournament(@PathParam("tournament_id") int tournamentId,
      @PathParam("user_id") int userId) {
    try {
      ProjectManager projectManager = new ProjectManager();
      projectManager.resignTournament(userId, tournamentId);

    } catch (Exception e) {
      logger.error("Exception Error");
    }

  }

  @Path("enrolled/{user_id}")
  @GET
  public String getTournamentsByParticipantId(@PathParam("user_id") int userId) {
    String feeds = null;
    try {
      ArrayList<Tournament> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getTournamentsByParticipantId(userId);
      feeds = FeedTransformer.getTournaments(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Path("{tournament_id}/races")
  @GET
  public String getRacesByTournamentId(@PathParam("tournament_id") int tournamentId) {
    String feeds = null;
    try {
      ArrayList<Race> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getRacesByTournamentId(tournamentId);
      feeds = FeedTransformer.getRaces(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Path("{tournament_id}/participants")
  @GET
  public String getParticipantsByTournamentId(@PathParam("tournament_id") int tournamentId) {
    String feeds = null;
    try {
      ArrayList<User> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getParticipantsByTournamentId(tournamentId);
      feeds = FeedTransformer.userFeed(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Produces("application/json")
  @Path("{tournament_id}/results/{race_id}/user/{user_id}")
  @GET
  public String getResultsByRaceIdAndUserId(@PathParam("tournament_id") int tournamentId,
      @PathParam("race_id") int raceId, @PathParam("user_id") int userId) {
    String feeds = null;
    try {
      ArrayList<Result> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getResultsByRaceIdAndUserId(raceId, userId);
      feeds = FeedTransformer.getResults(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Produces("application/json")
  @Path("{tournament_id}/results/{race_id}")
  @GET
  public String getResultsByRaceId(@PathParam("tournament_id") int tournamentId,
      @PathParam("race_id") int raceId) {
    String feeds = null;
    try {
      ArrayList<Result> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.getResultsByRaceId(raceId);
      feeds = FeedTransformer.getResults(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }

  }

  @Produces("application/json")
  @Path("race/{race_id}/create")
  @POST
  public String createResult(@FormParam("json") String json) {
    logger.info("Inserting results: {}", json);

    List<Result> results = new Gson().fromJson(json, new TypeToken<List<Result>>() {}.getType());

    if (results != null && results.size() > 0) {
      ProjectManager pm = new ProjectManager();
      pm.insertResults(results);

      return FeedTransformer.getResults((ArrayList<Result>) results);
    }

    return "Creation of result failed!";
  }
}
