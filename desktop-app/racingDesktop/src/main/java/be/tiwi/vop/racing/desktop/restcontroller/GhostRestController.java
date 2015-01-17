package be.tiwi.vop.racing.desktop.restcontroller;

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
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.restclient.AbstractServiceClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GhostRestController {
  private static final Logger logger = LoggerFactory.getLogger(GhostRestController.class);

  private volatile AuthenticatedUser user = AuthenticatedUser.getInstance();

  public ArrayList<Ghost> loadGhosts(final int circuitId) {
    logger.info("Loading ghosts of circuit with id {}", circuitId);

    ArrayList<Ghost> ghosts = new ArrayList<Ghost>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Ghost>> ghostsWorker = new AbstractServiceClient<List<Ghost>>() {

        @Override
        protected List<Ghost> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("ghosts");
          WebTarget ghostsWebTarget = webTarget.path("circuit/" + circuitId);

          Response resp = ghostsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("request for ghosts OK");
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<List<Ghost>>() {}.getType());
          } else {
            return Collections.<Ghost>emptyList();
          }
        }

      };

      if (user != null) {
        Future<List<Ghost>> getGhosts = executor.submit(ghostsWorker);
        ghosts = (ArrayList<Ghost>) getGhosts.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return ghosts;
  }

  public void createOrUpdateGhost(final Ghost ghost) {
    if (hasGhostImprovedTime(ghost)) {
      logger.info("Updating ghost with user id {} for circuit with id {}", ghost.getUserId(),
                  ghost.getCircuitId());

      try {
        ExecutorService executor = Executors.newCachedThreadPool();

        final Callable<Void> updateGhostWorker = new AbstractServiceClient<Void>() {

          @Override
          protected Void callImpl() throws Exception {
            client.register(user.getBasicAuth());
            webTarget = client.target(super.BASE_URL).path("ghosts");
            WebTarget updateWebTarget = webTarget.path("update");

            String json = new Gson().toJson(ghost);

            // Create new form for adding parameters
            Form form = new Form();
            form.param("json", json);

            Response resp =
                updateWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));

            if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
              // everything OK
              return null;
            } else {
              logger.error("Failed to update ghost");
              throw new ApiPostRequestException("Request failed: " + resp.getLocation());
            }
          }

        };
        Future<Void> updateGhost = executor.submit(updateGhostWorker);
        updateGhost.get();

        executor.shutdown();
      } catch (InterruptedException e) {
        logger.error(e.getMessage());
      } catch (ExecutionException e) {
        logger.error(e.getMessage());
      }
    }

    if (loadUserGhost(ghost.getCircuitId()) == null) {
      logger.info("Creating ghost with user id {} for circuit with id {}", ghost.getUserId(),
                  ghost.getCircuitId());

      try {
        ExecutorService executor = Executors.newCachedThreadPool();

        final Callable<Void> createGhostWorker = new AbstractServiceClient<Void>() {

          @Override
          protected Void callImpl() throws Exception {
            client.register(user.getBasicAuth());
            webTarget = client.target(super.BASE_URL).path("ghosts");
            WebTarget createWebTarget = webTarget.path("create");

            String json = new Gson().toJson(ghost);

            // Create new form for adding parameters
            Form form = new Form();
            form.param("json", json);

            Response resp =
                createWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));

            if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
              // everything OK
              return null;
            } else {
              logger.error("Failed to create ghost");
              throw new ApiPostRequestException("Request failed: " + resp.getLocation());
            }
          }
        };

        Future<Void> createGhost = executor.submit(createGhostWorker);
        createGhost.get();

        executor.shutdown();
      } catch (InterruptedException e) {
        logger.error("Error occured: " + e.getMessage());
      } catch (ExecutionException e) {
        logger.error("Error occured: " + e.getMessage());
      }
    }
  }

  public Ghost loadUserGhost(final int circuitId) {
    logger.info("Getting ghost of user for circuit with id {}", circuitId);

    Ghost ghost = null;

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<Ghost> ghostsWorker = new AbstractServiceClient<Ghost>() {

        @Override
        protected Ghost callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("ghosts");
          WebTarget ghostsWebTarget =
              webTarget.path("circuit/" + circuitId).path("user/" + user.getId());
          Response resp = ghostsWebTarget.request(MediaType.APPLICATION_JSON).get();


          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("request for ghosts OK");
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<Ghost>() {}.getType());
          } else {
            return null;
          }
        }

      };

      if (user != null) {
        Future<Ghost> getGhosts = executor.submit(ghostsWorker);
        ghost = getGhosts.get();
      }
      executor.shutdown();

    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return ghost;
  }

  public Ghost loadFastestGhost(final int circuitId) {
    logger.info("Getting fastest ghost for circuit with id {}", circuitId);

    Ghost ghost = null;

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<Ghost> ghostsWorker = new AbstractServiceClient<Ghost>() {

        @Override
        protected Ghost callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("ghosts");

          WebTarget ghostsWebTarget =
              webTarget.path("circuit").path(Integer.toString(circuitId)).path("fastest");

          Response resp = ghostsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("request for ghosts OK");
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<Ghost>() {}.getType());
          } else {
            return null;
          }
        }

      };

      if (user != null) {
        Future<Ghost> getGhosts = executor.submit(ghostsWorker);
        ghost = getGhosts.get();
      }
      executor.shutdown();

    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return ghost;
  }

  private boolean hasGhostImprovedTime(Ghost newGhost) {
    Ghost ghost = null;

    ghost = loadUserGhost(newGhost.getCircuitId());

    if (ghost != null) {
      if (newGhost.getTime() < ghost.getTime()) {
        newGhost.setId(ghost.getId());
        newGhost.setUsername(ghost.getUsername());
        return true;
      }
    }

    return false;
  }
}
