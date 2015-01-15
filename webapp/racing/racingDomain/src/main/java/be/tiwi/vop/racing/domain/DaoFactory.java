package be.tiwi.vop.racing.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class DaoFactory {
  private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);

  @Autowired
  private static GenericXmlApplicationContext applicationContext =
      new GenericXmlApplicationContext();

  // "development" -- "staging" -- "release"
  private static final String ACTIVE_PROFILE = "development";

  // TODO : Change variable ACTIVE_PROFILE to activate the correct profile if you deploy on
  // staging/release web server
  // -- development -- for deploying on local tomcat server
  // -- staging -- for deploying on staging server
  // -- release -- for deploying on release server
  public static DaoManager createMySQLDAOManager() {
    logger.info("Setting up datasource to " + ACTIVE_PROFILE);
    return setupManager(ACTIVE_PROFILE);
  }

  public static DaoManager createHSQLDBDAOManager() {
    logger.info("Setting up datasource to in-memory HSQLDB");
    return setupManager("test");
  }

  private static DaoManager setupManager(final String activeProfile) {
    ConfigurableEnvironment env = (ConfigurableEnvironment) applicationContext.getEnvironment();
    env.setActiveProfiles(activeProfile);

    applicationContext.load("classpath:/applicationContext.xml");
    applicationContext.refresh();
    return applicationContext.getBean("manager", DaoManager.class);
  }
}
