package be.tiwi.vop.racing.pojo;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Ghost {
  private int id;
  private int time;
  private String username;
  private int userId;
  private int circuitId;
  private LinkedHashMap<Integer, Pose> poses;

  public Ghost() {
    this.poses = new LinkedHashMap<Integer, Pose>();
  }

  public int getId() {
    return id;
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

  public void setId(int id) {
    this.id = id;
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

  public HashMap<Integer, Pose> getPoses() {
    return poses;
  }

  public void setPoses(LinkedHashMap<Integer, Pose> poses) {
    this.poses = poses;
  }

}
