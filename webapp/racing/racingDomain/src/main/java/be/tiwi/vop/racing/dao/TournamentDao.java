package be.tiwi.vop.racing.dao;

import java.util.List;

import be.tiwi.vop.racing.core.model.Tournament;
import be.tiwi.vop.racing.core.model.User;

public interface TournamentDao {
  List<Tournament> getUpcomingTournaments(int limit1, int limit2);

  List<Tournament> getAllTournaments();

  List<Tournament> getTournamentsByUserId(int id);

  List<Tournament> getTournamentsByParticipantId(int id);

  List<User> getParticipantsByTournamentId(int id);

  boolean isTournamentFull(int tournamentId);

  User getTournamentDesignerId(int tournamentId);

  Tournament getTournamentById(int id);

  Tournament getTournamentByName(String name);

  Tournament insertTournament(Tournament t);

  void updateTournament(Tournament t);

  void enrollTournament(int userId, int tournamentId);

  void resignTournament(int userId, int tournamentId);

}
