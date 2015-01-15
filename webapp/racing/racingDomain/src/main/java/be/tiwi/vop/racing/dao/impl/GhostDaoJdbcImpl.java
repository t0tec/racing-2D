package be.tiwi.vop.racing.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.DaoUtility;
import be.tiwi.vop.racing.dao.GhostDao;
import be.tiwi.vop.racing.core.model.Ghost;
import be.tiwi.vop.racing.core.model.Pose;


public class GhostDaoJdbcImpl implements GhostDao {
  private static final Logger logger = LoggerFactory.getLogger(GhostDaoJdbcImpl.class);

  private Connection connection;

  public GhostDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Ghost> getGhosts() {
    logger.info("Getting all ghosts");
    List<Ghost> ghosts = new ArrayList<Ghost>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          connection
              .prepareStatement("SELECT users.username, ghosts.id, ghosts.time, ghosts.user_id, ghosts.circuit_id, ghosts.poses FROM ghosts inner join users on ghosts.user_id = users.id");
      rs = ps.executeQuery();
      while (rs.next()) {
        Ghost ghost = mapToGhost(rs);
        ghosts.add(ghost);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return ghosts;
  }


  @Override
  public List<Ghost> getGhostsByCircuitId(int circuitId) {
    logger.info("Getting ghosts by circuit id");
    List<Ghost> ghosts = new ArrayList<Ghost>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          connection
              .prepareStatement("SELECT users.username, ghosts.id, ghosts.time, ghosts.user_id, ghosts.circuit_id, ghosts.poses from ghosts inner join users on ghosts.user_id = users.id WHERE circuit_id = ?");
      ps.setInt(1, circuitId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Ghost ghost = mapToGhost(rs);
        ghosts.add(ghost);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return ghosts;
  }

  @Override
  public Ghost getGhostByCircuitIdAndUserId(int circuitId, int userId) {
    logger.info("Getting a ghost by circuit id and user id");
    Ghost ghost = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          connection
              .prepareStatement("SELECT users.username, ghosts.id, ghosts.time, ghosts.user_id, ghosts.circuit_id, ghosts.poses FROM ghosts inner join users on ghosts.user_id = users.id WHERE user_id = ? and circuit_id = ?");
      ps.setInt(1, userId);
      ps.setInt(2, circuitId);
      rs = ps.executeQuery();

      while (rs.next()) {
        ghost = this.mapToGhost(rs);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return ghost;
  }

  @Override
  public Ghost getFastestGhostByCircuitId(int circuitId) {
    logger.info("Getting the fastest ghost by circuit id");
    Ghost ghost = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          connection
              .prepareStatement("SELECT users.username, g.id, g.time, g.user_id, g.circuit_id, g.poses FROM ghosts as g inner join users on g.user_id = users.id WHERE g.time = (SELECT MIN(ghosts.time) FROM ghosts WHERE circuit_id = ?)");
      ps.setInt(1, circuitId);
      rs = ps.executeQuery();

      while (rs.next()) {
        ghost = this.mapToGhost(rs);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return ghost;
  }

  @Override
  public void insertGhost(Ghost ghost) {
    logger.info("Inserting a new ghost");
    PreparedStatement ps = null;
    try {
      // Serialize LinkedHashMap
      ByteArrayOutputStream bArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objOutputStream = new ObjectOutputStream(bArrayOutputStream);
      objOutputStream.writeObject(ghost.getPoses());

      ps =
          this.connection
              .prepareStatement("INSERT INTO ghosts (time, user_id, circuit_id, poses) VALUES (?, ?, ?, ?)");
      ps.setInt(1, ghost.getTime());
      ps.setInt(2, ghost.getUserId());
      ps.setInt(3, ghost.getCircuitId());
      ps.setBinaryStream(4, new ByteArrayInputStream(bArrayOutputStream.toByteArray()));
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

  @Override
  public void updateGhost(Ghost ghost) {
    logger.info("Updating an existing ghost");
    PreparedStatement ps = null;
    try {
      // Serialize LinkedHashMap
      ByteArrayOutputStream bArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objOutputStream = new ObjectOutputStream(bArrayOutputStream);
      objOutputStream.writeObject(ghost.getPoses());

      ps = connection.prepareStatement("UPDATE ghosts SET time = ?, poses = ? WHERE id = ?");
      ps.setInt(1, ghost.getTime());
      ps.setBinaryStream(2, new ByteArrayInputStream(bArrayOutputStream.toByteArray()));
      ps.setInt(3, ghost.getId());
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

  @SuppressWarnings("unchecked")
  private Ghost mapToGhost(ResultSet rs) {
    Ghost ghost = new Ghost();
    try {
      ghost.setId(rs.getInt("id"));
      ghost.setTime(rs.getInt("time"));
      ghost.setUsername(rs.getString("username"));
      ghost.setUserId(rs.getInt("user_id"));
      ghost.setCircuitId(rs.getInt("circuit_id"));
      // Deserialize LinkedHashMap
      ByteArrayInputStream bArrayInputStream = new ByteArrayInputStream(rs.getBytes("poses"));
      ObjectInputStream objInputStream = new ObjectInputStream(bArrayInputStream);
      LinkedHashMap<Integer, Pose> poses =
          (LinkedHashMap<Integer, Pose>) objInputStream.readObject();

      ghost.setPoses(poses);

    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    } catch (ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }

    return ghost;
  }
}
