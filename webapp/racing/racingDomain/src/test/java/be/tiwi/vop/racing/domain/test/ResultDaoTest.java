package be.tiwi.vop.racing.domain.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.domain.DaoCommand;
import be.tiwi.vop.racing.domain.DaoManager;
import be.tiwi.vop.racing.domain.BaseSetupTest;
import be.tiwi.vop.racing.core.model.Result;

@FixMethodOrder(MethodSorters.JVM)
public class ResultDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(CircuitDaoTest.class);

  @Test
  public void getResultsByRaceIdAndUserId() {
    logger.info("Get results by race id and user id");

    ArrayList<Result> results =
        (ArrayList<Result>) daoManager.getResultDao().getResultsByRaceIdAndUserId(1, 1);

    Assert.assertTrue(results.size() > 0);
  }

  @Test
  public void getResultsByRaceId() {
    logger.info("Get results by race id");

    ArrayList<Result> results = (ArrayList<Result>) daoManager.getResultDao().getResultsByRaceId(1);

    Assert.assertTrue(results.size() > 0);
  }

  @Test
  public void insertDummyResult() {
    logger.info("Insert a dummy result");
    Result dummyResult = new Result();
    dummyResult.setTime(5000);
    dummyResult.setLapNumber(1);
    dummyResult.setRaceId(1);
    dummyResult.setUserId(1);

    daoManager.getResultDao().insertResult(dummyResult);

    Assert.assertNotNull(dummyResult.getId());
  }

  @Test
  public void insertDummyResults() {
    logger.info("Inserting dummy results");
    final List<Result> results = new ArrayList<Result>();

    for (int i = 0; i < 3; i++) {
      Result dummyResult = new Result();
      dummyResult.setTime(5000);
      dummyResult.setLapNumber(i + 1);
      dummyResult.setRaceId(1);
      dummyResult.setUserId(1);
      results.add(dummyResult);
    }

    daoManager.transaction(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getResultDaoTx().insertResults(results);

        return null;
      }
    });

    for (int i = 0; i < results.size(); i++) {
      Assert.assertNotNull(results.get(i).getId());
    }
  }
}
