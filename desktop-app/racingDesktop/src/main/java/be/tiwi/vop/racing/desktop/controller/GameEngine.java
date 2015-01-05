package be.tiwi.vop.racing.desktop.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.circuit.Obstacle;
import be.tiwi.vop.racing.desktop.model.circuit.Tile;
import be.tiwi.vop.racing.desktop.model.circuit.TileType;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.listeners.GhostFinishedBase;
import be.tiwi.vop.racing.desktop.model.listeners.GhostFinishedListener;

public abstract class GameEngine implements GhostFinishedBase {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected Circuit circuit;
  protected int time;

  protected List<Ghost> ghosts;
  protected final List<GhostFinishedListener> ghostFinishedListeners;

  public GameEngine(Circuit circuit) {
    logger.info("Creating {}", this.getClass().getSimpleName());

    this.circuit = circuit;
    this.time = 0;

    this.ghosts = new ArrayList<Ghost>();
    this.ghostFinishedListeners = new ArrayList<GhostFinishedListener>();

    setObstaclesPositions();
  }

  public Circuit getCircuit() {
    return this.circuit;
  }

  public List<Ghost> getGhosts() {
    return ghosts;
  }

  public void setGhosts(List<Ghost> ghosts) {
    this.ghosts = ghosts;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  protected void setObstaclesPositions() {
    for (Tile tile : this.circuit.getTiles()) {
      Obstacle obstacle = this.circuit.getObstacleByTileId(tile.getId());

      if (obstacle != null) {
        obstacle.setCollected(false);

        if (obstacle.getPlace().equals("LEFT")) {
          obstacle.setPosition(new Point(tile.getX() * this.circuit.getTILESIZE()
              - Obstacle.getObstaclesize(), tile.getY() * this.circuit.getTILESIZE()));
        }

        if (obstacle.getPlace().equals("RIGHT")) {
          obstacle.setPosition(new Point(tile.getX() * this.circuit.getTILESIZE()
              + Obstacle.getObstaclesize(), tile.getY() * this.circuit.getTILESIZE()));
        }

        if (tile.getType().equals(TileType.STRAIGHT) && obstacle.getPlace().equals("LEFT")) {
          obstacle.setPosition(new Point(tile.getX() * this.circuit.getTILESIZE(), tile.getY()
              * this.circuit.getTILESIZE() - Obstacle.getObstaclesize()));
        }

        if (tile.getType().equals(TileType.STRAIGHT) && obstacle.getPlace().equals("RIGHT")) {
          obstacle.setPosition(new Point(tile.getX() * this.circuit.getTILESIZE(), tile.getY()
              * this.circuit.getTILESIZE() + Obstacle.getObstaclesize()));
        }
      }

    }
  }

  public abstract void processGameEngineTick(int time);

  protected void haveGhostsFinishedFlag() {
    int countGhostsDone = 0;

    for (Ghost ghost : this.ghosts) {
      if (!ghost.update(this.time)) {
        countGhostsDone++;
      }
    }

    if (countGhostsDone == this.ghosts.size()) {
      haveGhostsFinished();
    }
  }

  @Override
  public void haveGhostsFinished() {
    for (Iterator<GhostFinishedListener> iterator = this.ghostFinishedListeners.iterator(); iterator
        .hasNext();) {
      GhostFinishedListener listener = iterator.next();
      listener.ghostHasFinished();
    }
  }

  @Override
  public void addGhostFinishedListener(GhostFinishedListener listener) {
    this.ghostFinishedListeners.add(listener);
  }

  @Override
  public void removeGhostFinishedListener(GhostFinishedListener listener) {
    this.ghostFinishedListeners.remove(listener);
  }

}
