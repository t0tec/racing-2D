package be.tiwi.vop.racing.desktop.restclient;

import java.util.concurrent.Callable;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public abstract class AbstractServiceClient<T> implements Callable<T> {
  // TODO change to appropriate environment
  // http://localhost:8080/api -- http://staging.be/api -- http://release.be/api
  protected static final String BASE_URL = "http://localhost:8080/api";
  protected Client client = ClientBuilder.newClient();
  protected WebTarget webTarget;

  @Override
  public final T call() throws Exception {
    return callImpl();
  }

  protected abstract T callImpl() throws Exception;
}
