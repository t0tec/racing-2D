package be.tiwi.vop.racing.dbunit.dao.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.DaoCommand;
import be.tiwi.vop.racing.DaoManager;
import be.tiwi.vop.racing.dbunit.dao.factory.test.BaseSetupTest;
import be.tiwi.vop.racing.pojo.Circuit;
import be.tiwi.vop.racing.pojo.Circuit.Direction;
import be.tiwi.vop.racing.pojo.Tile;
import be.tiwi.vop.racing.pojo.TileType;

@FixMethodOrder(MethodSorters.JVM)
public class CircuitDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(CircuitDaoTest.class);

  @Test
  public void getCircuitByDesigner() {
    logger.info("Get Circuit");

    Circuit circuit = daoManager.getCircuitDao().getCircuitById(1);
    Assert.assertNotNull(circuit);
  }

  @Test
  public void updateCircuitById() {
    logger.info("Get Circuit");
    Circuit circuit = daoManager.getCircuitDao().getCircuitById(1);
    circuit.setDirection(Direction.DOWN);
    daoManager.getCircuitDao().updateCircuitById(2, circuit);

    Assert.assertEquals(Direction.DOWN, circuit.getDirection());
  }

  @Test
  public void getCircuitWithTilesByDesigner() {
    logger.info("Get Circuit with tiles");

    Circuit circuit = daoManager.getCircuitDao().getCircuitById(1);
    Assert.assertNotNull(circuit);

    List<Tile> tiles = daoManager.getTileDao().getTilesByCircuitId(circuit.getId());
    circuit.setTiles(tiles);
    Assert.assertTrue(circuit.getTiles().size() > 0);

    for (Tile t : circuit.getTiles()) {
      logger.info("x: " + t.getX() + ", y : " + t.getY());
    }
  }

  @Test
  public void insertDummyCircuit() {
    logger.info("Insert a dummy circuit");
    Circuit dummyCircuit = new Circuit();
    dummyCircuit.setDesigner(2);
    dummyCircuit.setName("dummy circuit");
    dummyCircuit.setDirection(Direction.RIGHT);

    daoManager.getCircuitDao().insertCircuit(dummyCircuit);

    Assert.assertNotNull(dummyCircuit.getId());
  }

  @Test
  public void insertDummyCircuitWithTiles() {
    logger.info("Insert a dummy circuit with tiles as a transaction");
    daoManager.transaction(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        Circuit dummyCircuit = new Circuit();
        dummyCircuit.setDesigner(2);
        dummyCircuit.setName("dummy circuit 2");
        dummyCircuit.setRows(4);
        dummyCircuit.setColumns(4);
        dummyCircuit.setDirection(Direction.RIGHT);
        List<Tile> tiles = new ArrayList<Tile>();

        daoManager.getCircuitDao().insertCircuit(dummyCircuit);

        Assert.assertNotNull(dummyCircuit.getId());

        for (int i = 0; i < 4; i++) {
          Tile tile = new Tile();
          tile.setType(TileType.STRAIGHT);
          tile.setX(i);
          tile.setY(i);
          tile.setCheckpoint(0);
          tile.setCircuitid(dummyCircuit.getId());
          tiles.add(tile);
        }

        dummyCircuit.setTiles(tiles);
        Assert.assertTrue(dummyCircuit.getTiles().size() > 0);

        daoManager.getTileDaoTx().insertTiles(tiles);

        logger.info("id: " + dummyCircuit.getId());
        List<Tile> it = daoManager.getTileDao().getTilesByCircuitId(dummyCircuit.getId());

        Assert.assertTrue(it.size() > 0);

        return null;
      }
    });
  }
}
