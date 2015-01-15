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
import be.tiwi.vop.racing.domain.dao.RaceDao;
import be.tiwi.vop.racing.core.model.Race;

public class RaceDaoJdbcImpl implements RaceDao {
  private static final Logger logger = LoggerFactory.getLogger(TileDaoJdbcImpl.class);

  private Connection connection;

  public RaceDaoJdbcImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<Race> getRacesByTournamentId(int id) {
    logger.info("Getting races by tournament id");
    List<Race> races = new ArrayList<Race>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM races WHERE tournament_id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();
      while (rs.next()) {
        Race race = mapToRace(rs);
        races.add(race);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return races;
  }

  @Override
  public void insertRace(Race r) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void insertRaces(List<Race> races) {
    logger.info("Inserting races");
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      this.connection.setAutoCommit(false);
      ps =
          this.connection.prepareStatement(
              "INSERT INTO races (tournament_id, circuit_id, laps) VALUES (?, ?, ?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
      for (Race race : races) {
        ps.setInt(1, race.getTournamentId());
        ps.setInt(2, race.getCircuitId());
        ps.setInt(3, race.getLaps());
        ps.addBatch();
      }
      ps.executeBatch();

      rs = ps.getGeneratedKeys();

      if (rs != null) {
        for (Race race : races) {
          if (rs.next()) {
            race.setId((int) rs.getLong(1));
            logger.info("id: " + race.getId());
          }
        }
      }
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }
  }


  private Race mapToRace(ResultSet rs) {
    Race race = new Race();
    try {
      race.setId(rs.getInt("id"));
      race.setCircuitId(rs.getInt("circuit_id"));
      race.setTournamentId(rs.getInt("tournament_id"));
      race.setLaps(rs.getInt("laps"));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return race;
  }
}
