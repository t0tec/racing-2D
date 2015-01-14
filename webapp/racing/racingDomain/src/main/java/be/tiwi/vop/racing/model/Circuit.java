package be.tiwi.vop.racing.model;

import java.util.List;

public class Circuit {
  private int id;
  private String name;
  private int designer;
  private Direction direction;
  private List<Tile> tiles;
  private int columns;
  private int rows;

  public Circuit() {}

  public Circuit(int columns, int designer, Direction direction, String name, int rows,
                 List<Tile> tiles) {
    this.columns = columns;
    this.designer = designer;
    this.direction = direction;
    this.name = name;
    this.rows = rows;
    this.tiles = tiles;
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

  public List<Tile> getTiles() {
    return tiles;
  }

  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
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

  public enum Direction {
    UP, DOWN, RIGHT, LEFT
  }

  @Override
  public String toString() {
    return name;
  }

}
