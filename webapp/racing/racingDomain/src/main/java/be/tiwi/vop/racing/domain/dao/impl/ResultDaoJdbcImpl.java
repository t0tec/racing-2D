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
import be.tiwi.vop.racing.domain.dao.ResultDao;
import be.tiwi.vop.racing.core.model.Result;

public class ResultDaoJdbcImpl implements ResultDao {
  private static final Logger logger = LoggerFactory.getLogger(ResultDaoJdbcImpl.class);

  private Connection connection;

  public ResultDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Result> getResultsByRaceIdAndUserId(int raceId, int userId) {
    logger.info("Getting results by race id and user id");
    ArrayList<Result> results = new ArrayList<Result>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM results WHERE race_id = ? and user_id = ?");
      ps.setInt(1, raceId);
      ps.setInt(2, userId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Result result = mapToResult(rs);
        results.add(result);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return results;
  }

  @Override
  public List<Result> getResultsByRaceId(int raceId) {
    logger.info("Getting results by race id");
    ArrayList<Result> results = new ArrayList<Result>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("SELECT * FROM results WHERE race_id = ?");
      ps.setInt(1, raceId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Result result = mapToResult(rs);
        results.add(result);
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return results;
  }

  @Override
  public void insertResult(Result result) {
    logger.info("Inserting a new result");
    PreparedStatement ps = null;
    try {
      ps =
          this.connection
              .prepareStatement("INSERT INTO results (time, lap_number, race_id, user_id) VALUES (?, ?, ?, ?)");
      ps.setInt(1, result.getTime());
      ps.setInt(2, result.getLapNumber());
      ps.setInt(3, result.getRaceId());
      ps.setInt(4, result.getUserId());
      ps.executeUpdate();
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

  @Override
  public void insertResults(List<Result> results) {
    logger.info("Inserting results");

    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      this.connection.setAutoCommit(false);
      ps =
          this.connection
              .prepareStatement("INSERT INTO results (time, lap_number, race_id, user_id) VALUES (?, ?, ?, ?)");
      for (Result result : results) {
        ps.setInt(1, result.getTime());
        ps.setInt(2, result.getLapNumber());
        ps.setInt(3, result.getRaceId());
        ps.setInt(4, result.getUserId());
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
      DaoUtility.close(ps, rs);
    }

  }

  private Result mapToResult(ResultSet rs) {
    Result result = new Result();
    try {
      result.setId(rs.getInt("id"));
      result.setTime(rs.getInt("time"));
      result.setLapNumber(rs.getInt("lap_number"));
      result.setRaceId(rs.getInt("race_id"));
      result.setUserId(rs.getInt("user_id"));
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    }

    return result;
  }

}
