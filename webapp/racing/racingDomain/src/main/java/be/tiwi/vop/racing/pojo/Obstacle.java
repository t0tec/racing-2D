package be.tiwi.vop.racing.pojo;

public class Obstacle {

  private Integer id, tileId;
  private String place;
  private ObstacleType obstacleType;

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

}
