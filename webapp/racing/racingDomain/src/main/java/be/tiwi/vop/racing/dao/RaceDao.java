package be.tiwi.vop.racing.dao;

import be.tiwi.vop.racing.model.Race;
import java.util.List;

public interface RaceDao {
  List<Race> getRacesByTournamentId(int id);

  void insertRace(Race r);

  void insertRaces(List<Race> races);
}
