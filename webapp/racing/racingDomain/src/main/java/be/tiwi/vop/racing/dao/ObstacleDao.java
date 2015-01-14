package be.tiwi.vop.racing.dao;

import be.tiwi.vop.racing.model.Obstacle;
import java.util.List;

public interface ObstacleDao {
  List<Obstacle> getObstaclesByCircuitId(Integer circuitId);

  void insertObstacles(List<Obstacle> obstacles);

  void updateObstacles(List<Obstacle> obstacles);

  void deleteObstacles(List<Obstacle> obstacles);
}
