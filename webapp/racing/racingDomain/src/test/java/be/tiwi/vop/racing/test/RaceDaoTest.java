package be.tiwi.vop.racing.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.DaoCommand;
import be.tiwi.vop.racing.DaoManager;
import be.tiwi.vop.racing.factory.test.BaseSetupTest;
import be.tiwi.vop.racing.core.model.Race;

public class RaceDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(RaceDaoTest.class);

  @Test
  public void getAllRacesByTournament() {
    logger.info("Getting all races by tournament");

    List<Race> races = daoManager.getRaceDao().getRacesByTournamentId(1);
    Assert.assertTrue(races.size() > 0);
  }

  @Test
  public void insertDummyRaces() {
    logger.info("Inserting dummy races to a tournament");
    final ArrayList<Race> races = new ArrayList<Race>();

    for (int i = 0; i < 3; i++) {
      Race race = new Race();
      race.setCircuitId(i + 1);
      race.setLaps(i + 1);
      race.setTournamentId(1);
      races.add(race);
    }

    daoManager.transaction(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getRaceDaoTx().insertRaces(races);
        return null;
      }
    });

    for (Race r : races) {
      Assert.assertNotNull(r.getId());
    }
  }
}
