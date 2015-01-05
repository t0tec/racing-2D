package be.tiwi.vop.racing.desktop.model.circuit;

public enum ObstacleType {
  EGGPLANT("EGGPLANT"), MELON("MELON"), STRAWBERRY("STRAWBERRY"), PADDO("PADDO");

  public static ObstacleType getByName(String toString) {
    for (ObstacleType obsType : values()) {
      if (obsType.equalsName(toString)) {
        return obsType;
      }
    }
    return null;
  }

  private final String name;

  private ObstacleType(String s) {
    name = s.replaceAll("^\"|\"$", "");
  }

  public boolean equalsName(String otherName) {
    return (otherName == null) ? false : name.equals(otherName);
  }

  public String toString() {
    return name;
  }

}
