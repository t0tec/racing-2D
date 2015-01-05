package be.tiwi.vop.racing.desktop.model.circuit;

import java.awt.Point;
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

  public Circuit() {
    this.tiles = new ArrayList<Tile>();
    this.obstacles = new ArrayList<Obstacle>();
  }

  public Circuit(List<Tile> tiles, List<Obstacle> obstacles) {
    this.tiles = tiles;
    this.obstacles = obstacles;
  }

  private final int TILESIZE = 100;
  private int lapCount;

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

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public List<Tile> getTiles() {
    return tiles;
  }

  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
  }

  public int getTILESIZE() {
    return TILESIZE;
  }

  public List<Obstacle> getObstacles() {
    return obstacles;
  }

  public void setObstacles(List<Obstacle> obstacles) {
    this.obstacles = obstacles;
  }

  public int getLapCount() {
    return lapCount;
  }

  public void setLapCount(int lapCount) {
    this.lapCount = lapCount;
  }

  public enum Direction {
    UP, DOWN, RIGHT, LEFT
  }

  public Tile getStartTile() {
    for (Tile t : tiles) {
      if (t.getType().equals(TileType.START)) {
        return t;
      }
    }
    return null;
  }

  public Tile getPreviousTileAfterStartTile() {
    if (this.direction == Direction.RIGHT) {
      return getTileByXandY(getStartTile().getX() - 1, getStartTile().getY());
    } else if (this.direction == Direction.LEFT) {
      return getTileByXandY(getStartTile().getX() + 1, getStartTile().getY());
    } else if (this.direction == Direction.UP) {
      return getTileByXandY(getStartTile().getX(), getStartTile().getY() - 1);
    } else if (this.direction == Direction.DOWN) {
      return getTileByXandY(getStartTile().getX() - 1, getStartTile().getY() + 1);
    }

    return getStartTile();
  }

  public Tile getTileByXandY(int x, int y) {
    for (Tile t : tiles) {
      if (t.getX() == x && t.getY() == y) {
        return t;
      }
    }
    return null;
  }

  public List<Tile> getCheckpoints() {
    List<Tile> checkpoints = new ArrayList<Tile>();
    for (Tile tile : this.tiles) {
      if (tile.getCheckpoint() != 0) {
        checkpoints.add(tile);
      }
    }
    return checkpoints;
  }

  public Obstacle getObstacleByTileId(int tileId) {
    for (Obstacle obst : this.obstacles) {
      if (obst.getTileId() == tileId) {
        return obst;
      }
    }

    return null;
  }

  public int getAmountOfRemainingObstacles() {
    int count = 0;
    for (Obstacle obst : this.obstacles) {
      if (!obst.isCollected()) {
        count++;
      }
    }

    return count;
  }

  public Point getFinishLinePoint() {
    if (this.direction == Direction.RIGHT) {
      return new Point(((this.getStartTile().getX() + 1) * this.TILESIZE), (this.getStartTile()
          .getY() * this.TILESIZE + this.TILESIZE / 2));
    } else if (this.direction == Direction.LEFT) {
      return new Point(((this.getStartTile().getX()) * this.TILESIZE), (this.getStartTile().getY()
          * this.TILESIZE + this.TILESIZE / 2));
    } else if (this.direction == Direction.UP) {
      return new Point(((this.getStartTile().getX()) * this.TILESIZE),
          ((this.getStartTile().getY()) * this.TILESIZE + this.TILESIZE / 2));
    } else {
      return new Point(((this.getStartTile().getX()) * this.TILESIZE),
          ((this.getStartTile().getY() + 1) * this.TILESIZE + this.TILESIZE / 2));
    }
  }

  @Override
  public String toString() {
    return name;
  }

}
