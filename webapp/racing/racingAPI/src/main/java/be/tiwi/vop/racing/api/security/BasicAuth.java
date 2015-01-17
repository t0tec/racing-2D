package be.tiwi.vop.racing.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

public class BasicAuth {
  private static final Logger logger = LoggerFactory.getLogger(BasicAuth.class);

  /**
   * Decode the basic auth and convert it to array login/password
   *
   * @param auth The string encoded authentification
   * @return The login (case 0), the password (case 1)
   */
  public static String[] decode(String auth) {
    // Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
    String[] lap = new String[2];

    auth = auth.replaceFirst("[B|b]asic ", "");

    // Decode the Base64 into byte[]
    byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

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
