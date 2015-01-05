package be.tiwi.vop.racing.service.client;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.xml.bind.DatatypeConverter;

public class BasicAuthFeature implements ClientRequestFilter {
  private String authString;

  public BasicAuthFeature(String authentification) {
    this.authString = authentification;
  }

  public BasicAuthFeature(String username, String password) {
    this.authString = getBase64(username, password);
  }

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    requestContext.getHeaders().add("Authorization", "Basic " + authString);
  }

  private String getBase64(String username, String password) {
    String original = username + ":" + password;
    return DatatypeConverter.printBase64Binary(original.getBytes());
  }

  public String getAuthString() {
    return this.authString;
  }

  public static String[] decode(String authString) {
    // Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
    authString = authString.replaceFirst("[B|b]asic ", "");

    // Decode the Base64 into byte[]
    byte[] decodedBytes = DatatypeConverter.parseBase64Binary(authString);

    // If the decode fails in any case
    if (decodedBytes == null || decodedBytes.length == 0) {
      return null;
    }

    // Now we can convert the byte[] into a splitted array :
    // - the first one is login,
    // - the second one password
    return new String(decodedBytes).split(":", 2);
  }

}
