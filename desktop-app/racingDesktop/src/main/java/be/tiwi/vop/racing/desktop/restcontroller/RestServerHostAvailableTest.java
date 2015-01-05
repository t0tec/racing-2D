package be.tiwi.vop.racing.desktop.restcontroller;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestServerHostAvailableTest {
  private static final Logger logger = LoggerFactory.getLogger(RestServerHostAvailableTest.class);

  // TODO change to appropriate environment -- localhost -- staging.be -- release.be
  public static final String HOST = "localhost";

  public static boolean hostAvailabilityCheck() {
    try {
      new Socket(HOST, 8080); // TODO 8080 for localhost
      return true;
    } catch (IOException ex) {
      logger.error("REST server at {} is offline or there is an internal server errror!", HOST);
    }
    return false;
  }
}
