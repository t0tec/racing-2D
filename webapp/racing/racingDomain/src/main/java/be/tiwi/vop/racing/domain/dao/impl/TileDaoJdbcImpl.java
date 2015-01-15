package be.tiwi.vop.racing.domain.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.domain.DaoUtility;
import be.tiwi.vop.racing.domain.dao.TileDao;
import be.tiwi.vop.racing.core.model.Tile;
import be.tiwi.vop.racing.core.model.TileType;

public class TileDaoJdbcImpl implements TileDao {

  private static final Logger logger = LoggerFactory.getLogger(TileDaoJdbcImpl.class);

  private Connection connection;

  public TileDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Tile> getTilesByCircuitId(Integer circuitId) {
    logger.info("Getting tiles by circuit id");
    List<Tile> tiles = new ArrayList<Tile>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM tiles WHERE circuit_id = ?");
      ps.setInt(1, circuitId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Tile tile = mapToTile(rs, circuitId);
        tiles.add(tile);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tiles;
  }

  @Override
  public void deleteTiles(List<Tile> tiles) {
    logger.info("Deleting tiles");
    PreparedStatement ps = null;
    try {
      this.connection.setAutoCommit(false);
      ps = this.connection.prepareStatement("DELETE FROM tiles WHERE id = ?");
      for (Tile tile : tiles) {
        ps.setInt(1, tile.getId());
        ps.addBatch();
      }
      ps.executeBatch();
      this.connection.commit();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
      try {
        this.connection.rollback();
      } catch (SQLException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    } finally {
      DaoUtility.close(ps);
    }
  }

  @Override
  public void insertTiles(List<Tile> tiles) {
    logger.info("Inserting tiles");
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection.prepareStatement(
              "INSERT INTO tiles (tile, x, y, checkpoint, circuit_id) VALUES (?, ?, ?, ?, ?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
      for (Tile tile : tiles) {
        ps.setInt(1, tile.getType().getId());
        ps.setInt(2, tile.getX());
        ps.setInt(3, tile.getY());
        ps.setInt(4, tile.getCheckpoint());
        ps.setInt(5, tile.getCircuitid());
        ps.addBatch();
      }
      ps.executeBatch();

      rs = ps.getGeneratedKeys();

      if (rs != null) {
        for (Tile tile : tiles) {
          if (rs.next()) {
            tile.setId((int) rs.getLong(1));
            logger.info("id: " + tile.getId());
          }
        }
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }
  }

  @Override
  public void updateTiles(List<Tile> tiles) {
    logger.info("Updating tiles");
    PreparedStatement ps = null;
    String sql =
        "INSERT INTO tiles (id, tile, x, y, checkpoint, circuit_id) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE tile = VALUES(tile), checkpoint = VALUES(checkpoint)";
    try {
      // insert updated tiles
      this.connection.setAutoCommit(false);
      ps = this.connection.prepareStatement(sql);
      for (Tile tile : tiles) {
        ps.setInt(1, tile.getId());
        ps.setInt(2, tile.getType().getId());
        ps.setInt(3, tile.getX());
        ps.setInt(4, tile.getY());
        ps.setInt(5, tile.getCheckpoint());
        ps.setInt(6, tile.getCircuitid());
        ps.addBatch();
      }
      ps.executeBatch();
      this.connection.commit();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
      try {
        this.connection.rollback();
      } catch (SQLException e) {
        e.printStackTrace();
        logger.error(e.getMessage());
      }
    } finally {
      DaoUtility.close(ps);
    }
  }

  @Override
  public void mergeTiles(List<Tile> tiles) {
    logger.info("Merging tiles (HSQLDB ONLY!!!)");
    PreparedStatement ps = null;
    String sql =
        "MERGE INTO tiles USING (VALUES(?, ?, ?, ?, ?, ?)) "
            + "AS vals(id, tile, x, y, checkpoint, circuit_id) ON tiles.id = vals.id "
            + "WHEN MATCHED THEN UPDATE SET tiles.tile = vals.tile "
            + "WHEN NOT MATCHED THEN INSERT VALUES vals.id, vals.tile, vals.x, vals.y, vals.checkpoint, vals.circuit_id";
    try {
      ps = this.connection.prepareStatement(sql);
      for (Tile tile : tiles) {
        ps.setInt(1, tile.getId());
        ps.setInt(2, tile.getType().getId());
        ps.setInt(3, tile.getX());
        ps.setInt(4, tile.getY());
        ps.setInt(5, tile.getCheckpoint());
        ps.setInt(6, tile.getCircuitid());
        ps.addBatch();
      }
      ps.executeBatch();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

  private Tile mapToTile(ResultSet rs, int circuitId) {
    Tile tile = new Tile();
    try {
      tile.setId(rs.getInt("id"));
      tile.setX(rs.getInt("x"));
      tile.setY(rs.getInt("y"));
      tile.setCircuitid(circuitId);
      tile.setType(TileType.getTileTypeById(rs.getInt("tile")));
      tile.setCheckpoint(rs.getInt("checkpoint"));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return tile;
  }

}
