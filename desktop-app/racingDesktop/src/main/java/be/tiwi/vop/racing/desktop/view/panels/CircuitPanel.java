package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.controller.GameEngine;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit.Direction;
import be.tiwi.vop.racing.desktop.model.circuit.Obstacle;
import be.tiwi.vop.racing.desktop.model.circuit.Tile;
import be.tiwi.vop.racing.desktop.model.circuit.TileType;
import be.tiwi.vop.racing.desktop.util.ResourceManager;

public class CircuitPanel extends JPanel {
  private static final Logger logger = LoggerFactory.getLogger(CircuitPanel.class);

  protected GameEngine engine;
  private BufferedImage circuitImage;

  public CircuitPanel(Circuit circuit) {
    this.setBackground(Color.LIGHT_GRAY);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  public void drawCircuit(Graphics2D g, Point carCenterPosition) {
    g.drawImage(this.circuitImage, -carCenterPosition.x + getWidth() / 2, carCenterPosition.y
        + getHeight() / 2, null);
  }

  protected void createCircuitBoard() {
    logger.info("Creating circuit board");
    this.circuitImage =
        new BufferedImage(this.engine.getCircuit().getColumns()
            * this.engine.getCircuit().getTILESIZE(), this.engine.getCircuit().getRows()
            * this.engine.getCircuit().getTILESIZE(), BufferedImage.TYPE_INT_RGB);

    Graphics2D g2d = (Graphics2D) this.circuitImage.getGraphics();

    for (int y = 0; y < this.engine.getCircuit().getColumns(); y++) {
      for (int x = 0; x < this.engine.getCircuit().getRows(); x++) {
        Tile tile = this.engine.getCircuit().getTileByXandY(y, x);

        if (tile != null) {
          drawTile(g2d, tile.getX() * this.engine.getCircuit().getTILESIZE(), tile.getY()
              * this.engine.getCircuit().getTILESIZE(), loadTileImage(tile));
        } else {
          Tile earthTile = new Tile();
          earthTile.setType(TileType.EARTH);
          earthTile.setId(0);
          earthTile.setX(y);
          earthTile.setY(x);
          earthTile.setCircuitid(this.engine.getCircuit().getId());
          earthTile.setCheckpoint(0);
          this.engine.getCircuit().getTiles().add(earthTile);

          drawTile(g2d, earthTile.getX() * this.engine.getCircuit().getTILESIZE(), earthTile.getY()
              * this.engine.getCircuit().getTILESIZE(), loadTileImage(earthTile));
        }
      }
    }
  }

  private void drawTile(Graphics2D g2d, int x, int y, Image image) {
    AffineTransform at = new AffineTransform();
    at.translate(x, y);
    g2d.drawImage(image, at, null);
  }

  private BufferedImage loadTileImage(Tile tile) {
    BufferedImage bufferedImage;

    if (tile.getCheckpoint() != 0) {
      bufferedImage =
          (BufferedImage) ResourceManager.getInstance().getImage(
              tile.getType().toString() + "_CHECKPOINT");
    } else {
      bufferedImage =
          (BufferedImage) ResourceManager.getInstance().getImage(tile.getType().toString());
    }

    if (tile.getType() == TileType.START) {
      bufferedImage = setStartTileDirection(bufferedImage);
    }

    return bufferedImage;
  }

  private BufferedImage setStartTileDirection(BufferedImage bufferedImage) {
    if (this.engine.getCircuit().getDirection() == Direction.LEFT) {
      return horizontalflip(bufferedImage);
    } else if (this.engine.getCircuit().getDirection() == Direction.UP) {
      return verticalflip(bufferedImage);
    } else {
      return bufferedImage;
    }
  }

  protected void drawObstacles(Graphics2D g2d, Point carCenterPosition) {
    for (int y = 0; y < this.engine.getCircuit().getColumns(); y++) {
      for (int x = 0; x < this.engine.getCircuit().getRows(); x++) {
        Tile tile = this.engine.getCircuit().getTileByXandY(y, x);

        if (tile != null) {
          Obstacle obstacle = this.engine.getCircuit().getObstacleByTileId(tile.getId());
          if (obstacle != null && !obstacle.isCollected()) {
            drawObstacle(g2d, obstacle, tile.getX(), tile.getY(), carCenterPosition);
          }
        }
      }
    }
  }

  private void drawObstacle(Graphics2D g2d, Obstacle obstacle, int x, int y, Point carPosition) {
    g2d.translate(-carPosition.x + getWidth() / 2, carPosition.y + getHeight() / 2);
    g2d.drawImage(ResourceManager.getInstance().getImage(obstacle.getObstacleType().toString()),
        obstacle.getPosition().x + Obstacle.getObstaclesize(),
        obstacle.getPosition().y + Obstacle.getObstaclesize(), null);
    g2d.translate(carPosition.x - getWidth() / 2, -carPosition.y - getHeight() / 2);
  }

  private BufferedImage horizontalflip(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(w, h, img.getType());
    Graphics2D g = dimg.createGraphics();
    g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
    g.dispose();
    return dimg;
  }

  private BufferedImage verticalflip(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
    Graphics2D g = dimg.createGraphics();
    g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
    g.dispose();
    return dimg;
  }

  public void updateCircuit(int time) {
    this.engine.processGameEngineTick(time);
    repaint();
  }
}
