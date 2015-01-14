package be.tiwi.vop.racing.api.webservice;

import java.util.ArrayList;
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
import be.tiwi.vop.racing.model.Circuit;
import be.tiwi.vop.racing.model.Obstacle;
import be.tiwi.vop.racing.model.ObstacleType;
import be.tiwi.vop.racing.model.Tile;
import be.tiwi.vop.racing.model.TileType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Component
@Path("circuits")
public class CircuitService {

  private static final Logger logger = LoggerFactory.getLogger(CircuitService.class);

  @GET
  @Produces("application/json")
  public String getCircuits() {
    logger.info("Getting all circuits");
    ProjectManager projectManager = new ProjectManager();
    ArrayList<Circuit> feedData = projectManager.getCircuits();
    String feeds = FeedTransformer.circuitFeed(feedData);

    return feeds;
  }

  @GET
  @Produces("application/json")
  @Path("user/{userId}/favorites")
  public String getFavoriteCircuitsByUserId(@PathParam("userId") int userId) {
    logger.info("Getting all favorite circuits from user_id {}", userId);
    ProjectManager projectManager = new ProjectManager();
    ArrayList<Circuit> feedData = projectManager.getFavoriteCircuitsByUser(userId);
    String feeds = FeedTransformer.circuitFeed(feedData);

    return feeds;
  }

  @Produces("application/json")
  @GET
  @Path("user/{designerId}")
  public String getCircuitByDesigner(@PathParam("designerId") String designerId) {
    logger.info("Getting circuits by designer id: {}", designerId);
    ProjectManager pm = new ProjectManager();

    return FeedTransformer.circuitFeed(pm.getCircuitsByDesigner(Integer.parseInt(designerId)));
  }

  @Produces("application/json")
  @GET
  @Path("{id}/info")
  public String getCircuitInfoById(@PathParam("id") String id) {
    logger.info("Getting info about circuit by id");
    ProjectManager pm = new ProjectManager();

    return FeedTransformer.getCircuit(pm.getCircuitInfoById(Integer.parseInt(id)));
  }

  @Produces("application/json")
  @GET
  @Path("/{id}")
  public String getCircuitWithTiles(@PathParam("id") String id) {
    logger.info("Getting circuit by id with tiles");
    ProjectManager pm = new ProjectManager();
    Circuit circuit = pm.getCircuitInfoById(Integer.parseInt(id));
    ArrayList<Tile> tiles = (ArrayList<Tile>) pm.getTilesByCircuitId(circuit.getId());
    circuit.setTiles(tiles);

    return FeedTransformer.getCircuit(circuit);
  }

  @Produces("application/json")
  @GET
  @Path("{id}/tiles/")
  public String getTilesByCircuitId(@PathParam("id") String circuitid) {
    logger.info("Get tiles by circuit id");
    List<Tile> tiles = null;
    ProjectManager pm = new ProjectManager();
    tiles = pm.getTilesByCircuitId(Integer.parseInt(circuitid));

    return FeedTransformer.tilesFeed(tiles);
  }

  @Produces("application/json")
  @GET
  @Path("{id}/obstacles/")
  public String getObstaclesByCircuitId(@PathParam("id") String circuitid) {
    logger.info("Get obstacles by circuit id");
    List<Obstacle> obstacles = null;
    ProjectManager pm = new ProjectManager();
    obstacles = pm.getObstaclesByCircuitId(Integer.parseInt(circuitid));

    return FeedTransformer.obstaclesFeed(obstacles);
  }

  @Produces("application/json")
  @POST
  @Path("{circuitId}/update")
  public String updateCircuit(@PathParam("circuitId") String circuitId,
      @FormParam("circuit") String strCircuit, @FormParam("tiles") String strTiles,
      @FormParam("insertTiles") String strInsertTiles,
      @FormParam("deleteTiles") String strDeleteTiles, @FormParam("obstacles") String strObstacles,
      @FormParam("updateObstacles") String strUpdateObstacles,
      @FormParam("deleteObstacles") String strDeleteObstacles) {
    logger.info("Updating a circuit " + circuitId);

    logger.info(strCircuit);
    logger.info(strTiles);
    logger.info(strInsertTiles);
    logger.info(strDeleteTiles);
    logger.info(strObstacles);
    logger.info(strUpdateObstacles);
    logger.info(strDeleteObstacles);

    // update circuit
    ProjectManager pm = new ProjectManager();
    Circuit circuit = new Gson().fromJson(strCircuit, Circuit.class);
    pm.updateCircuit(Integer.parseInt(circuitId), circuit);

    // update tiles
    ArrayList<Tile> tiles = new Gson().fromJson(strTiles, new TypeToken<List<Tile>>() {}.getType());
    ArrayList<Tile> insertTiles =
        new Gson().fromJson(strInsertTiles, new TypeToken<List<Tile>>() {}.getType());
    ArrayList<Tile> deleteTiles =
        new Gson().fromJson(strDeleteTiles, new TypeToken<List<Tile>>() {}.getType());
    JsonArray jArrayUpdateObstacles = new JsonParser().parse(strUpdateObstacles).getAsJsonArray();
    ArrayList<Obstacle> updateObstacles = new ArrayList<Obstacle>();
    JsonArray jArrayObstacles = new JsonParser().parse(strObstacles).getAsJsonArray();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    JsonArray jArrayDeleteObstacles = new JsonParser().parse(strDeleteObstacles).getAsJsonArray();
    ArrayList<Obstacle> deleteObstacles = new ArrayList<Obstacle>();

    if (tiles.size() > 0) {
      pm.updateTiles(tiles, circuit.getDesigner(), circuit.getName());
    }
    if (insertTiles.size() > 0) {
      pm.insertTiles(insertTiles, circuit.getDesigner(), circuit.getName());
    }
    if (deleteTiles.size() > 0) {
      pm.deleteTiles(deleteTiles, circuit.getDesigner(), circuit.getName());
    }
    tiles.addAll(insertTiles);
    tiles.addAll(deleteTiles);
    tiles.addAll(pm.getTilesByCircuitId(Integer.parseInt(circuitId)));

    for (int i = 0; i < jArrayUpdateObstacles.size(); i++) {
      JsonObject jsonObject = jArrayUpdateObstacles.get(i).getAsJsonObject();
      Obstacle obst = new Obstacle();
      obst.setId(jsonObject.get("id").getAsInt());
      obst.setPlace(jsonObject.get("place").toString().replaceAll("^\"|\"$", ""));
      obst.setObstacleType(ObstacleType.valueOf(jsonObject.get("obstacleType").toString()
          .replaceAll("^\"|\"$", "")));
      obst.setTileId(jsonObject.get("tileId").getAsInt());
      updateObstacles.add(obst);
    }
    if (updateObstacles.size() > 0) {
      pm.updateObstacles(updateObstacles, circuit.getDesigner(), circuit.getName());
    }

    for (int i = 0; i < jArrayDeleteObstacles.size(); i++) {
      JsonObject jsonObject = jArrayDeleteObstacles.get(i).getAsJsonObject();
      Obstacle obst = new Obstacle();
      obst.setId(jsonObject.get("id").getAsInt());
      obst.setPlace(jsonObject.get("place").toString().replaceAll("^\"|\"$", ""));
      obst.setObstacleType(ObstacleType.valueOf(jsonObject.get("obstacleType").toString()
          .replaceAll("^\"|\"$", "")));
      obst.setTileId(jsonObject.get("tileId").getAsInt());
      deleteObstacles.add(obst);
    }

    if (deleteObstacles.size() > 0) {
      pm.deleteObstacles(deleteObstacles, circuit.getDesigner(), circuit.getName());
    }

    if (tiles.size() > 0) {
      circuit.setTiles(tiles);
      for (Tile t : circuit.getTiles()) {
        logger.info("id: " + t.getId());
        for (int i = 0; i < jArrayObstacles.size(); i++) {
          JsonObject jsonObject = jArrayObstacles.get(i).getAsJsonObject();
          int col = Integer.parseInt(jsonObject.get("col").toString());
          int row = Integer.parseInt(jsonObject.get("row").toString());
          if (t.getX() == col && t.getY() == row) {
            Obstacle obst = new Obstacle();
            obst.setPlace(jsonObject.get("place").toString().replaceAll("^\"|\"$", ""));
            obst.setObstacleType(ObstacleType.valueOf(jsonObject.get("obstacleType").toString()
                .replaceAll("^\"|\"$", "")));
            obst.setTileId(t.getId());
            obstacles.add(obst);
          }
        }
      }
      if (obstacles.size() > 0) {
        pm.insertObstacles(obstacles, circuit.getDesigner(), circuit.getName());
      }
    }

    return FeedTransformer.getCircuit(circuit);
  }

  @Produces("application/json")
  @POST
  @Path("/create/{userId}/")
  public String createCircuit(@PathParam("userId") String userId,
      @FormParam("circuit") String strCircuit, @FormParam("tiles") String json,
      @FormParam("obstacles") String jsonObstacles) {
    logger.info("Creating a new circuit");
    logger.info("user id: " + userId);
    logger.info("content: " + json);

    ProjectManager pm = new ProjectManager();
    Circuit circuit = new Gson().fromJson(strCircuit, Circuit.class);

    pm.insertCircuit(circuit);

    JsonArray jArray = new JsonParser().parse(json).getAsJsonArray();
    ArrayList<Tile> tiles = new ArrayList<Tile>();

    for (int i = 0; i < jArray.size(); i++) {
      Tile tile = new Tile();
      JsonObject jsonObject = jArray.get(i).getAsJsonObject();
      String type = jsonObject.get("type").getAsString();
      logger.info("type: " + type);
      logger.info("circuit id: " + circuit.getId());
      tile.setType(TileType.valueOf(type));
      tile.setX(Integer.parseInt(jsonObject.get("x").toString()));
      tile.setY(Integer.parseInt(jsonObject.get("y").toString()));
      tile.setCircuitid(circuit.getId());
      tile.setCheckpoint(Integer.parseInt(jsonObject.get("checkpoint").toString()));
      tiles.add(tile);
    }

    JsonArray jArrayObstacles = new JsonParser().parse(jsonObstacles).getAsJsonArray();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    if (tiles.size() > 0) {
      pm.insertTiles(tiles, circuit.getDesigner(), circuit.getName());
      circuit.setTiles(tiles);
      for (Tile t : circuit.getTiles()) {
        logger.info("id: " + t.getId());
        for (int i = 0; i < jArrayObstacles.size(); i++) {
          JsonObject jsonObject = jArrayObstacles.get(i).getAsJsonObject();
          int col = Integer.parseInt(jsonObject.get("col").toString());
          int row = Integer.parseInt(jsonObject.get("row").toString());
          if (t.getX() == col && t.getY() == row) {
            Obstacle obst = new Obstacle();
            obst.setPlace(jsonObject.get("place").toString().replaceAll("^\"|\"$", ""));
            obst.setObstacleType(ObstacleType.valueOf(jsonObject.get("obstacleType").toString()
                .replaceAll("^\"|\"$", "")));
            obst.setTileId(t.getId());
            obstacles.add(obst);
          }
        }
      }

      if (obstacles.size() > 0) {
        pm.insertObstacles(obstacles, circuit.getDesigner(), circuit.getName());
      }
      return FeedTransformer.getCircuit(circuit);
    } else {
      return null;
    }
  }

  @Produces("application/json")
  @Path("{circuit_id}/favorite/{user_id}")
  @GET
  public void favoriteCircuit(@PathParam("circuit_id") int circuitId,
      @PathParam("user_id") int userId) {
    logger.info("favoriting ciruit_id {} for user_id {}", circuitId, userId);
    try {
      ProjectManager projectManager = new ProjectManager();
      projectManager.favoriteCircuit(userId, circuitId);
    } catch (Exception e) {
      logger.error("Exception error");
    }
  }

  @Produces("application/json")
  @Path("{circuit_id}/unfavorite/{user_id}")
  @GET
  public void unfavoriteCircuit(@PathParam("circuit_id") int circuitId,
      @PathParam("user_id") int userId) {
    logger.info("Unfavoriting ciruit_id {} for user_id {}", circuitId, userId);
    try {
      ProjectManager projectManager = new ProjectManager();
      projectManager.unfavoriteCircuit(userId, circuitId);
    } catch (Exception e) {
      logger.error("Exception error");
    }
  }

  @Produces("application/json")
  @GET
  @Path("user/favorite/{userId}")
  public String getFavoriteCircuitByDesigner(@PathParam("userId") String userid) {
    logger.info("Getting favorite circuits by user id");
    ProjectManager pm = new ProjectManager();

    return FeedTransformer.circuitFeed(pm.getFavoriteCircuitsByDesigner(Integer.parseInt(userid)));
  }
}
