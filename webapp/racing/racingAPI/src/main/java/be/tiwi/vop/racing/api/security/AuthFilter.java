package be.tiwi.vop.racing.api.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.api.model.ProjectManager;
import be.tiwi.vop.racing.pojo.User;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthFilter implements ContainerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

  /**
   * Apply the filter : check input request, validate or not with user auth
   * 
   * @param containerRequest The request from Tomcat server
   */
  @Override
  public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {
    // GET, POST, PUT, DELETE, ...
    String method = containerRequest.getMethod();
    // myresource/get/56bCA for example
    String path = containerRequest.getPath(true);

    logger.info("request path: {}", path);

    // We do allow wadl to be retrieve
    if (method.equals("GET")
        && (path.equals("application.wadl") || path.equals("application.wadl/xsd0.xsd"))) {
      return containerRequest;
    }

    if (path.contains("index.html") || path.contains("register") || path.contains("create")
        || path.equals("circuits") || path.contains("insert") || path.contains("events")) {
      // when registering users, you don't need to be logged in of course
      return containerRequest;
    }

    // Get the authentification passed in HTTP headers parameters
    String auth = containerRequest.getHeaderValue("authorization");

    // If the user does not have the right (does not provide any HTTP Basic Auth)
    if (auth == null) {
      throw new WebApplicationException(Status.UNAUTHORIZED);
    }

    // lap : loginAndPassword
    String[] lap = BasicAuth.decode(auth);
    // logger.info("user: " + lap[0] + " -- password: " + lap[1]);

    // If login or password fail
    if (lap == null || lap.length != 2) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    // DO YOUR DATABASE CHECK HERE (replace that line behind)...
    User authenticationResult = null;

    authenticationResult = new ProjectManager().authenticateUser(lap[0], lap[1]);
    if (authenticationResult == null) {
      // Our system refuse login and password
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    // TODO : HERE YOU SHOULD ADD PARAMETER TO REQUEST, TO REMEMBER USER ON YOUR REST SERVICE...
    return containerRequest;
  }
}
