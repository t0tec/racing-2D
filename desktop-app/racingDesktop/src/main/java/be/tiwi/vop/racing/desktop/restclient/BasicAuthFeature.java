package be.tiwi.vop.racing.desktop.restclient;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.xml.bind.DatatypeConverter;

public class BasicAuthFeature implements ClientRequestFilter {
  private String authString;

  public BasicAuthFeature(String authentification) {
    this.authString = authentification;
  }

  public BasicAuthFeature(String username, String password) {
    this.authString = getBase64(username + ":" + password);
  }

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    requestContext.getHeaders().add("Authorization", "Basic " + authString);
  }

  private String getBase64(String value) {
    byte[] val = value.getBytes(Charset.forName("UTF-8"));
    return DatatypeConverter.printBase64Binary(val);
  }

  public String getAuthString() {
    return this.authString;
  }

}
