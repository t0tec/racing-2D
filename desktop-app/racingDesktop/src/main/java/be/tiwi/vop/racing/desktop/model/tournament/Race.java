package be.tiwi.vop.racing.desktop.model.tournament;

public class Race {
  private int id, tournamentId, circuitId, laps;

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

  @Override
  public String toString() {
    return this.id + " -- circuit id: " + this.circuitId + " -- laps: " + this.laps;
  }
}
