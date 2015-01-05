package be.tiwi.vop.racing;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import be.tiwi.vop.racing.dao.CircuitDao;
import be.tiwi.vop.racing.dao.EventDao;
import be.tiwi.vop.racing.dao.GhostDao;
import be.tiwi.vop.racing.dao.ObstacleDao;
import be.tiwi.vop.racing.dao.RaceDao;
import be.tiwi.vop.racing.dao.ResultDao;
import be.tiwi.vop.racing.dao.TileDao;
import be.tiwi.vop.racing.dao.TournamentDao;
import be.tiwi.vop.racing.dao.UserDao;
import be.tiwi.vop.racing.dao.impl.CircuitDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.EventDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.GhostDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.ObstacleDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.RaceDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.ResultDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.TileDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.TournamentDaoJdbcImpl;
import be.tiwi.vop.racing.dao.impl.UserDaoJdbcImpl;
import be.tiwi.vop.racing.pojo.Event;

public class DaoManager {

  private DataSource dataSource;
  private Connection connection;

  private UserDao userDao;
  private CircuitDao circuitDao;
  private TileDao tileDao;
  private GhostDao ghostDao;
  private TournamentDao tournamentDao;
  private RaceDao raceDao;
  private ResultDao resultDao;
  private ObstacleDao obstacleDao;
  private EventDao eventDao;

  public DaoManager(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private Connection getConnection() throws SQLException {
    if (this.connection == null || this.connection.isClosed()) {
      try {
        this.connection = this.dataSource.getConnection();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return this.connection;
  }

  private Connection getTxConnection() {
    try {
      this.connection = getConnection();
      this.connection.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return this.connection;
  }


  public Object transaction(DaoCommand command) {
    Object returnValue = null;
    try {
      returnValue = command.execute(this);
      this.getConnection().commit();
      return returnValue;
    } catch (Exception e) {
      try {
        this.getConnection().rollback();
      } catch (SQLException sqlEx) {
        sqlEx.printStackTrace();
      }
      try {
        throw e; // or wrap it before rethrowing it
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } finally {
      try {
        this.getConnection().setAutoCommit(true);
      } catch (SQLException sqlEx) {
        sqlEx.printStackTrace();
      }
    }

    return returnValue;
  }

  public Object transactionAndLog(DaoCommand command, Event event) {
    Object returnValue = null;
    try {
      returnValue = command.execute(this);
      this.getConnection().commit();
      return returnValue;
    } catch (Exception e) {
      try {
        this.getConnection().rollback();
      } catch (SQLException sqlEx) {
        sqlEx.printStackTrace();
      }
      try {
        throw e; // or wrap it before rethrowing it
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } finally {
      try {
        this.getConnection().setAutoCommit(true);
        this.getEventDao().insertEvent(event);
      } catch (SQLException sqlEx) {
        sqlEx.printStackTrace();
      }
    }
    return returnValue;
  }

  public Object executeAndClose(DaoCommand command) {
    try {
      return command.execute(this);
    } finally {
      try {
        this.getConnection().close();
      } catch (SQLException sqlEx) {
        sqlEx.printStackTrace();
      }
    }
  }

  public Object executeAndLog(DaoCommand command, Event event) {
    try {
      Object returnValue = null;
      returnValue = command.execute(this);
      this.getEventDao().insertEvent(event);
      return returnValue;
    } finally {

    }
  }

  public Object transactionAndClose(final DaoCommand command) {
    return executeAndClose(new DaoCommand() {
      public Object execute(DaoManager daoManager) {
        return daoManager.transaction(command);
      }
    });
  }

  public Object transactionAndLogAndClose(final DaoCommand command, final Event event) {
    return executeAndClose(new DaoCommand() {
      public Object execute(DaoManager daoManager) {
        return daoManager.transactionAndLog(command, event);
      }
    });
  }

  public UserDao getUserDao() {
    try {
      if (this.userDao == null) {
        this.userDao = new UserDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.userDao;
  }

  public UserDao getUserDaoTx() {
    try {
      if (this.userDao == null) {
        this.userDao = new UserDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this.userDao;
  }

  public CircuitDao getCircuitDao() {
    try {
      if (this.circuitDao == null) {
        this.circuitDao = new CircuitDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.circuitDao;
  }

  public TileDao getTileDao() {
    try {
      if (this.tileDao == null) {
        this.tileDao = new TileDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.tileDao;
  }

  public TileDao getTileDaoTx() {
    try {
      if (this.tileDao == null) {
        this.tileDao = new TileDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.tileDao;
  }

  public ObstacleDao getObstacleDao() {
    try {
      if (this.obstacleDao == null) {
        this.obstacleDao = new ObstacleDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.obstacleDao;
  }

  public ObstacleDao getObstacleDaoTx() {
    try {
      if (this.obstacleDao == null) {
        this.obstacleDao = new ObstacleDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.obstacleDao;
  }

  public GhostDao getGhostDao() {
    try {
      if (this.ghostDao == null) {
        this.ghostDao = new GhostDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.ghostDao;
  }

  public TournamentDao getTournamentDao() {
    try {
      if (this.tournamentDao == null) {
        this.tournamentDao = new TournamentDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.tournamentDao;
  }

  public TournamentDao getTournamentDaoTx() {
    try {
      if (this.tournamentDao == null) {
        this.tournamentDao = new TournamentDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.tournamentDao;
  }

  public RaceDao getRaceDao() {
    try {
      if (this.raceDao == null) {
        this.raceDao = new RaceDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.raceDao;
  }

  public RaceDao getRaceDaoTx() {
    try {
      if (this.raceDao == null) {
        this.raceDao = new RaceDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.raceDao;
  }

  public ResultDao getResultDao() {
    try {
      if (this.resultDao == null) {
        this.resultDao = new ResultDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.resultDao;
  }

  public ResultDao getResultDaoTx() {
    try {
      if (this.resultDao == null) {
        this.resultDao = new ResultDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.resultDao;
  }

  public EventDao getEventDao() {
    try {
      if (this.eventDao == null) {
        this.eventDao = new EventDaoJdbcImpl(this.getConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.eventDao;
  }

  public EventDao getEventDaoTx() {
    try {
      if (this.eventDao == null) {
        this.eventDao = new EventDaoJdbcImpl(this.getTxConnection());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return this.eventDao;
  }

}
