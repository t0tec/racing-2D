package be.tiwi.vop.racing.service.client;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.core.model.Circuit;
import be.tiwi.vop.racing.core.model.Obstacle;
import be.tiwi.vop.racing.core.model.Tile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CircuitServiceClient extends AbstractServiceClient {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
  private BasicAuthFeature auth;

  public CircuitServiceClient() {
    webTarget = client.target(super.BASE).path("circuits");
  }

  public CircuitServiceClient(String authString) {
    auth = new BasicAuthFeature(authString);
    client.register(auth);
    webTarget = client.target(super.BASE).path("circuits");
  }

  public List<Circuit> getCircuitsByUserId(int userId) {

    WebTarget circuitsWebTarget = webTarget.path("user/" + userId);
    logger.info(circuitsWebTarget.getUri().toString());

    Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("request for circuits OK");
      return new Gson().fromJson(resp.readEntity(String.class),
          new TypeToken<List<Circuit>>() {}.getType());
    } else {
      return null;
    }

  }

  public List<Circuit> getFavoriteCircuitsByUserId(int userId) {

    WebTarget circuitsWebTarget =
        webTarget.path("user").path(Integer.toString(userId)).path("favorites");
    logger.info(circuitsWebTarget.getUri().toString());

    Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("request for circuits OK");
      return new Gson().fromJson(resp.readEntity(String.class),
          new TypeToken<List<Circuit>>() {}.getType());
    } else {
      return null;
    }

  }

  public List<Tile> getTilesByCircuitId(int circuitId) {

    WebTarget circuitsWebTarget = webTarget.path(circuitId + "/tiles/");
    logger.info(circuitsWebTarget.getUri().toString());

    Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("request for circuits OK");
      return new Gson().fromJson(resp.readEntity(String.class),
          new TypeToken<List<Tile>>() {}.getType());
    } else {
      return null;
    }

  }

  public List<Obstacle> getObstaclesByCircuitId(int circuitId) {

    WebTarget circuitsWebTarget = webTarget.path(circuitId + "/obstacles/");
    logger.info(circuitsWebTarget.getUri().toString());

    Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("request for obstacles OK");
      return new Gson().fromJson(resp.readEntity(String.class),
          new TypeToken<List<Obstacle>>() {}.getType());
    } else {
      return null;
    }

  }

  public Circuit getCircuitByCircuitId(int circuitId) {

    WebTarget circuitsWebTarget = webTarget.path(circuitId + "/info");
    logger.info(circuitsWebTarget.getUri().toString());

    Response resp = circuitsWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("request for circuits OK");
      return new Gson().fromJson(resp.readEntity(String.class), Circuit.class);
    } else {
      return null;
    }
  }

  public Circuit createCircuit(Circuit circuit, String tiles, String obstacles) {

    WebTarget circuitsWebTarget = webTarget.path("create/" + circuit.getDesigner());
    logger.info(circuitsWebTarget.getUri().toString());

    String strCircuit = new Gson().toJson(circuit);
    // Create new form
    Form form = new Form();
    form.param("circuit", strCircuit);
    form.param("tiles", tiles);
    form.param("obstacles", obstacles);

    Response resp =
        circuitsWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));
    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("Circuit " + circuit.getName() + " is succesfully inserted.");
      return new Gson().fromJson(resp.readEntity(String.class), Circuit.class);
    } else {
      return null;
    }

  }

  public Circuit updateCircuit(Circuit circuit, String tiles, String insertTiles,
      String deleteTiles, String obstacles, String updateObstacles, String deleteObstacles) {

    WebTarget circuitsWebTarget = webTarget.path(circuit.getId() + "/update");
    logger.info(circuitsWebTarget.getUri().toString());

    String strCircuit = new Gson().toJson(circuit);
    // Create new form
    Form form = new Form();
    form.param("circuit", strCircuit);
    form.param("tiles", tiles);
    form.param("insertTiles", insertTiles);
    form.param("deleteTiles", deleteTiles);
    form.param("obstacles", obstacles);
    form.param("updateObstacles", updateObstacles);
    form.param("deleteObstacles", deleteObstacles);

    Response resp =
        circuitsWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));
    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("Circuit " + circuit.getName() + " is succesfully updated.");
      return new Gson().fromJson(resp.readEntity(String.class), Circuit.class);
    } else {
      return null;
    }

  }

  public int favoriteCircuit(int userId, int circuitId) {
    WebTarget circuitWebTarget = webTarget.path(circuitId + "/favorite/" + userId);
    logger.info(circuitWebTarget.getUri().toString());

    Response resp = circuitWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      return resp.getStatus();
    } else {
      return resp.getStatus();
    }
  }

  public int unfavoriteCircuit(int userId, int circuitId) {
    WebTarget circuitWebTarget = webTarget.path(circuitId + "/unfavorite/" + userId);
    logger.info(circuitWebTarget.getUri().toString());

    Response resp = circuitWebTarget.request(MediaType.APPLICATION_JSON).get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      return resp.getStatus();
    } else {
      return resp.getStatus();
    }
  }

}
