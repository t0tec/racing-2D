package be.tiwi.vop.racing.pojo;

public class Tile {

  private Integer id, circuitid, x, y, checkpoint;

  private TileType type;

  public Tile() {
    type = TileType.EARTH;
  }

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Tile(int x, int y, int circuit) {
    this.x = x;
    this.y = y;
    this.circuitid = circuit;
    this.type = TileType.EARTH;
  }

  public Tile(int x, int y, TileType t) {
    this.x = x;
    this.y = y;
    this.type = t;
  }

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

  public TileType getType() {
    return type;
  }

  public void setType(TileType type) {
    this.type = type;
  }

  public Integer getCheckpoint() {
    return checkpoint;
  }

  public void setCheckpoint(Integer check) {
    this.checkpoint = check;
  }

}
