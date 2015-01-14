package be.tiwi.vop.racing.dao;

import java.util.List;

import be.tiwi.vop.racing.model.Ghost;

public interface GhostDao {
  List<Ghost> getGhosts();

  List<Ghost> getGhostsByCircuitId(int circuitId);

  Ghost getGhostByCircuitIdAndUserId(int userId, int circuitId);

  Ghost getFastestGhostByCircuitId(int circuitId);

  void insertGhost(Ghost ghost);

  void updateGhost(Ghost ghost);
}
