package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.controller.GamePlayEngine;
import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.car.factory.CarFactory;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.listeners.LapCompletedListener;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class GamePanel extends CircuitPanel implements KeyListener, LapCompletedListener {
  private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);

  private BufferedImage carImage;
  private BufferedImage ghostImage;
  private MainWindow window;

  public GamePanel(MainWindow window, Circuit circuit) {
    super(circuit);
    this.window = window;

    this.engine = new GamePlayEngine(circuit, CarFactory.getCar());

    addKeyListener(this);

    loadCarImage();

    loadGhostImage();

    Utility.assignComponentNames(this);
    this.setName("gamePanel");
  }

  private void loadCarImage() {
    try {
      URL url =
          this.getClass().getClassLoader()
              .getResource(((GamePlayEngine) this.engine).getCar().getCarImageLocation());
      this.carImage = ImageIO.read(url);
    } catch (IOException ex) {
      logger.error("IO exception: " + ex.getMessage());
    }
  }

  private void loadGhostImage() {
    try {
      URL url = this.getClass().getClassLoader().getResource("ghosts/ghost_image.png");
      this.ghostImage = ImageIO.read(url);
    } catch (IOException ex) {
      logger.error("IO exception: " + ex.getMessage());
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    drawCircuit(g2d, ((GamePlayEngine) this.engine).getCar().getCenterPosition());

    drawObstacles(g2d, ((GamePlayEngine) this.engine).getCar().getCenterPosition());

    drawERSBatteryLevel(g2d, ((GamePlayEngine) this.engine).getCar().getBatteryERSLevel(),
        Car.getMaximumBatteryERSLevel(), getWidth(), getHeight());

    drawCar(g2d);

    for (Ghost ghost : this.engine.getGhosts()) {
      if (ghost.getPosition() != null) {
        drawGhost(g2d, ghost, ((GamePlayEngine) this.engine).getCar().getCenterPosition());
      }
    }
  }

  private void drawCar(Graphics2D g2d) {
    g2d.translate(getWidth() / 2, getHeight() / 2);
    g2d.scale(1, -1);
    g2d.rotate(((GamePlayEngine) this.engine).getCar().getOrientation());
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    g2d.drawImage(this.carImage, -this.carImage.getWidth() / 2, -this.carImage.getHeight() / 2,
        null);
    g2d.rotate(-((GamePlayEngine) this.engine).getCar().getOrientation());
  }

  private void drawERSBatteryLevel(Graphics2D g2d, int batteryLevel, int maximumBatteryLevel,
      int panelWidth, int panelHeight) {
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(BasicStroke.CAP_SQUARE));
    g2d.drawRect(panelWidth - 90, panelHeight - 30, 80, 20);
    if (batteryLevel > 0) {
      int center = (int) (80.0 * batteryLevel / maximumBatteryLevel);
      g2d.setColor(Color.RED);
      center -= 2;
      g2d.fillRect(panelWidth - 90 + 1, panelHeight - 30 + 1, center, 20 - 2);
      center += 1;
      g2d.setColor(new Color(0, 0, 0, 100));
      g2d.fillRect(panelWidth - 90 + center, panelHeight - 30, 80 - center, 20);
    }
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("TimesRomand", Font.BOLD, 10));
    g2d.drawString("(K)ERS", panelWidth - 70, panelHeight - 17);
  }

  private void drawGhost(Graphics2D g2d, Ghost ghost, Point carPosition) {
    g2d.translate(ghost.getPosition().x - carPosition.x, ghost.getPosition().y - carPosition.y);
    g2d.rotate(ghost.getOrientation());
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    g2d.drawImage(this.ghostImage, -this.ghostImage.getWidth() / 2,
        -this.ghostImage.getHeight() / 2, null);
    g2d.rotate(-ghost.getOrientation());
    g2d.translate(-ghost.getPosition().x + carPosition.x, -ghost.getPosition().y + carPosition.y);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not used, will only trigger if you press characters
  }

  @Override
  public void keyPressed(KeyEvent e) {
    ((GamePlayEngine) this.engine).handleInput(e.getKeyCode(), true);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    ((GamePlayEngine) this.engine).handleInput(e.getKeyCode(), false);
  }

  @Override
  public void updateCircuit(int time) {
    super.updateCircuit(time);
  }

  public void resetGameEngine() {
    ((GamePlayEngine) this.engine).resetGame();
  }

  public void setInitialCarPosition() {
    ((GamePlayEngine) this.engine).setCarStartPosition();
  }

  public void addLapCompletedListener() {
    ((GamePlayEngine) this.engine).addLapCompletedListener(this);
  }

  @Override
  public void lapCompleted(int lapCount) {
    logger.info("Has completed {} lap(s)", lapCount);
    this.window.hasCompletedGame();
  }

  public void loadGhosts(List<Ghost> ghosts) {
    this.engine.setGhosts(ghosts);
  }

}
