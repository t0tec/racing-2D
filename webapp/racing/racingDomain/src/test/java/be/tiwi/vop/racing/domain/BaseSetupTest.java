package be.tiwi.vop.racing.domain;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.domain.DaoFactory;
import be.tiwi.vop.racing.domain.DaoManager;

public class BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(BaseSetupTest.class);

  protected static DaoManager daoManager;

  private static boolean initialized = true;

  private static void setup() {
    if (!initialized) {
      return;
    } else {
      logger.info("Initializing daoManger");
      initialized = false;
      daoManager = DaoFactory.createHSQLDBDAOManager();
    }
  }

  @BeforeClass
  public static void setupDAOManager() {
    setup();
  }
}
