package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.controller.ReplayGamePlayEngine;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.listeners.GhostFinishedListener;
import be.tiwi.vop.racing.desktop.model.listeners.GhostIndexChangedListener;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class ReplayGamePanel extends CircuitPanel implements GhostFinishedListener,
    GhostIndexChangedListener {
  private static final Logger logger = LoggerFactory.getLogger(ReplayGamePanel.class);

  private BufferedImage ghostImage;
  private MainWindow window;
  private int ghostIndex = 0;

  public ReplayGamePanel(MainWindow window, Circuit circuit) {
    super(circuit);
    this.engine = new ReplayGamePlayEngine(circuit);
    this.window = window;
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

    if (this.engine.getGhosts().get(ghostIndex).getPosition() != null) {
      drawCircuit(g2d, this.engine.getGhosts().get(ghostIndex).getPosition());
    }

    g2d.translate(getWidth() / 2, getHeight() / 2);
    g2d.scale(1, -1);

    Point selectedGhostPosition = this.engine.getGhosts().get(this.ghostIndex).getPosition();

    for (Ghost ghost : this.engine.getGhosts()) {
      if (ghost.getPosition() != null) {
        drawGhost(g2d, ghost, selectedGhostPosition);
      }
    }
  }

  public void loadGhosts(List<Ghost> ghosts) {
    this.engine.setGhosts(ghosts);
  }

  private void drawGhost(Graphics2D g2d, Ghost ghost, Point selectedGhostPosition) {
    g2d.translate(ghost.getPosition().x - selectedGhostPosition.x, ghost.getPosition().y
        - selectedGhostPosition.y);
    g2d.rotate(ghost.getOrientation());
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    g2d.drawImage(this.ghostImage, -this.ghostImage.getWidth() / 2,
        -this.ghostImage.getHeight() / 2, null);
    g2d.rotate(-ghost.getOrientation());
    g2d.translate(-ghost.getPosition().x + selectedGhostPosition.x, -ghost.getPosition().y
        + selectedGhostPosition.y);
  }

  public void addGhostFinishedListener() {
    this.engine.addGhostFinishedListener(this);
  }

  @Override
  public void updateCircuit(int time) {
    super.updateCircuit(time);
  }

  @Override
  public void ghostHasFinished() {
    logger.info("The ghost(s) has/have finished the race");
    this.window.hasCompletedReplay();
  }

  @Override
  public void ghostIndexChangedListener(int index) {
    logger.info("Selected a ghost with index {}", index);

    if ((index <= this.engine.getGhosts().size() || index >= 0)) {
      this.ghostIndex = index;
    }
  }
}
