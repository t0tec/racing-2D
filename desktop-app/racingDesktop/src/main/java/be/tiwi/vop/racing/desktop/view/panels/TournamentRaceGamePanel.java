package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.controller.GamePlayEngine;
import be.tiwi.vop.racing.desktop.controller.TournamentRaceEngine;
import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.car.factory.CarFactory;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.listeners.LapCompletedListener;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class TournamentRaceGamePanel extends CircuitPanel implements KeyListener,
    LapCompletedListener {
  private static final Logger logger = LoggerFactory.getLogger(TournamentRaceGamePanel.class);

  private BufferedImage carImage;
  private MainWindow window;

  public TournamentRaceGamePanel(MainWindow window, Race race, Circuit circuit) {
    super(circuit);
    this.window = window;

    this.engine = new TournamentRaceEngine(race, circuit, CarFactory.getCar());

    addKeyListener(this);

    loadCarImage();

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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    drawCircuit(g2d, ((TournamentRaceEngine) this.engine).getCar().getCenterPosition());

    drawObstacles(g2d, ((TournamentRaceEngine) this.engine).getCar().getCenterPosition());

    drawERSBatteryLevel(g2d, ((TournamentRaceEngine) this.engine).getCar().getBatteryERSLevel(),
        Car.getMaximumBatteryERSLevel(), getWidth(), getHeight());

    drawCar(g2d);
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

  @Override
  public void keyTyped(KeyEvent e) {
    // will only trigger if you press characters
  }

  @Override
  public void keyPressed(KeyEvent e) {
    ((TournamentRaceEngine) this.engine).handleInput(e.getKeyCode(), true);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    ((TournamentRaceEngine) this.engine).handleInput(e.getKeyCode(), false);
  }

  @Override
  public void updateCircuit(int time) {
    super.updateCircuit(time);
  }

  public void resetGameEngine() {
    ((TournamentRaceEngine) this.engine).resetGame();
  }

  public void setInitialCarPosition() {
    ((TournamentRaceEngine) this.engine).setCarStartPosition();
  }

  public void addLapCompletedListener() {
    ((TournamentRaceEngine) this.engine).addLapCompletedListener(this);
  }

  @Override
  public void lapCompleted(int lapCount) {
    logger.info("Has completed {} lap(s)", lapCount);
    this.window.hasCompletedTournamentRaceGame();
  }
}
