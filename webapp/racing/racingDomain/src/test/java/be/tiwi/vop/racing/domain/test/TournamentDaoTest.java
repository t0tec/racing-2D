package be.tiwi.vop.racing.domain.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.core.model.Tournament;
import be.tiwi.vop.racing.core.model.User;
import be.tiwi.vop.racing.domain.BaseSetupTest;

public class TournamentDaoTest extends BaseSetupTest {
  private static final Logger logger = LoggerFactory.getLogger(TournamentDaoTest.class);

  @Test
  public void getAllTournaments() {
    logger.info("Getting all tournaments");

    List<Tournament> tournaments = daoManager.getTournamentDao().getAllTournaments();
    Assert.assertTrue(tournaments.size() > 0);
  }

  @Test
  public void getLimitedNumberOfTournaments() {
    int number1 = 5;
    int number2 = 9;

    List<Tournament> tournaments =
        daoManager.getTournamentDao().getUpcomingTournaments(number1, number2);
    Assert.assertTrue(tournaments.size() > 0);
    Assert.assertEquals(number2 - number1, tournaments.size());
  }

  @Test
  public void getTournamentById() {
    logger.info("Get a tournament by id");
    Tournament tournament = daoManager.getTournamentDao().getTournamentById(1);
    Assert.assertNotNull(tournament);
  }

  @Test
  public void getTournamentByName() {
    logger.info("Get a tournament by name");
    Tournament tournament = daoManager.getTournamentDao().getTournamentByName("tournament1");
    Assert.assertNotNull(tournament);
  }

  @Test
  public void insertDummyTournament() {
    logger.info("Insert a dummy tournament");
    Tournament dummyTournament = new Tournament();
    dummyTournament.setName("dummy tournament");
    dummyTournament.setUserId(2);
    dummyTournament.setMaxPlayers(4);
    dummyTournament.setFormule(Tournament.Formule.FASTEST);
    String strDate = "04/26/2014 12:00";
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
    Date date = null;
    try {
      date = sdf.parse(strDate);
    } catch (ParseException pEx) {
      logger.error("Parse Exception: " + pEx.getMessage());
    }
    dummyTournament.setDate(date);

    daoManager.getTournamentDao().insertTournament(dummyTournament);
    Assert.assertNotNull(dummyTournament.getId());
  }

  @Test
  @Ignore
  public void updateTournament() {
    logger.info("Updating a tournament");
    Tournament tournament = daoManager.getTournamentDao().getTournamentById(1);
    tournament.setMaxPlayers(10);

    daoManager.getTournamentDao().updateTournament(tournament);
    Assert.assertEquals(10, tournament.getMaxPlayers());
  }

  @Test
  public void getTournamentsByUser() {
    logger.info("Getting tournaments created by one user");
    List<Tournament> tournaments = daoManager.getTournamentDao().getTournamentsByUserId(3);
    Assert.assertTrue(tournaments.size() > 0);
  }

  @Test
  public void getTournamentsByParticipantId() {
    logger.info("Getting tournaments where a user is participating in");
    List<Tournament> tournaments = daoManager.getTournamentDao().getTournamentsByParticipantId(4);
    Assert.assertTrue(tournaments.size() > 0);
  }

  @Test
  public void getParticipantsByTournamentId() {
    logger.info("Getting all particpating users who are playing in the same tournament");
    List<User> participants = daoManager.getTournamentDao().getParticipantsByTournamentId(1);
    Assert.assertTrue(participants.size() > 0);
  }

}
