package be.tiwi.vop.racing.api.webservice;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import be.tiwi.vop.racing.api.model.ProjectManager;
import be.tiwi.vop.racing.api.transformer.FeedTransformer;
import be.tiwi.vop.racing.pojo.Ghost;

@Component
@Path("ghosts")
public class GhostService {
  private static final Logger logger = LoggerFactory.getLogger(GhostService.class);

  @GET
  @Produces("application/json")
  public String getGhosts() {
    logger.info("Getting all ghosts");
    ProjectManager pm = new ProjectManager();
    ArrayList<Ghost> feedData = pm.getGhosts();
    String feeds = FeedTransformer.ghostsFeed(feedData);

    return feeds;
  }

  @Produces("application/json")
  @GET
  @Path("circuit/{circuitId}")
  public String getGhostsByCircuitId(@PathParam("circuitId") String circuitId) {
    logger.info("Getting ghosts by circuit id");
    ProjectManager pm = new ProjectManager();

    return FeedTransformer.ghostsFeed(pm.getGhostsByCircuitId(Integer.parseInt(circuitId)));
  }

  @Produces("application/json")
  @GET
  @Path("circuit/{circuitId}/user/{userId}")
  public String getGhostByUserIdAndCircuitId(@PathParam("circuitId") String circuitId,
      @PathParam("userId") String userId) {
    logger.info("Getting ghost by user id and circuit id");
    ProjectManager pm = new ProjectManager();

    return FeedTransformer.getGhost(pm.getGhostByCircuitIdAndGhostId(Integer.parseInt(circuitId),
        Integer.parseInt(userId)));
  }

  @Produces("application/json")
  @GET
  @Path("circuit/{circuitId}/fastest")
  public String getFastestGhostByCircuitId(@PathParam("circuitId") String circuitId) {
    logger.info("Getting fastest ghost by circuit id");
    ProjectManager pm = new ProjectManager();

    return FeedTransformer.getGhost(pm.getFastestGhostByCircuitId(Integer.parseInt(circuitId)));
  }

  @Produces("application/json")
  @Path("/create")
  @POST
  public String createGhost(@FormParam("json") String json) {
    logger.info("Inserting ghost");

    Ghost ghost = new Gson().fromJson(json, Ghost.class);

    if (ghost != null) {
      ProjectManager pm = new ProjectManager();
      pm.insertGhost(ghost);

      logger.info("Inserted ghost({}) with user_id {} and a time of {}", ghost.getId(),
          ghost.getUserId(), ghost.getTime());

      return FeedTransformer.getGhost(ghost);
    }

    return "Creation of ghost failed!";
  }

  @Produces("application/json")
  @Path("/update")
  @POST
  public String updateGhost(@FormParam("json") String json) {
    logger.info("Updating ghost");

    Ghost ghost = new Gson().fromJson(json, Ghost.class);

    if (ghost != null) {
      ProjectManager pm = new ProjectManager();
      pm.updateGhost(ghost);

      logger.info("Updated ghost({}) with user_id {} and a time of {}", ghost.getId(),
          ghost.getUserId(), ghost.getTime());

      return FeedTransformer.getGhost(ghost);
    }

    return "Updating of ghost failed!";
  }
}
