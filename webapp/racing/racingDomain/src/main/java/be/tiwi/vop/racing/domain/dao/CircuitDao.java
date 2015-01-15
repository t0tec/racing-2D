package be.tiwi.vop.racing.domain.dao;

import java.util.List;

import be.tiwi.vop.racing.core.model.Circuit;

public interface CircuitDao {
  List<Circuit> getCircuits();

  List<Circuit> getCircuitsByDesigner(int designer);

  Circuit getCircuitById(int id);

  Circuit getCircuitByName(String name);

  void insertCircuit(Circuit circuit);

  void updateCircuitById(int id, Circuit circuit);

  void favoriteCircuit(int userId, int circuitId);

  void unfavoriteCircuit(int userId, int circuitId);

  List<Circuit> getFavoriteCircuitsByDesigner(Integer designer);

  List<Circuit> getFavoriteCircuitsByUser(Integer designer);
}
