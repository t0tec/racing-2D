package be.tiwi.vop.racing.desktop.model.ghost;

import java.awt.Point;
import java.util.LinkedHashMap;

import be.tiwi.vop.racing.desktop.util.Utility;

public class Ghost {
  private int id;
  private int time;
  private String username;
  private int userId;
  private int circuitId;
  private LinkedHashMap<Integer, Pose> poses;
  private Point position;
  private double orientation;

  public Ghost() {
    this.poses = new LinkedHashMap<Integer, Pose>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getCircuitId() {
    return circuitId;
  }

  public void setCircuitId(int circuitId) {
    this.circuitId = circuitId;
  }

  public LinkedHashMap<Integer, Pose> getPoses() {
    return poses;
  }

  public void setPoses(LinkedHashMap<Integer, Pose> poses) {
    this.poses = poses;
  }

  public Point getPosition() {
    return this.position;
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

  public boolean update(int time) {
    Pose pose = (Pose) this.poses.get(time);

    if (pose != null) {
      this.position = pose.getPosition();
      this.orientation = pose.getOrientation();

      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Player: " + this.username + " - lap time: " + Utility.timeFormat(this.time));

    return sb.toString();
  }

}
