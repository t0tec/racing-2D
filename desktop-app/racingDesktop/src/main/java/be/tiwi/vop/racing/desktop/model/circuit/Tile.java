package be.tiwi.vop.racing.desktop.model.circuit;

public class Tile {

  private Integer id, circuitid, x, y, checkpoint;
  private TileType type;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getCircuitid() {
    return circuitid;
  }

  public void setCircuitid(Integer circuitid) {
    this.circuitid = circuitid;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public Integer getCheckpoint() {
    return checkpoint;
  }

  public void setCheckpoint(Integer checkpoint) {
    this.checkpoint = checkpoint;
  }

  public TileType getType() {
    return type;
  }

  public void setType(TileType type) {
    this.type = type;
  }

}
