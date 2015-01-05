package be.tiwi.vop.racing.desktop.model.ghost;

import java.awt.Point;

public class Pose {
  private Point position;
  private double orientation;

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
}
