package be.tiwi.vop.racing.service.client;

import be.tiwi.vop.racing.core.model.User;
import com.google.gson.Gson;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceClient extends AbstractServiceClient {
  private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
  private BasicAuthFeature auth;
  private String username;

  public UserServiceClient() {
    webTarget = client.target(super.BASE).path("users");
  }

  public UserServiceClient(String user, String password) {
    auth = new BasicAuthFeature(user, password);
    client.register(auth);
    webTarget = client.target(super.BASE).path("users");
    this.username = user;
  }

  public UserServiceClient(String authString) {
    auth = new BasicAuthFeature(authString);
    client.register(auth);
    this.username = BasicAuthFeature.decode(authString)[0];
    webTarget = client.target(super.BASE).path("users");
  }

  public User authenticateUser() {
    WebTarget loginWebTarget = webTarget.path("login/" + username);
    logger.info(loginWebTarget.getUri().toString());

    Invocation.Builder invocationBuilder = loginWebTarget.request(MediaType.APPLICATION_JSON);

    Response resp = invocationBuilder.get();

    if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
      logger.info("User " + username + " found");
      return new Gson().fromJson(resp.readEntity(String.class), User.class);
    } else {
      logger.info("User " + username + " NOT found");
      return null;
    }

  }

  public boolean registerUser(String username, String password, String email, String fullName) {
    WebTarget registerWebTarget = webTarget.path("register");
    logger.info(registerWebTarget.getUri().toString());

    // Create new form
    Form form = new Form();
    form.param("username", username);
    form.param("password", password);
    form.param("email", email);
    form.param("fullname", fullName);

    Response resp =
        registerWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));

    return (resp.getStatus() == Response.Status.OK.getStatusCode());
  }

  public boolean updateUser(String username, String fullName, String email, String password,
      boolean isPublic) {
    WebTarget updateWebTarget = webTarget.path("update").path(username);

    // Create new form
    Form form = new Form();
    form.param("username", username);
    form.param("fullName", fullName);
    form.param("email", email);
    form.param("password", password);
    form.param("isPublic", Boolean.toString(isPublic));

    Response resp = updateWebTarget.request().post(Entity.entity(form, MediaType.APPLICATION_JSON));

    return (resp.getStatus() == Response.Status.OK.getStatusCode());

  }

  public String getAuthString() {
    try {
      return this.auth.getAuthString();
    } catch (NullPointerException exp) {
      logger.error("Set password and username before getting the auth string");
    }

    return null;
  }

}
