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
import be.tiwi.vop.racing.dao.CircuitDao;
import be.tiwi.vop.racing.pojo.Circuit;
import be.tiwi.vop.racing.pojo.Circuit.Direction;

public class CircuitDaoJdbcImpl implements CircuitDao {
  private static final Logger logger = LoggerFactory.getLogger(CircuitDaoJdbcImpl.class);

  private Connection connection;

  public CircuitDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Circuit> getCircuits() {
    List<Circuit> circuits = new ArrayList<Circuit>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM circuits");
      rs = ps.executeQuery();
      while (rs.next()) {
        Circuit circuit = mapToCircuit(rs);
        circuits.add(circuit);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return circuits;
  }

  @Override
  public List<Circuit> getCircuitsByDesigner(int designer) {
    List<Circuit> circuits = new ArrayList<Circuit>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM circuits WHERE designer = ?");
      ps.setInt(1, designer);
      rs = ps.executeQuery();

      while (rs.next()) {
        Circuit circuit = mapToCircuit(rs);
        circuits.add(circuit);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return circuits;
  }

  @Override
  public Circuit getCircuitById(int id) {
    Circuit circuit = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM circuits WHERE id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();

      while (rs.next()) {
        circuit = mapToCircuit(rs);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return circuit;
  }

  @Override
  public Circuit getCircuitByName(String name) {
    Circuit circuit = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM circuits WHERE name = ?");
      ps.setString(1, name);
      rs = ps.executeQuery();

      while (rs.next()) {
        circuit = mapToCircuit(rs);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return circuit;
  }

  @Override
  public void insertCircuit(Circuit circuit) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    Integer id = null;
    try {
      ps =
          connection
              .prepareStatement(
                  "INSERT INTO circuits (name, designer, direction, rows, columns) VALUES (?, ?, ?, ?, ?)",
                  PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setString(1, circuit.getName());
      ps.setInt(2, circuit.getDesigner());
      ps.setString(3, circuit.getDirection().toString());
      ps.setInt(4, circuit.getRows());
      ps.setInt(5, circuit.getColumns());
      ps.executeUpdate();

      rs = ps.getGeneratedKeys();

      if (rs != null && rs.next()) {
        id = (int) rs.getLong(1);
        logger.info("Auto generated key: " + id);
      }
      circuit.setId(id);
    } catch (Exception sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

  }

  @Override
  public void updateCircuitById(int id, Circuit circuit) {
    PreparedStatement ps = null;
    try {
      ps =
          connection
              .prepareStatement("UPDATE circuits SET name = ?, designer = ?, direction = ?, rows = ?, columns = ? WHERE id = ?");
      ps.setString(1, circuit.getName());
      ps.setInt(2, circuit.getDesigner());
      ps.setString(3, circuit.getDirection().toString());
      ps.setInt(4, circuit.getRows());
      ps.setInt(5, circuit.getColumns());
      ps.setInt(6, id);
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }

  }

  @Override
  public void favoriteCircuit(int userId, int circuitId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    Integer id = null;
    try {
      ps =
          this.connection.prepareStatement(
              "INSERT INTO favorite_circuits (user_id, circuit_id) VALUES (?,?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setInt(1, userId);
      ps.setInt(2, circuitId);
      ps.executeUpdate();

      rs = ps.getGeneratedKeys();

      if (rs != null && rs.next()) {
        id = (int) rs.getLong(1);
        logger.info("Auto generated key: " + id);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

  }

  @Override
  public void unfavoriteCircuit(int userId, int circuitId) {
    PreparedStatement ps = null;
    try {
      ps =
          this.connection
              .prepareStatement("DELETE FROM favorite_circuits where user_id = ? and circuit_id = ?");
      ps.setInt(1, userId);
      ps.setInt(2, circuitId);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }

  }

  @Override
  public List<Circuit> getFavoriteCircuitsByDesigner(Integer designer) {
    List<Circuit> circuits = new ArrayList<Circuit>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM favorite_circuits WHERE designer_id = ?");
      ps.setInt(1, designer);
      rs = ps.executeQuery();

      while (rs.next()) {
        Circuit circuit = mapToCircuit(rs);
        circuits.add(circuit);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return circuits;
  }

  @Override
  public List<Circuit> getFavoriteCircuitsByUser(Integer designer) {
    List<Circuit> circuits = new ArrayList<Circuit>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          connection
              .prepareStatement("SELECT * FROM circuits INNER JOIN favorite_circuits ON circuit_id = circuits.id WHERE user_id = ?");
      ps.setInt(1, designer);
      rs = ps.executeQuery();

      while (rs.next()) {
        Circuit circuit = mapToCircuit(rs);
        circuits.add(circuit);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return circuits;
  }

  private Circuit mapToCircuit(ResultSet rs) {
    Circuit circuit = new Circuit();
    try {
      circuit.setId(rs.getInt("id"));
      circuit.setName(rs.getString("name"));
      circuit.setDesigner(rs.getInt("designer"));
      circuit.setRows(rs.getInt("rows"));
      circuit.setColumns(rs.getInt("columns"));
      circuit.setDirection(Direction.valueOf(rs.getString("direction")));
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    }

    return circuit;
  }
}
