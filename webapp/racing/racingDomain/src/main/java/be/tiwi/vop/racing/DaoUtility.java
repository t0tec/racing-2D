package be.tiwi.vop.racing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DaoUtility {
  private static final Logger logger = LoggerFactory.getLogger(DaoUtility.class);

  public static PreparedStatement getPrepareStatement(Connection connection, String sql,
      boolean returnGeneratedKeys, Object... values) {
    PreparedStatement ps = null;
    try {
      ps =
          connection.prepareStatement(sql,
              returnGeneratedKeys ? PreparedStatement.RETURN_GENERATED_KEYS
                  : PreparedStatement.NO_GENERATED_KEYS);
      setValues(ps, values);
    } catch (SQLException e) {
      logger.error("Getting PreparedStatement failed: " + e.getMessage());
      e.printStackTrace();
    }

    return ps;
  }

  private static void setValues(PreparedStatement ps, Object... values) {
    for (int i = 0; i < values.length; i++) {
      try {
        ps.setObject(i + 1, values[i]);
      } catch (SQLException e) {
        logger.error("Setting values for PreparedStatement failed: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.error("Closing Connection failed: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public static void close(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        logger.error("Closing PreparedStatement failed: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public static void close(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        logger.error("Closing ResultSet failed: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public static void close(Connection connection, PreparedStatement ps) {
    close(ps);
    close(connection);
  }

  public static void close(PreparedStatement ps, ResultSet rs) {
    close(rs);
    close(ps);
  }

  public static void close(Connection connection, PreparedStatement ps, ResultSet rs) {
    close(rs);
    close(ps);
    close(connection);
  }
}
