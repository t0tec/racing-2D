package be.tiwi.vop.racing.desktop.restcontroller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.exceptions.ApiPostRequestException;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.model.tournament.Result;
import be.tiwi.vop.racing.desktop.model.tournament.Tournament;
import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.restclient.AbstractServiceClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TournamentRestController {
  private static final Logger logger = LoggerFactory.getLogger(TournamentRestController.class);

  private volatile AuthenticatedUser user = AuthenticatedUser.getInstance();

  public ArrayList<Tournament> loadEnrolledTournaments() {
    logger.info("Loading all user his enrolled tournaments");

    ArrayList<Tournament> tournaments = new ArrayList<Tournament>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Tournament>> tournamentsWorker =
          new AbstractServiceClient<List<Tournament>>() {

            @Override
            protected List<Tournament> callImpl() throws Exception {
              client.register(user.getBasicAuth());
              webTarget = client.target(super.BASE_URL).path("tournaments");
              WebTarget circuitsWebTarget =
                  webTarget.path("enrolled").path(user.getId().toString());

              Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

              if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("request for tournaments OK");
                Gson gson =
                    new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
                return gson.fromJson(resp.readEntity(String.class),
                    new TypeToken<List<Tournament>>() {}.getType());
              } else {
                return Collections.<Tournament>emptyList();
              }
            }

          };

      if (user != null) {
        Future<List<Tournament>> getTournaments = executor.submit(tournamentsWorker);
        tournaments = new ArrayList<Tournament>(getTournaments.get());
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return tournaments;
  }

  public ArrayList<Race> loadTournamentRaces(final int tournamentId) {
    logger.info("Loading all races from tournament with id {}", tournamentId);

    ArrayList<Race> races = new ArrayList<Race>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Race>> racesWorker = new AbstractServiceClient<List<Race>>() {

        @Override
        protected List<Race> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("tournaments");
          WebTarget racesWebTarget = webTarget.path(Integer.toString(tournamentId)).path("races");

          Response resp = racesWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<List<Race>>() {}.getType());
          } else {
            return Collections.<Race>emptyList();
          }
        }

      };

      if (user != null) {
        Future<List<Race>> getRaces = executor.submit(racesWorker);
        races = (ArrayList<Race>) getRaces.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return races;
  }

  public void createResults(final List<Result> results) {
    logger.info("Creating results for user id {} for race with id {}", results.get(0).getUserId(),
                results.get(0).getRaceId());

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<Void> createResultWorker = new AbstractServiceClient<Void>() {

        @Override
        protected Void callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("tournaments").path("race");
          WebTarget createWebTarget =
              webTarget.path(Integer.toString(results.get(0).getRaceId())).path("create");

          String json = new Gson().toJson(results);

          // Create new form for adding parameters
          Form form = new Form();
          form.param("json", json);

          Response resp =
              createWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            // everything OK
            return null;
          } else {
            logger.error("Failed to create result");
            throw new ApiPostRequestException("Request failed: " + resp.getLocation());
          }
        }

      };
      Future<Void> createResults = executor.submit(createResultWorker);
      createResults.get();
      executor.shutdown();

    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

  }

  public ArrayList<Result> getRaceResults(final int tournamentId, final int raceId) {
    logger.info("Loading all results from tournament with id {} from race with id {}",
        tournamentId, raceId);

    ArrayList<Result> raceResults = new ArrayList<Result>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Result>> resultsWorker = new AbstractServiceClient<List<Result>>() {

        @Override
        protected List<Result> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("tournaments");
          WebTarget resultsWebTarget =
              webTarget.path(Integer.toString(tournamentId)).path("results")
                  .path(Integer.toString(raceId));

          Response resp = resultsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<List<Result>>() {}.getType());
          } else {
            return Collections.<Result>emptyList();
          }
        }

      };

      if (user != null) {
        Future<List<Result>> getResults = executor.submit(resultsWorker);
        raceResults = (ArrayList<Result>) getResults.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return raceResults;
  }

  public ArrayList<Result> getRaceResultsFromCurrentUser(final int tournamentId, final int raceId) {
    logger.info(
        "Loading all results from tournament with id {} from race with id {} for current user",
        tournamentId, raceId);

    ArrayList<Result> userRaceResults = new ArrayList<Result>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Result>> resultsWorker = new AbstractServiceClient<List<Result>>() {

        @Override
        protected List<Result> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("tournaments");
          WebTarget resultsWebTarget =
              webTarget.path(Integer.toString(tournamentId)).path("results")
                  .path(Integer.toString(raceId)).path("user")
                  .path(Integer.toString(AuthenticatedUser.getInstance().getId()));

          Response resp = resultsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<List<Result>>() {}.getType());
          } else {
            return Collections.<Result>emptyList();
          }
        }

      };

      if (user != null) {
        Future<List<Result>> getResults = executor.submit(resultsWorker);
        userRaceResults = (ArrayList<Result>) getResults.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return userRaceResults;
  }

}
