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
import be.tiwi.vop.racing.domain.dao.EventDao;
import be.tiwi.vop.racing.core.model.Event;

public class EventDaoJdbcImpl implements EventDao {

  private static final Logger logger = LoggerFactory.getLogger(EventDaoJdbcImpl.class);

  private Connection connection;

  public EventDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Event> getEventsByUserId(int userId, int limit1, int limit2) {
    logger.info("Getting events by user id");
    List<Event> events = new ArrayList<Event>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      this.connection.setAutoCommit(true);
      ps =
          DaoUtility.getPrepareStatement(this.connection,
              "SELECT * FROM events WHERE user_id = ? ORDER BY timestamp DESC LIMIT ?, ?", false,
              userId, limit1, limit2);
      rs = ps.executeQuery();
      while (rs.next()) {
        Event event = mapToEvent(rs);
        events.add(event);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return events;
  }

  @Override
  public void insertEvent(Event e) {
    logger.info("Insert new event: " + e.getAction());
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      ps =
          DaoUtility.getPrepareStatement(this.connection,
              "INSERT INTO events (action, timestamp, user_id) VALUES (?, ?, ?)", false,
              e.getAction(), new java.sql.Timestamp(e.getTimestamp().getTime()), e.getUserId());
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }
  }

  private Event mapToEvent(ResultSet rs) {
    Event event = new Event();
    try {
      event.setId(rs.getInt("id"));
      event.setAction(rs.getString("action"));
      event.setTimestamp(rs.getTimestamp("timestamp"));
      event.setUserId(rs.getInt("user_id"));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return event;
  }

}
