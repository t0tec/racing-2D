package be.tiwi.vop.racing.desktop.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit.Direction;
import be.tiwi.vop.racing.desktop.model.circuit.Obstacle;
import be.tiwi.vop.racing.desktop.model.circuit.Tile;
import be.tiwi.vop.racing.desktop.model.circuit.TileType;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.ghost.Pose;
import be.tiwi.vop.racing.desktop.model.listeners.LapCompletedBase;
import be.tiwi.vop.racing.desktop.model.listeners.LapCompletedListener;
import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.restcontroller.GhostRestController;

public class GamePlayEngine extends GameEngine implements LapCompletedBase {

  protected Car car;
  protected Ghost ghostCar;
  protected int lastLapTime;
  protected int lapTime;
  protected int lapCount;
  protected int lapsDone;
  protected boolean hasFinished;
  protected LinkedHashSet<Tile> reachedCheckpoints;
  protected Tile lastCarPositionTile;

  protected final List<LapCompletedListener> lapCompletedListeners;

  public GamePlayEngine(Circuit circuit, Car car) {
    super(circuit);
    this.lapTime = 0;
    this.car = car;
    this.ghostCar = new Ghost();
    this.ghostCar.setCircuitId(circuit.getId());
    this.ghostCar.setUserId(AuthenticatedUser.getInstance().getId());
    this.ghostCar.setUsername(AuthenticatedUser.getInstance().getUsername());
    this.lapCount = circuit.getLapCount();
    this.lapsDone = 0;
    this.hasFinished = false;
    this.reachedCheckpoints = new LinkedHashSet<Tile>();
    this.lapCompletedListeners = new ArrayList<LapCompletedListener>();

    if (this.circuit.getCheckpoints().size() == 0) {
      this.setLastTileAsCheckpoint();
    }
  }

  private void setLastTileAsCheckpoint() {
    Tile tile = this.circuit.getPreviousTileAfterStartTile();
    tile.setCheckpoint(1);
  }

  public Car getCar() {
    return this.car;
  }

  public int getLapTime() {
    return lapTime;
  }

  public void setLapTime(int lapTime) {
    this.lapTime = lapTime;
  }

  public int getLastLapTime() {
    return lastLapTime;
  }


  @Override
  public void processGameEngineTick(int time) {
    this.time = time;
    this.car.updatePosition();

    logCarPosition();

    haveGhostsFinishedFlag();

    Point carPosition = this.car.getCenterPosition();

    Tile carPositionTile =
        this.circuit.getTileByXandY(carPosition.x / this.circuit.getTILESIZE(), -carPosition.y
            / this.circuit.getTILESIZE());

    detectCollission(carPositionTile);

    this.hasFinished = hasFinishedFlag(carPositionTile);

    if (this.hasFinished) {
      // Update player ghost to check if he has a new fastest lap time
      this.lastLapTime = lapTime;
      new GhostRestController().createOrUpdateGhost(this.ghostCar);
      this.lapsDone++;
      logger.info("laps done: {} -- lap time: {}", this.lapsDone, this.lastLapTime);
      reset();

      if (lapCount == lapsDone) {
        logger.info("Player has reached the finishline!");
        hasLapsCompleted();

        // Reset for new game
        resetGame();
      }

    }
  }

  protected void logCarPosition() {
    this.ghostCar.getPoses().put(this.time,
        new Pose(this.car.getCenterPosition(), this.car.getOrientation()));
    this.ghostCar.setTime(this.time);
  }

  protected boolean hasFinishedFlag(Tile carPositionTile) {
    Point carPosition = this.car.getCenterPosition();

    if (carPosition.x > 0 && carPosition.y < 0) {
      // circuit x is positive circuit y is negative so need to invert carPosition.y
      carPositionTile =
          this.circuit.getTileByXandY(carPosition.x / this.circuit.getTILESIZE(), -carPosition.y
              / this.circuit.getTILESIZE());

      if (carPositionTile != null) {

        // logger.info("car position: ({}, {})", carPosition.x, -carPosition.y);

        if (carPositionTile.getType() == TileType.EARTH) {
          this.car.changeRoadResistance();
        } else {
          this.car.resetRoadResitance();
        }

        if (carPositionTile.getCheckpoint() != 0) {
          reachedCheckpoints.add(carPositionTile);
        }

        lastCarPositionTile = carPositionTile;

      } else {
        this.resetCarToPosition(lastCarPositionTile);
      }
    } else {
      this.resetCarToPosition(lastCarPositionTile);
    }

    if (!hasFinishedAllCheckPoints()) {
      return false;
    }

    return car.hasCrossedFinishline(this.circuit.getFinishLinePoint());
  }

  protected void detectCollission(Tile carPositionTile) {
    Obstacle obstacle = null;

    if (carPositionTile != null) {
      if (carPositionTile.getId() != null) {
        obstacle = this.circuit.getObstacleByTileId(carPositionTile.getId());

        if (obstacle != null && !obstacle.isCollected()) {
          car.doCollision(obstacle.getCenterPosition());

          if (car.isColliding(obstacle.getObstacleType()) && !obstacle.isCollected()) {
            obstacle.setCollected(true);
            logger.info("remaining obstacles: {}", this.circuit.getAmountOfRemainingObstacles());
          }
        }
      }
    }
  }

  private boolean hasFinishedAllCheckPoints() {
    if (this.circuit.getCheckpoints().size() == reachedCheckpoints.size()) {
      return true;
    }
    return false;
  }

  public void setCarStartPosition() {
    logger.info("Setting car to start position: ({}, {})", this.circuit.getStartTile().getX(),
        this.circuit.getStartTile().getY());

    this.car.resetCar();

    if (this.circuit.getDirection() == Direction.UP) {
      this.car.setOrientation(Math.PI / 2);
    } else if (this.circuit.getDirection() == Direction.LEFT) {
      this.car.setCenterPosition(new Point(this.circuit.getStartTile().getX()
          * this.circuit.getTILESIZE() + this.circuit.getTILESIZE() / 2 - Car.getImageWidth() / 2,
          -this.circuit.getStartTile().getY() * this.circuit.getTILESIZE()
              - this.circuit.getTILESIZE() / 2));
      this.car.setOrientation(Math.PI);
    } else if (this.circuit.getDirection() == Direction.DOWN) {
      this.car.setOrientation(-(Math.PI / 2));
    } else {
      this.car.setCenterPosition(new Point(this.circuit.getStartTile().getX()
          * this.circuit.getTILESIZE() + this.circuit.getTILESIZE() / 2 + Car.getImageWidth() / 2,
          -this.circuit.getStartTile().getY() * this.circuit.getTILESIZE()
              - this.circuit.getTILESIZE() / 2));
      this.car.setOrientation(0); // Direction.RIGHT
    }
  }

  private void resetCarToPosition(Tile tile) {
    logger.info("(Re)setting car to position: ({}, {})", tile.getX(), tile.getY());

    this.car.resetCar();

    this.car.setCenterPosition(new Point(tile.getX() * this.circuit.getTILESIZE()
        + this.circuit.getTILESIZE() / 2, -tile.getY() * this.circuit.getTILESIZE()
        - this.circuit.getTILESIZE() / 2));
  }

  public void handleInput(int keyCode, boolean isPressed) {
    this.car.handleKeyInput(keyCode, isPressed);
  }

  public void reset() {
    this.hasFinished = false;
    reachedCheckpoints.clear();
    this.lapTime = 0;
    this.car.setBatteryERSLevel(Car.getMaximumBatteryERSLevel());
    setObstaclesPositions();
  }

  public void resetGame() {
    this.hasFinished = false;
    reachedCheckpoints.clear();
    this.lapsDone = 0;
    this.lapTime = 0;
    this.car.setBatteryERSLevel(Car.getMaximumBatteryERSLevel());
    setObstaclesPositions();
  }

  @Override
  public void hasLapsCompleted() {
    for (Iterator<LapCompletedListener> iterator = this.lapCompletedListeners.iterator(); iterator
        .hasNext();) {
      LapCompletedListener listener = iterator.next();
      listener.lapCompleted(this.lapCount);
    }
  }

  @Override
  public void addLapCompletedListener(LapCompletedListener listener) {
    this.lapCompletedListeners.add(listener);
  }

  @Override
  public void removeLapCompletedListener(LapCompletedListener listener) {
    this.lapCompletedListeners.add(listener);
  }

}
