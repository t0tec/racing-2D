package be.tiwi.vop.racing.service.client;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.model.Tournament;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TournamentServiceClient extends AbstractServiceClient {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
  private BasicAuthFeature auth;

  public TournamentServiceClient() {
    webTarget = client.target(super.BASE).path("tournaments");
  }

  public TournamentServiceClient(String authString) {
    auth = new BasicAuthFeature(authString);
    client.register(auth);
    webTarget = client.target(super.BASE).path("tournaments");
  }

  public Tournament insertTournament(String name, String userId, String formule, String maxPlayers,
      String date) {

    WebTarget insertWebTarget = webTarget.path("create");
    logger.info(insertWebTarget.getUri().toString());

    // Create new form
    Form form = new Form();
    form.param("name", name);
    form.param("user_id", userId);
    form.param("formule", formule);
    form.param("max_players", maxPlayers);
    form.param("date", date);

    Response resp = insertWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));
    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("Tournament " + name + " is succesfully inserted.");
      Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
      return gson.fromJson(resp.readEntity(String.class), Tournament.class);
    } else {
      return null;
    }
  }

  public int enrollTournament(int userId, int tournamentId) {
    WebTarget tournamentWebTarget = webTarget.path(tournamentId + "/enroll/" + userId);
    logger.info(tournamentWebTarget.getUri().toString());

    Response resp = tournamentWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      return resp.getStatus();
    } else {
      return resp.getStatus();
    }
  }

  public int resignTournament(int userId, int tournamentId) {
    WebTarget tournamentWebTarget = webTarget.path(tournamentId + "/resign/" + userId);
    logger.info(tournamentWebTarget.getUri().toString());

    Response resp = tournamentWebTarget.request().get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      return resp.getStatus();
    } else {
      return resp.getStatus();
    }
  }

  public Map<Integer, Boolean> getTournamentStatus(int[] tournamentIds) {
    Map<Integer, Boolean> tStats = Maps.newHashMapWithExpectedSize(tournamentIds.length);
    for (int i = 0; i < tournamentIds.length; i++) {
      int tId = tournamentIds[i];
      WebTarget tournamentsWebTarget = webTarget.path(tId + "/status");

      Response resp = tournamentsWebTarget.request(MediaType.APPLICATION_JSON).get();

      if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
        tStats.put(tId, new Gson().fromJson(resp.readEntity(String.class), Boolean.class));
      } else {
        logger.error("Could not get status for tournament " + tId);
      }
    }
    return tStats;

  }

  public ArrayList<Tournament> getUpcomingTournaments(String limit1, String limit2) {
    WebTarget tournamentsWebTarget = webTarget.path("upcoming");
    logger.info(tournamentsWebTarget.getUri().toString());

    // Create new form
    Form form = new Form();
    form.param("limit1", limit1);
    form.param("limit2", limit2);

    Response resp =
        tournamentsWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));
    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("Tournaments between " + limit1 + " and " + limit2 + " are fetched");
      Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
      return gson.fromJson(resp.readEntity(String.class),
          new TypeToken<ArrayList<Tournament>>() {}.getType());
    } else {
      return null;
    }
  }

  public ArrayList<Tournament> getEnrolledByParticipantId(String userId) {
    WebTarget tournamentsWebTarget = webTarget.path("enrolled/" + userId);
    logger.info(tournamentsWebTarget.getUri().toString());

    Response resp = tournamentsWebTarget.request(MediaType.APPLICATION_JSON).get();
    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
      return gson.fromJson(resp.readEntity(String.class),
          new TypeToken<ArrayList<Tournament>>() {}.getType());
    } else {
      return null;
    }
  }

}
