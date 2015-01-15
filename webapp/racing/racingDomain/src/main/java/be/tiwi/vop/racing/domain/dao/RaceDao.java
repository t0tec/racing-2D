package be.tiwi.vop.racing.domain.dao;

import be.tiwi.vop.racing.core.model.Race;
import java.util.List;

public interface RaceDao {
  List<Race> getRacesByTournamentId(int id);

  void insertRace(Race r);

  void insertRaces(List<Race> races);
}
