package be.tiwi.vop.racing.dao;

import java.util.List;

import be.tiwi.vop.racing.pojo.Tile;

public interface TileDao {
  List<Tile> getTilesByCircuitId(Integer circuitid);

  void insertTiles(List<Tile> tiles);

  void updateTiles(List<Tile> tiles);

  void mergeTiles(List<Tile> tiles);

  void deleteTiles(List<Tile> tiles);
}
