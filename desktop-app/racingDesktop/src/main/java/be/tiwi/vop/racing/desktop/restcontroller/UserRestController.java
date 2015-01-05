package be.tiwi.vop.racing.desktop.restcontroller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.model.user.User;
import be.tiwi.vop.racing.desktop.restclient.AbstractServiceClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserRestController {
  private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);

  private volatile AuthenticatedUser authenticatedUser = AuthenticatedUser.getInstance();

  public User getUser(final int id) {
    User user = null;
    try {
      ExecutorService executor = Executors.newCachedThreadPool();

      final Callable<User> userWorker = new AbstractServiceClient<User>() {

        @Override
        protected User callImpl() throws Exception {
          client.register(authenticatedUser.getBasicAuth());
          webTarget = client.target(super.BASE_URL).path("users").path(Integer.toString(id));
          Response resp = webTarget.request(MediaType.APPLICATION_JSON).get();

          if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("request for user OK");
            return new Gson().fromJson(resp.readEntity(String.class),
                new TypeToken<User>() {}.getType());
          } else {
            return null;
          }
        }

      };

      if (authenticatedUser != null) {
        Future<User> getUser = executor.submit(userWorker);
        user = getUser.get();
      }
      executor.shutdown();
    } catch (InterruptedException ex) {
      logger.error("Error occured: " + ex.getMessage());
      throw new RuntimeException(ex);
    } catch (ExecutionException ex) {
      logger.error("Error occured: " + ex.getMessage());
      throw new RuntimeException(ex.getCause());
    }

    return user;
  }
}
