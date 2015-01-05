package be.tiwi.vop.racing.desktop.model.tournament;

public class Result {
  private int id, time, lapNumber, raceId, userId;

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
