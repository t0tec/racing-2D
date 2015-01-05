package be.tiwi.vop.racing.desktop.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.circuit.Tile;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.model.tournament.Result;
import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.restcontroller.TournamentRestController;

public class TournamentRaceEngine extends GamePlayEngine {
  private Race race;
  private List<Result> results = new ArrayList<Result>();

  public TournamentRaceEngine(Race race, Circuit circuit, Car car) {
    super(circuit, car);
    this.race = race;
  }

  @Override
  public void processGameEngineTick(int time) {
    this.time = time;
    this.car.updatePosition();

    Point carPosition = this.car.getCenterPosition();

    Tile carPositionTile =
        this.circuit.getTileByXandY(carPosition.x / this.circuit.getTILESIZE(), -carPosition.y
            / this.circuit.getTILESIZE());

    detectCollission(carPositionTile);

    this.hasFinished = hasFinishedFlag(carPositionTile);

    if (this.hasFinished) {
      this.lastLapTime = lapTime;
      this.lapsDone++;

      // Create result after each lap to keep track of results of tournament races
      Result result = new Result();
      result.setLapNumber(this.lapsDone);
      result.setRaceId(this.race.getId());
      result.setTime(this.lastLapTime);
      result.setUserId(AuthenticatedUser.getInstance().getId());
      results.add(result);

      logger.info("lap nr: {}, lap time: {}", result.getLapNumber(), result.getTime());

      reset();

      if (lapCount == lapsDone) {
        logger.info("Player has finished all laps!");

        if (!results.isEmpty()) {
          new TournamentRestController().createResults(results);
        }

        hasLapsCompleted();

        // reset for new game
        resetGame();
      }

    }

  }

  public int getLapsDone() {
    return lapsDone;
  }

}
