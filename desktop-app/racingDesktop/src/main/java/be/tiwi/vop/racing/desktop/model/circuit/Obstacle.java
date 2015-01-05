package be.tiwi.vop.racing.desktop.model.circuit;

import java.awt.Point;

public class Obstacle {

  private Integer id, tileId;
  private String place;
  private ObstacleType obstacleType;
  private Point position;
  private boolean isCollected;

  private static final int OBSTACLESIZE = 32;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getTileId() {
    return tileId;
  }

  public void setTileId(Integer tileId) {
    this.tileId = tileId;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public ObstacleType getObstacleType() {
    return obstacleType;
  }

  public void setObstacleType(ObstacleType type) {
    this.obstacleType = type;
  }

  public Point getPosition() {
    return position;
  }

  public void setPosition(Point position) {
    this.position = position;
  }

  public boolean isCollected() {
    return isCollected;
  }

  public void setCollected(boolean isCollected) {
    this.isCollected = isCollected;
  }

  public Point getCenterPosition() {
    return new Point((this.position.x + 100 / 2), (this.position.y + 100 / 2));
  }

  public static int getObstaclesize() {
    return OBSTACLESIZE;
  }

}
