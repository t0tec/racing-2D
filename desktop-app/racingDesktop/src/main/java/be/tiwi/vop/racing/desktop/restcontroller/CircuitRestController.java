package be.tiwi.vop.racing.desktop.restcontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.circuit.Obstacle;
import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.restclient.AbstractServiceClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CircuitRestController {
  private static final Logger logger = LoggerFactory.getLogger(CircuitRestController.class);

  private volatile AuthenticatedUser user = AuthenticatedUser.getInstance();


  public ArrayList<Circuit> loadCircuits() {
    logger.info("Loading all circuits");

    ArrayList<Circuit> circuits = new ArrayList<Circuit>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Circuit>> circuitsWorker = new AbstractServiceClient<List<Circuit>>() {

        @Override
        protected List<Circuit> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("circuits");
          WebTarget circuitsWebTarget = webTarget;

          Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("request for circuits OK");
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<List<Circuit>>() {}.getType());
          } else {
            return Collections.<Circuit>emptyList();
          }
        }

      };

      if (user != null) {
        Future<List<Circuit>> getCircuits = executor.submit(circuitsWorker);
        circuits = (ArrayList<Circuit>) getCircuits.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return circuits;
  }

  public ArrayList<Circuit> loadCircuitsFromUser() {
    logger.info("Getting all circuits from user with id {}", user.getId());

    ArrayList<Circuit> circuits = new ArrayList<Circuit>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Circuit>> circuitsWorker = new AbstractServiceClient<List<Circuit>>() {

        @Override
        protected List<Circuit> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("circuits");

          WebTarget circuitsWebTarget = webTarget.path("user/" + user.getId());

          Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<Circuit>() {}.getType());
          } else {
            return null;
          }
        }

      };

      if (user != null) {
        Future<List<Circuit>> getCircuits = executor.submit(circuitsWorker);
        circuits = (ArrayList<Circuit>) getCircuits.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return circuits;
  }

  public Circuit loadCircuit(final int circuitId) {
    logger.info("Loading circuit with id {}", circuitId);

    Circuit circuit = new Circuit();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<Circuit> circuitWorker = new AbstractServiceClient<Circuit>() {

        @Override
        protected Circuit callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("circuits");

          WebTarget circuitsWebTarget = webTarget.path(Integer.toString(circuitId));

          Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<Circuit>() {}.getType());
          } else {
            return null;
          }
        }

      };

      if (user != null) {
        Future<Circuit> getCircuit = executor.submit(circuitWorker);
        circuit = getCircuit.get();
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return circuit;
  }

  public ArrayList<Obstacle> loadObstaclesByCircuitId(final int circuitId) {
    logger.info("Loading all obstacles from circuit with id {}", circuitId);

    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<List<Obstacle>> obstaclesWorker = new AbstractServiceClient<List<Obstacle>>() {

        @Override
        protected List<Obstacle> callImpl() throws Exception {
          client.register(user.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("circuits");
          WebTarget circuitsWebTarget =
              webTarget.path(Integer.toString(circuitId)).path("obstacles");

          Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("request for obstacles OK");
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<List<Obstacle>>() {}.getType());
          } else {
            return Collections.<Obstacle>emptyList();
          }
        }

      };

      if (user != null) {
        Future<List<Obstacle>> getObstacles = executor.submit(obstaclesWorker);
        obstacles = new ArrayList<Obstacle>(getObstacles.get());
      }
      executor.shutdown();
    } catch (InterruptedException e) {
      logger.error("Error occured: " + e.getMessage());
    } catch (ExecutionException e) {
      logger.error("Error occured: " + e.getMessage());
    }

    return obstacles;
  }

}
