package be.tiwi.vop.racing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.DaoUtility;
import be.tiwi.vop.racing.dao.ObstacleDao;
import be.tiwi.vop.racing.core.model.Obstacle;
import be.tiwi.vop.racing.core.model.ObstacleType;

public class ObstacleDaoJdbcImpl implements ObstacleDao {

  private static final Logger logger = LoggerFactory.getLogger(ObstacleDaoJdbcImpl.class);

  private Connection connection;

  public ObstacleDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Obstacle> getObstaclesByCircuitId(Integer circuitId) {
    logger.info("Getting obstacles by circuit id");
    List<Obstacle> obstacles = new ArrayList<Obstacle>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM obstacles WHERE tile_id in (SELECT id FROM tiles WHERE circuit_id = ?)");
      ps.setInt(1, circuitId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Obstacle obst = mapToObstacle(rs);
        obstacles.add(obst);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return obstacles;
  }

  @Override
  public void insertObstacles(List<Obstacle> obstacles) {
    logger.info("Inserting obstacles");
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      this.connection.setAutoCommit(false);
      ps =
          this.connection.prepareStatement(
              "INSERT INTO obstacles (obstacle_type, place, tile_id) VALUES (?, ?, ?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
      for (Obstacle obst : obstacles) {
        ps.setString(1, obst.getObstacleType().toString());
        ps.setString(2, obst.getPlace());
        ps.setInt(3, obst.getTileId());
        ps.addBatch();
      }
      ps.executeBatch();

      rs = ps.getGeneratedKeys();

      if (rs != null) {
        for (Obstacle obs : obstacles) {
          if (rs.next()) {
            obs.setId((int) rs.getLong(1));
            logger.info("id: " + obs.getId());
          }
        }
      }

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
      DaoUtility.close(ps, rs);
    }
  }

  @Override
  public void updateObstacles(List<Obstacle> obstacles) {
    logger.info("Updating osbstacles");
    PreparedStatement ps = null;
    String sql =
        "INSERT INTO obstacles (id, place, obstacle_type, tile_id) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE place = VALUES(place), obstacle_type = VALUES(obstacle_type)";
    try {
      // insert updated tiles
      this.connection.setAutoCommit(false);
      ps = this.connection.prepareStatement(sql);
      for (Obstacle obst : obstacles) {
        ps.setInt(1, obst.getId());
        ps.setString(2, obst.getPlace());
        ps.setString(3, obst.getObstacleType().toString());
        ps.setInt(4, obst.getTileId());
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
  public void deleteObstacles(List<Obstacle> obstacles) {
    logger.info("Deleting obstacles");
    PreparedStatement ps = null;
    try {
      this.connection.setAutoCommit(false);
      ps = this.connection.prepareStatement("DELETE FROM obstacles WHERE id = ?");
      for (Obstacle obst : obstacles) {
        ps.setInt(1, obst.getId());
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

  private Obstacle mapToObstacle(ResultSet rs) {
    Obstacle obst = new Obstacle();
    try {
      obst.setId(rs.getInt("id"));
      obst.setObstacleType(ObstacleType.valueOf(rs.getString("obstacle_type")));
      obst.setPlace(rs.getString("place"));
      obst.setTileId(rs.getInt("tile_id"));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return obst;
  }

}
