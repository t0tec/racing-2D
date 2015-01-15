package be.tiwi.vop.racing.dao;

import java.util.List;

import be.tiwi.vop.racing.core.model.Tile;

public interface TileDao {
  List<Tile> getTilesByCircuitId(Integer circuitid);

  void insertTiles(List<Tile> tiles);

  void updateTiles(List<Tile> tiles);

  void mergeTiles(List<Tile> tiles);

  void deleteTiles(List<Tile> tiles);
}
