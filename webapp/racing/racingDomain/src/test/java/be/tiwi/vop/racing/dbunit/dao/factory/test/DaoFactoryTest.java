package be.tiwi.vop.racing.dbunit.dao.factory.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoFactoryTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(DaoFactoryTest.class);

  @Test
  public void creationDaoManager() {
    logger.info("Checking if DaoManager is not null");
    Assert.assertNotNull(daoManager);
  }

  @Test
  public void creationUserDao() {
    logger.info("Checking if UserDao is not null");
    Assert.assertNotNull(daoManager.getUserDao());
  }

  @Test
  public void creationCircuitDao() {
    logger.info("Checking if CircuitDao is not null");
    Assert.assertNotNull(daoManager.getCircuitDao());
  }

  @Test
  public void creationTileDao() {
    logger.info("Checking if TileDao is not null");
    Assert.assertNotNull(daoManager.getTileDao());
  }

  @Test
  public void creationTileDaoTx() {
    logger.info("Checking if TileDaoTx is not null");
    Assert.assertNotNull(daoManager.getTileDaoTx());
  }

  @Test
  public void creationGhostDao() {
    logger.info("Checking if GhostDao is not null");
    Assert.assertNotNull(daoManager.getGhostDao());
  }

  @Test
  public void creationTournamentDao() {
    logger.info("Checking if TournamentDao is not null");
    Assert.assertNotNull(daoManager.getTournamentDao());
  }

  @Test
  public void creationRaceDao() {
    logger.info("Checking if RaceDao is not null");
    Assert.assertNotNull(daoManager.getRaceDao());
  }

  @Test
  public void creationRaceDaoTx() {
    logger.info("Checking if RaceDAOTx is not null");
    Assert.assertNotNull(daoManager.getRaceDaoTx());
  }

  @Test
  public void creationResultDao() {
    logger.info("Checking if ResultDao is not null");
    Assert.assertNotNull(daoManager.getResultDao());
  }

}
