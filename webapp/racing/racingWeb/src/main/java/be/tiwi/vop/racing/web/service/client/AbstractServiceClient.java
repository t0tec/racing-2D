package be.tiwi.vop.racing.web.service.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class AbstractServiceClient {
  // http://localhost:8080/api -- http://staging.be/api/ -- http://release.be/api/
  protected static final String BASE = "http://localhost:8080/api";
  protected Client client = ClientBuilder.newClient();
  protected WebTarget webTarget;

}
