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
import be.tiwi.vop.racing.dao.TournamentDao;
import be.tiwi.vop.racing.pojo.Tournament;
import be.tiwi.vop.racing.pojo.Tournament.Formule;
import be.tiwi.vop.racing.pojo.User;

public class TournamentDaoJdbcImpl implements TournamentDao {

  private Connection connection;
  private static final Logger logger = LoggerFactory.getLogger(TournamentDaoJdbcImpl.class);

  public TournamentDaoJdbcImpl(Connection conn) {
    this.connection = conn;
  }

  @Override
  public List<Tournament> getUpcomingTournaments(int limit1, int limit2) {
    List<Tournament> tournaments = new ArrayList<Tournament>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM tournaments WHERE date >= now() ORDER BY date ASC LIMIT ?, ?");
      ps.setInt(1, limit1);
      ps.setInt(2, limit2);
      rs = ps.executeQuery();

      while (rs.next()) {
        Tournament tournament = mapToTournament(rs);
        tournaments.add(tournament);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournaments;
  }

  @Override
  public List<Tournament> getAllTournaments() {
    List<Tournament> tournaments = new ArrayList<Tournament>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM tournaments WHERE date >= now() ORDER BY date ASC");

      rs = ps.executeQuery();

      while (rs.next()) {
        Tournament tournament = mapToTournament(rs);
        tournaments.add(tournament);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournaments;
  }

  @Override
  public Tournament getTournamentById(int id) {
    Tournament tournament = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM tournaments WHERE id = ?");
      ps.setInt(1, id);

      rs = ps.executeQuery();

      while (rs.next()) {
        tournament = mapToTournament(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournament;
  }

  @Override
  public Tournament getTournamentByName(String name) {
    Tournament tournament = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM tournaments WHERE name = ?");
      ps.setString(1, name);

      rs = ps.executeQuery();

      while (rs.next()) {
        tournament = mapToTournament(rs);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournament;
  }

  @Override
  public Tournament insertTournament(Tournament t) {
    ResultSet rs = null;
    Integer id = null;
    PreparedStatement ps = null;
    Tournament tournament = new Tournament();
    try {
      ps =
          this.connection
              .prepareStatement(
                  "INSERT INTO tournaments (name, formule, user_id, max_players, date) VALUES (?, ?, ?, ?, ?)",
                  PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setString(1, t.getName());
      ps.setString(2, t.getFormule().name());
      ps.setInt(3, t.getUserId());
      ps.setInt(4, t.getMaxPlayers());
      ps.setTimestamp(5, new java.sql.Timestamp(t.getDate().getTime()));
      ps.executeUpdate();

      rs = ps.getGeneratedKeys();

      if (rs != null && rs.next()) {
        id = (int) rs.getLong(1);
        logger.info("Auto generated key: " + id);
      }
      t.setId(id);
      tournament = new Tournament(t);
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournament;
  }

  @Override
  public void updateTournament(Tournament t) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public static Tournament mapToTournament(ResultSet rs) {
    Tournament tournament = new Tournament();
    try {
      tournament.setId(rs.getInt("id"));
      tournament.setName(rs.getString("name"));
      tournament.setDate(rs.getTimestamp("date"));
      tournament.setFormule(Formule.valueOf(rs.getString("formule")));
      tournament.setMaxPlayers(rs.getInt("max_players"));
      tournament.setUserId(rs.getInt("user_id"));
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    }

    return tournament;
  }

  @Override
  public List<Tournament> getTournamentsByUserId(int id) {
    ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.connection.prepareStatement("SELECT * FROM tournaments WHERE user_id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();

      while (rs.next()) {
        Tournament tournament = mapToTournament(rs);
        tournaments.add(tournament);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournaments;
  }

  @Override
  public List<Tournament> getTournamentsByParticipantId(int id) {
    ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM tournaments t INNER JOIN participants p ON t.id = p.tournament_id WHERE p.user_id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();

      while (rs.next()) {
        Tournament tournament = mapToTournament(rs);
        tournaments.add(tournament);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return tournaments;
  }

  @Override
  public List<User> getParticipantsByTournamentId(int id) {
    ArrayList<User> participants = new ArrayList<User>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM users u INNER JOIN participants p ON u.id = p.user_id WHERE p.tournament_id = ?");
      ps.setInt(1, id);
      rs = ps.executeQuery();

      while (rs.next()) {
        User u = UserDaoJdbcImpl.mapToUser(rs);
        participants.add(u);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return participants;
  }

  @Override
  public User getTournamentDesignerId(int id) {
    User user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT * FROM users u INNER JOIN tournaments t ON u.id = t.user_id WHERE t.id = ?");
      ps.setInt(1, id);

      rs = ps.executeQuery();

      while (rs.next()) {
        user = UserDaoJdbcImpl.mapToUser(rs);
      }
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }

    return user;
  }

  @Override
  public boolean isTournamentFull(int tournamentId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    int numberParticipants = 0, maxPlayers = 0;
    try {
      ps =
          this.connection
              .prepareStatement("SELECT COUNT(1) FROM participants WHERE tournament_id = ?");
      ps.setInt(1, tournamentId);
      rs = ps.executeQuery();

      if (rs.next()) {
        numberParticipants = rs.getInt(1);
      }
      DaoUtility.close(ps, rs);

      ps = this.connection.prepareStatement("SELECT max_players FROM tournaments WHERE id = ?");
      ps.setInt(1, tournamentId);
      rs = ps.executeQuery();

      while (rs.next()) {
        maxPlayers = rs.getInt("max_players");
      }
      if (numberParticipants < maxPlayers) {
        return false;
      } else {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps, rs);
    }
    return false;
  }

  @Override
  public void enrollTournament(int userId, int tournamentId) {
    ResultSet rs = null;
    PreparedStatement ps = null;
    Integer id = null;
    try {
      ps =
          this.connection.prepareStatement(
              "INSERT INTO participants (tournament_id, user_id) VALUES (?, ?)",
              PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setInt(1, tournamentId);
      ps.setInt(2, userId);
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
  public void resignTournament(int userId, int tournamentId) {
    PreparedStatement ps = null;
    try {
      ps =
          this.connection
              .prepareStatement("DELETE FROM participants WHERE user_id = ? AND tournament_id = ?");
      ps.setInt(1, userId);
      ps.setInt(2, tournamentId);
      ps.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DaoUtility.close(ps);
    }
  }

}
