package be.tiwi.vop.racing.model;

public class Race {

  private int id, tournamentId, circuitId, laps;

  public Race() {}

  public Race(int circuitId, int laps, int tournamentId) {
    this.circuitId = circuitId;
    this.laps = laps;
    this.tournamentId = tournamentId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(int tournamentId) {
    this.tournamentId = tournamentId;
  }

  public int getCircuitId() {
    return circuitId;
  }

  public void setCircuitId(int circuitId) {
    this.circuitId = circuitId;
  }

  public int getLaps() {
    return laps;
  }

  public void setLaps(int laps) {
    this.laps = laps;
  }

}
