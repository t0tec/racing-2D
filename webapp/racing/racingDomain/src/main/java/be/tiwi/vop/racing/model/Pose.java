package be.tiwi.vop.racing.model;

import java.awt.Point;
import java.io.Serializable;

public class Pose implements Serializable {
  private Point position;
  private double orientation;

  public Pose() {}

  public Pose(Point positon, double orientation) {
    this.position = positon;
    this.orientation = orientation;
  }

  public Point getPosition() {
    return position;
  }

  public void setPosition(Point position) {
    this.position = position;
  }

  public double getOrientation() {
    return orientation;
  }

  public void setOrientation(double orientation) {
    this.orientation = orientation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pose)) {
      return false;
    }

    Pose pose = (Pose) o;

    if (Double.compare(pose.orientation, orientation) != 0) {
      return false;
    }
    if (!position.equals(pose.position)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = position.hashCode();
    temp = Double.doubleToLongBits(orientation);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
