package be.tiwi.vop.racing.desktop.restcontroller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.exceptions.UserNotFoundException;
import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.model.user.User;
import be.tiwi.vop.racing.desktop.restclient.AbstractServiceClient;
import be.tiwi.vop.racing.desktop.restclient.BasicAuthFeature;

import com.google.gson.Gson;

public class LoginRestController {
  private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);

  private volatile AuthenticatedUser user;

  public void loginUser(final String username, final String password) throws UserNotFoundException {
    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<AuthenticatedUser> userWorker = new AbstractServiceClient<AuthenticatedUser>() {

        private BasicAuthFeature auth;

        @Override
        protected AuthenticatedUser callImpl() throws Exception {
          User user = authenticateUser();

          if (user != null) {
            AuthenticatedUser aus = AuthenticatedUser.getInstance();
            aus.setId(user.getId());
            aus.setUsername(user.getUsername());
            aus.setPassword(user.getPassword());
            aus.setEmail(user.getPassword());
            aus.setFullName(user.getFullName());
            aus.setBasicAuth(this.auth);

            logger.info("User {} has successfully authenticated!", AuthenticatedUser.getInstance()
                .getUsername());

            return aus;
          }

          return null;
        }

        public User authenticateUser() {
          this.auth = new BasicAuthFeature(username, password);
          client.register(this.auth);
          webTarget = client.target(super.BASE_URL).path("users");
          WebTarget loginWebTarget = webTarget.path("login/" + username);

          Invocation.Builder invocationBuilder = loginWebTarget.request(MediaType.APPLICATION_JSON);

          Response resp = invocationBuilder.get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            return new Gson().fromJson(resp.readEntity(String.class), User.class);
          } else {
            logger.error("User " + username + " NOT found");
            return null;
          }
        }

      };

      Future<AuthenticatedUser> authenticateUser = executor.submit(userWorker);
      this.user = authenticateUser.get();
      executor.shutdown();
      if (user == null) {
        throw new UserNotFoundException("User not found! Unauthorized!");
      }

    } catch (InterruptedException ex) {
      logger.error("Error occured: " + ex.getMessage());
      throw new RuntimeException(ex);
    } catch (ExecutionException ex) {
      logger.error("Error occured: " + ex.getMessage());
      throw new RuntimeException(ex.getCause());
    }
  }
}
