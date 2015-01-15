package be.tiwi.vop.racing.api.model;

import java.util.ArrayList;
import java.util.List;

import be.tiwi.vop.racing.core.model.Circuit;
import be.tiwi.vop.racing.core.model.Event;
import be.tiwi.vop.racing.core.model.Ghost;
import be.tiwi.vop.racing.core.model.Obstacle;
import be.tiwi.vop.racing.core.model.Race;
import be.tiwi.vop.racing.core.model.Result;
import be.tiwi.vop.racing.core.model.Tile;
import be.tiwi.vop.racing.core.model.Tournament;
import be.tiwi.vop.racing.core.model.User;
import be.tiwi.vop.racing.domain.DaoCommand;
import be.tiwi.vop.racing.domain.DaoFactory;
import be.tiwi.vop.racing.domain.DaoManager;

public class ProjectManager {

  private static DaoManager daoManager;
  private static boolean initialized = false;

  public ProjectManager() {
    if (initialized) {
      return;
    } else {
      initialized = true;
      daoManager = DaoFactory.createMySQLDAOManager();
    }
  }

  public ArrayList<User> GetUsers() {
    return (ArrayList<User>) daoManager.getUserDao().getUsers();
  }

  public User authenticateUser(String username, String password) {
    return daoManager.getUserDao().authenticateUser(username, password);
  }

  public User getUserById(Integer id) {
    return daoManager.getUserDao().getUserById(id);
  }

  public User getUserByUsername(String username) {
    return daoManager.getUserDao().getUserByUsername(username);
  }

  public User getUserByEmail(String email) {
    return daoManager.getUserDao().getUserByEmail(email);
  }

  public void insertUser(User user) {
    daoManager.getUserDao().insertUser(user);
  }

  public void updateUser(User user) {
    daoManager.getUserDao().updateUser(user);
  }

  public ArrayList<Circuit> getCircuits() {
    return (ArrayList<Circuit>) daoManager.getCircuitDao().getCircuits();
  }

  public ArrayList<Circuit> getCircuitsByDesigner(Integer designer) {
    return (ArrayList<Circuit>) daoManager.getCircuitDao().getCircuitsByDesigner(designer);
  }

  public ArrayList<Circuit> getFavoriteCircuitsByDesigner(Integer designer) {
    return (ArrayList<Circuit>) daoManager.getCircuitDao().getFavoriteCircuitsByDesigner(designer);
  }

  public Circuit getCircuitInfoById(Integer id) {
    return daoManager.getCircuitDao().getCircuitById(id);
  }

  public void insertCircuit(final Circuit circuit) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getCircuitDao().insertCircuit(circuit);
        return null;
      }
    }, new Event(circuit.getDesigner(), "User " + circuit.getDesigner() + " created new circuit "
        + circuit.getName()));

  }

  public void updateCircuit(final int id, final Circuit circuit) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getCircuitDao().updateCircuitById(id, circuit);
        return null;
      }
    }, new Event(circuit.getDesigner(), "User " + circuit.getDesigner() + " updated new circuit "
        + circuit.getName()));
  }

  public void updateTiles(final List<Tile> tiles, int userId, String circuitName) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTileDaoTx().updateTiles(tiles);
        return null;
      }

    }, new Event(userId, "User " + userId + " updated " + tiles.size() + " tiles in circuit "
        + circuitName));
  }

  public void insertTiles(final List<Tile> tiles, int userId, String circuitName) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTileDaoTx().insertTiles(tiles);

        return null;
      }
    }, new Event(userId, "User " + userId + " inserted " + tiles.size() + " tiles in circuit "
        + circuitName));
  }

  public void deleteTiles(final List<Tile> tiles, int userId, String circuitName) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTileDaoTx().deleteTiles(tiles);

        return null;
      }
    }, new Event(userId, "User " + userId + " deleted " + tiles.size() + " tiles in circuit "
        + circuitName));
  }

  public List<Tile> getTilesByCircuitId(Integer circuitid) {
    return daoManager.getTileDao().getTilesByCircuitId(circuitid);
  }

  public ArrayList<Ghost> getGhosts() {
    return (ArrayList<Ghost>) daoManager.getGhostDao().getGhosts();
  }

  public ArrayList<Ghost> getGhostsByCircuitId(Integer circuitId) {
    return (ArrayList<Ghost>) daoManager.getGhostDao().getGhostsByCircuitId(circuitId);
  }

  public Ghost getGhostByCircuitIdAndGhostId(Integer circuitId, Integer userId) {
    return daoManager.getGhostDao().getGhostByCircuitIdAndUserId(circuitId, userId);
  }

  public Ghost getFastestGhostByCircuitId(Integer circuitId) {
    return daoManager.getGhostDao().getFastestGhostByCircuitId(circuitId);
  }

  public void insertGhost(final Ghost ghost) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getGhostDao().insertGhost(ghost);
        return null;
      }
    }, new Event(ghost.getUserId(), "User " + ghost.getUsername()
        + " set a new fastest lap on circuit " + ghost.getCircuitId() + " with a time of: "
        + timeFormat(ghost.getTime())));
  }

  public void updateGhost(final Ghost ghost) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getGhostDao().updateGhost(ghost);
        return null;
      }
    }, new Event(ghost.getUserId(), "User " + ghost.getUsername()
        + " set a new fastest laptime on circuit " + ghost.getCircuitId() + " with a time of: "
        + timeFormat(ghost.getTime())));
  }

  public ArrayList<Tournament> getAllTournaments() {
    return (ArrayList<Tournament>) daoManager.getTournamentDao().getAllTournaments();
  }

  public ArrayList<Tournament> getUpcomingTournaments(int limit1, int limit2) {
    return (ArrayList<Tournament>) daoManager.getTournamentDao().getUpcomingTournaments(limit1,
        limit2);
  }

  public Tournament getTournamentById(int id) {
    return daoManager.getTournamentDao().getTournamentById(id);
  }

  public Tournament getTournamentByName(String name) {
    return daoManager.getTournamentDao().getTournamentByName(name);
  }

  public Tournament insertTournament(final Tournament tournament) {
    return (Tournament) daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        return daoManager.getTournamentDao().insertTournament(tournament);
      }
    }, new Event(tournament.getUserId(), "User " + tournament.getUserId()
        + " created new tournament " + tournament.getName()));
  }

  public User getTournamentDesignerId(int id) {
    return daoManager.getTournamentDao().getTournamentDesignerId(id);
  }

  public ArrayList<Tournament> getTournamentsByUserId(int id) {
    return (ArrayList<Tournament>) daoManager.getTournamentDao().getTournamentsByUserId(id);
  }

  public void insertRaces(final List<Race> races, final Tournament tournament) {
    daoManager.transaction(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getRaceDaoTx().insertRaces(races);

        return null;
      }
    });
  }

  public boolean isTournamentFull(int id) {
    return daoManager.getTournamentDao().isTournamentFull(id);
  }

  public void enrollTournament(final int userId, final int tournamentId) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTournamentDao().enrollTournament(userId, tournamentId);
        return null;
      }
    }, new Event(userId, "User " + userId + " enrolled tournament " + tournamentId));

  }

  public void resignTournament(final int userId, final int tournamentId) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getTournamentDao().resignTournament(userId, tournamentId);
        return null;
      }
    }, new Event(userId, "User " + userId + " resigned tournament " + tournamentId));
  }

  public ArrayList<Tournament> getTournamentsByParticipantId(int userId) {
    return (ArrayList<Tournament>) daoManager.getTournamentDao().getTournamentsByParticipantId(
        userId);
  }

  public ArrayList<Race> getRacesByTournamentId(int tournamentId) {
    return (ArrayList<Race>) daoManager.getRaceDao().getRacesByTournamentId(tournamentId);
  }

  public ArrayList<User> getParticipantsByTournamentId(int tournamentId) {
    return (ArrayList<User>) daoManager.getTournamentDao().getParticipantsByTournamentId(
        tournamentId);
  }

  public ArrayList<Result> getResultsByRaceIdAndUserId(int raceId, int userId) {
    return (ArrayList<Result>) daoManager.getResultDao()
        .getResultsByRaceIdAndUserId(raceId, userId);
  }

  public ArrayList<Result> getResultsByRaceId(int raceId) {
    return (ArrayList<Result>) daoManager.getResultDao().getResultsByRaceId(raceId);
  }

  public void insertResult(final Result result) {
    daoManager.executeAndLog(
        new DaoCommand() {
          @Override
          public Object execute(DaoManager daoManager) {
            daoManager.getResultDao().insertResult(result);
            return null;
          }
        },
        new Event(result.getUserId(), "User " + result.getUserId() + " completed "
            + result.getRaceId() + ", lap " + result.getLapNumber() + " in "
            + timeFormat(result.getTime())));
  }

  public void insertResults(final List<Result> results) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getResultDaoTx().insertResults(results);
        return null;
      }
    }, new Event(results.get(0).getUserId(), "User " + results.get(0).getUserId() + " inserted "
        + results.size() + " new results"));
  }

  public void favoriteCircuit(final int userId, final int circuitId) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getCircuitDao().favoriteCircuit(userId, circuitId);
        return null;
      }
    }, new Event(userId, "User " + userId + " added circuit " + circuitId + " to favorites"));
  }

  public void unfavoriteCircuit(final int userId, final int circuitId) {
    daoManager.executeAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getCircuitDao().unfavoriteCircuit(userId, circuitId);
        return null;
      }
    }, new Event(userId, "User " + userId + " deleted circuit " + circuitId + " from favorites"));
  }

  public ArrayList<Circuit> getFavoriteCircuitsByUser(int userId) {
    return (ArrayList<Circuit>) daoManager.getCircuitDao().getFavoriteCircuitsByUser(userId);
  }

  public List<Obstacle> getObstaclesByCircuitId(Integer circuitid) {
    return daoManager.getObstacleDao().getObstaclesByCircuitId(circuitid);
  }

  public void updateObstacles(final List<Obstacle> obstacles, int userId, String circuitName) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getObstacleDaoTx().updateObstacles(obstacles);
        return null;
      }
    }, new Event(userId, "User " + userId + " updated " + obstacles.size()
        + " obstacles in circuit " + circuitName));
  }

  public void insertObstacles(final List<Obstacle> obstacles, int userId, String circuitName) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getObstacleDaoTx().insertObstacles(obstacles);

        return null;
      }
    }, new Event(userId, "User " + userId + " inserted " + obstacles.size()
        + " obstacles in circuit " + circuitName));
  }

  public void deleteObstacles(final List<Obstacle> obstacles, int userId, String circuitName) {
    daoManager.transactionAndLog(new DaoCommand() {
      @Override
      public Object execute(DaoManager daoManager) {
        daoManager.getObstacleDaoTx().deleteObstacles(obstacles);

        return null;
      }
    }, new Event(userId, "User " + userId + " deleted " + obstacles.size()
        + " obstacles in circuit " + circuitName));
  }

  public List<Event> getEvents(int limit1, int limit2, int userId) {
    return daoManager.getEventDao().getEventsByUserId(userId, limit1, limit2);
  }

  public static String timeFormat(final int time) {
    int minutes = time / 60000 % 60;
    int seconds = time / 1000 % 60;
    int milliseconds = time % 1000;

    return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
  }

}
