package be.tiwi.vop.racing.api.webservice;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import be.tiwi.vop.racing.api.model.ProjectManager;
import be.tiwi.vop.racing.api.transformer.FeedTransformer;
import be.tiwi.vop.racing.core.model.Event;

@Component
@Path("events")
public class EventService {

  private static final Logger logger = LoggerFactory.getLogger(CircuitService.class);

  @POST
  @Produces("application/json")
  @Path("{id}")
  public String getEvents(@FormParam("limit1") int limit1, @FormParam("limit2") int limit2,
      @PathParam("id") String userId) {
    logger.info("Getting events");
    ProjectManager projectManager = new ProjectManager();
    List<Event> feedData = projectManager.getEvents(limit1, limit2, Integer.parseInt(userId));
    String feeds = FeedTransformer.eventFeed(feedData);

    return feeds;
  }
}
