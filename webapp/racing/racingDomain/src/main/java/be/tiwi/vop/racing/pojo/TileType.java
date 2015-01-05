package be.tiwi.vop.racing.pojo;

public enum TileType {
  EARTH(1), START(2), STRAIGHT(3), STRAIGHT_UP(4), L_TURN(5), L_TURN_90(6), L_TURN_180(7), L_TURN_270(
      8), CROSS(9);

  private int id;

  TileType(int id) {
    this.id = id;
  }

  public static TileType getTileTypeById(int id) {
    for (TileType tt : values()) {
      if (tt.id == id) {
        return tt;
      }
    }
    return null;
  }

  public int getId() {
    return id;
  }
}
