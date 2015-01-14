package be.tiwi.vop.racing.test;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.factory.test.BaseSetupTest;
import be.tiwi.vop.racing.model.Ghost;
import be.tiwi.vop.racing.model.Pose;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GhostDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(GhostDaoTest.class);

  @Test
  public void firstInsertGhost() {
    logger.info("Inserting a dummy ghost");
    Ghost ghost = new Ghost();
    ghost.setTime(4000);
    ghost.setUsername("jan");
    ghost.setUserId(2);
    ghost.setCircuitId(2);
    LinkedHashMap<Integer, Pose> myPoses = new LinkedHashMap<Integer, Pose>();

    for (int i = 0; i < 5; i++) {
      myPoses.put(i * 20, new Pose(new Point(i, i), 0.0));
    }

    ghost.setPoses(myPoses);

    daoManager.getGhostDao().insertGhost(ghost);
  }

  @Test
  public void secondgetAllGhost() {
    logger.info("Getting all ghost");

    List<Ghost> ghosts = daoManager.getGhostDao().getGhosts();

    Assert.assertTrue(ghosts.size() > 0);
  }

  @Test
  public void thirdUpdateGhost() {
    logger.info("update a dummy ghost");

    Ghost ghost = new Ghost();
    ghost.setTime(4000);
    ghost.setUsername("jan");
    ghost.setUserId(2);
    ghost.setCircuitId(3);
    LinkedHashMap<Integer, Pose> myPoses = new LinkedHashMap<Integer, Pose>();

    for (int i = 0; i < 5; i++) {
      myPoses.put(i * 20, new Pose(new Point(i, i), 0.0));
    }

    ghost.setPoses(myPoses);

    daoManager.getGhostDao().insertGhost(ghost);

    Ghost updateGhost = daoManager.getGhostDao().getGhostByCircuitIdAndUserId(3, 2);

    updateGhost.setTime(3000);

    daoManager.getGhostDao().updateGhost(updateGhost);

    ghost = daoManager.getGhostDao().getGhostByCircuitIdAndUserId(3, 2);

    Assert.assertNotNull(ghost);
    Assert.assertEquals(3, ghost.getCircuitId());
    Assert.assertEquals(2, ghost.getUserId());
    Assert.assertEquals(3000, ghost.getTime());
  }

  @Test
  public void fourthGetGhostsByCircuitId() {
    logger.info("Getting all ghost by circuit id");

    List<Ghost> ghosts = daoManager.getGhostDao().getGhostsByCircuitId(2);

    Assert.assertTrue(ghosts.size() > 0);
  }

  @Test
  public void lastGetGhostByCircuitIdAndGhostId() {
    logger.info("Getting all ghost");

    Ghost ghost = daoManager.getGhostDao().getGhostByCircuitIdAndUserId(2, 2);

    Assert.assertNotNull(ghost);
    Assert.assertEquals(2, ghost.getCircuitId());
    Assert.assertEquals(2, ghost.getUserId());
    Assert.assertEquals(4000, ghost.getTime());
  }
}
