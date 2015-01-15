package be.tiwi.vop.racing.api.webservice;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import be.tiwi.vop.racing.api.model.ProjectManager;
import be.tiwi.vop.racing.core.model.User;
import be.tiwi.vop.racing.api.transformer.FeedTransformer;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Path("users")
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Produces("application/json")
  @GET
  public String getUsers() {
    String feeds = null;
    try {
      ArrayList<User> feedData = null;
      ProjectManager projectManager = new ProjectManager();
      feedData = projectManager.GetUsers();
      feeds = FeedTransformer.userFeed(feedData);
      return feeds;
    } catch (Exception e) {
      logger.error("Exception Error");
      return "error:" + e.getMessage();
    }
  }

  @Produces("application/json")
  @GET
  @Path("/{id}")
  public String getUserById(@PathParam("id") String id) {
    ProjectManager pm = new ProjectManager();
    User user = pm.getUserById(Integer.parseInt(id));
    return FeedTransformer.getUser(user);
  }

  @Produces("application/json")
  @GET
  @Path("login/{username}")
  public String getUserByUsername(@PathParam("username") String username) {
    ProjectManager pm = new ProjectManager();
    User user = pm.getUserByUsername(username);
    return FeedTransformer.getUser(user);
  }

  @Produces("application/json")
  @Path("register")
  @POST
  public String registerUser(@FormParam("username") String username,
      @FormParam("password") String password, @FormParam("email") String email,
      @FormParam("fullname") String fullname) {
    User user = new User();
    ProjectManager pm = new ProjectManager();
    if (pm.getUserByUsername(username) == null && username != null && !username.isEmpty()
        && password != null && !password.isEmpty()) {
      user.setEmail(email);
      user.setPassword(FeedTransformer.getSHA512(password));
      user.setFullName(fullname);
      user.setUsername(username);
      pm.insertUser(user);

      user = pm.getUserByUsername(username);
      return FeedTransformer.getUser(user);
    } else {
      return null;
    }
  }

  @Produces("application/json")
  @Path("update/{username}")
  @POST
  public String updateUser(@PathParam("username") String username,
      @FormParam("fullName") String fullName, @FormParam("email") String email,
      @FormParam("password") String password, @FormParam("isPublic") String isPublic) {

    if (username != null) {
      ProjectManager pm = new ProjectManager();
      User user = pm.getUserByUsername(username);

      if (user != null) {
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(FeedTransformer.getSHA512(password));
        user.setIsPublic(Boolean.valueOf(isPublic));

        pm.updateUser(user);

        return FeedTransformer.getUser(user);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
