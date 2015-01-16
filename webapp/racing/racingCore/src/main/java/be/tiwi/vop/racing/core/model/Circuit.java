package be.tiwi.vop.racing.core.model;

import java.util.ArrayList;
import java.util.List;

public class Circuit {

  private int id;
  private String name;
  private int designer;
  private Direction direction;
  private int columns;
  private int rows;
  private List<Tile> tiles;
  private List<Obstacle> obstacles;

  public Circuit() {}

  public Circuit(String name, int designer, Direction direction, int rows, int columns,
                 List<Tile> tiles, List<Obstacle> obstacles) {
    this.name = name;
    this.designer = designer;
    this.direction = direction;
    this.rows = rows;
    this.columns = columns;
    this.tiles = tiles;
    this.obstacles = obstacles;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDesigner() {
    return designer;
  }

  public void setDesigner(int designer) {
    this.designer = designer;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void setRows(int number) {
    this.rows = number;
  }

  public void setColumns(int number) {
    this.columns = number;
  }

  public int getRows() {
    return this.rows;
  }

  public int getColumns() {
    return this.columns;
  }

  public List<Tile> getTiles() {
    return tiles;
  }

  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
  }

  public List<Obstacle> getObstacles() {
    return obstacles;
  }

  public void setObstacles(List<Obstacle> obstacles) {
    this.obstacles = obstacles;
  }

  public enum Direction {
    UP, DOWN, RIGHT, LEFT
  }

}
