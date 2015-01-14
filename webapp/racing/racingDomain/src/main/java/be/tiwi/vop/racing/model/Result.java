package be.tiwi.vop.racing.model;

public class Result {
  private int id, time, lapNumber, raceId, userId;

  public Result() {}

  public Result(int lapNumber, int raceId, int time, int userId) {
    this.lapNumber = lapNumber;
    this.raceId = raceId;
    this.time = time;
    this.userId = userId;
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

  public int getLapNumber() {
    return lapNumber;
  }

  public void setLapNumber(int lapNumber) {
    this.lapNumber = lapNumber;
  }

  public int getRaceId() {
    return raceId;
  }

  public void setRaceId(int raceId) {
    this.raceId = raceId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
