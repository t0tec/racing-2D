package be.tiwi.vop.racing.test;

import java.sql.SQLSyntaxErrorException;
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
import be.tiwi.vop.racing.factory.test.BaseSetupTest;
import be.tiwi.vop.racing.model.Tile;
import be.tiwi.vop.racing.model.TileType;

@FixMethodOrder(MethodSorters.JVM)
public class TileDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(TileDaoTest.class);

  @Test
  public void insertTiles() {
    logger.info("Inserting dummy tiles");
    final List<Tile> tiles = new ArrayList<Tile>();

    for (int i = 0; i < 4; i++) {
      Tile tile = new Tile();
      tile.setType(TileType.STRAIGHT);
      tile.setX(i);
      tile.setY(i);
      tile.setCheckpoint(0);
      tile.setCircuitid(2);
      tiles.add(tile);
    }

    daoManager.transaction(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTileDaoTx().insertTiles(tiles);

        return null;
      }
    });

    for (int i = 0; i < tiles.size(); i++) {
      Assert.assertNotNull(tiles.get(i).getId());
    }
  }

  @Test
  public void getTiles() {
    logger.info("Getting tiles");
    List<Tile> tiles = daoManager.getTileDao().getTilesByCircuitId(2);

    Assert.assertTrue(tiles.size() > 0);

    for (Tile tile : tiles) {
      Assert.assertNotNull(tile.getId());
    }
  }

  @Test
  public void mergeTiles() throws SQLSyntaxErrorException {
    logger.info("Updating existing/inserting tiles");
    final List<Tile> tiles = daoManager.getTileDao().getTilesByCircuitId(2);

    logger.info("size: " + tiles.size());
    Assert.assertTrue(tiles.size() > 0);

    for (Tile tile : tiles) {
      tile.setType(TileType.STRAIGHT_UP);
    }

    logger.info("ON DUPLICATE KEY UPDATE NOT RECOGNISED BY HSQLDB ==> USE MERGE");

    daoManager.transaction(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTileDaoTx().mergeTiles(tiles);

        return null;
      }
    });

    for (Tile tile : tiles) {
      Assert.assertNotNull(tile.getType());
      logger.info("tile type: " + tile.getType());
    }

  }
}
