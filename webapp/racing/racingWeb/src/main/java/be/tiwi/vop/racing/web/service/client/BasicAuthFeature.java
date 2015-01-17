package be.tiwi.vop.racing.web.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.xml.bind.DatatypeConverter;

public class BasicAuthFeature implements ClientRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(BasicAuthFeature.class);

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

  public static String[] decode(String authString) {
    // Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
    String[] lap = new String[2];

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
    try {
      lap = new String(decodedBytes, "UTF-8").split(":", 2);
    } catch (UnsupportedEncodingException ex) {
      logger.error(ex.getMessage());
    }

    return lap;
  }

}
