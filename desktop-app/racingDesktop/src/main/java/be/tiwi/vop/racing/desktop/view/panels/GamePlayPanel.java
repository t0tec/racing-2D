package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import be.tiwi.vop.racing.desktop.controller.GamePlayEngine;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuDialog.State;

public class GamePlayPanel extends JPanel implements ActionListener, Internationalization {
  private MainWindow window;

  private ResourceBundle languageBundle;

  private GamePanel gamePanel;
  private StatsInfoPanel statsInfoPanel;
  private JButton pauseGameButton;

  private Timer gameTimer;
  private final int DELAY = 20; // 20ms =~ 60fps
  private int gameTime;
  private int lapTime;

  public GamePlayPanel(MainWindow window, Circuit circuit) {
    super(new BorderLayout());
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");
    setFocusable(true);
    setVisible(true);
    this.gamePanel = new GamePanel(window, circuit);
    this.statsInfoPanel = new StatsInfoPanel();
    initLayoutPanel();
    this.gameTimer = new Timer(DELAY, this);
    resetTime();

    Utility.assignComponentNames(this);
    this.setName("gamePlayPanel");
  }

  public void startGame(List<Ghost> ghosts) {
    this.gamePanel.requestFocus();
    this.gamePanel.setInitialCarPosition();
    this.gamePanel.resetGameEngine();
    this.gamePanel.loadGhosts(ghosts);
    this.gamePanel.addLapCompletedListener();
    new CountDownTimerDialog(window, true, 3);
    startTimer();
    this.gamePanel.requestFocus();
  }

  public void stopGame() {
    stopTimer();
  }

  public void resumeGame() {
    startTimer();
    this.pauseGameButton.setEnabled(true);
    this.gamePanel.requestFocus();
  }

  public void restartGame() {
    resetTime();
    this.gamePanel.requestFocus();
    this.gamePanel.setInitialCarPosition();
    this.gamePanel.resetGameEngine();
    this.pauseGameButton.setEnabled(true);
    new CountDownTimerDialog(window, true, 3);
    this.gamePanel.requestFocus();
    startTimer();
  }

  private void initLayoutPanel() {
    this.gamePanel.createCircuitBoard();
    add(this.gamePanel, BorderLayout.CENTER);
    add(this.statsInfoPanel, BorderLayout.NORTH);

    JPanel pausePanel = new JPanel();
    pausePanel.setBackground(Color.BLACK);

    this.pauseGameButton = new JButton(this.languageBundle.getString("PAUSE"));
    this.pauseGameButton.addActionListener(this);
    pausePanel.add(this.pauseGameButton);
    add(pausePanel, BorderLayout.SOUTH);

    this.gamePanel.requestFocus();
  }

  private void startTimer() {
    this.gameTimer.start();
  }

  private void stopTimer() {
    this.gameTimer.stop();
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.pauseGameButton) {
      this.window.showGameMenuDialog(State.PAUSEGAME);
      this.stopTimer();
      this.pauseGameButton.setEnabled(false);
    }

    this.gamePanel.updateCircuit(this.gameTime);
    this.statsInfoPanel.updateGameTime(this.gameTime,
        ((GamePlayEngine) this.gamePanel.engine).getLastLapTime());
    updateTime();
  }

  private void updateTime() {
    this.gameTime += DELAY;
    this.lapTime = ((GamePlayEngine) this.gamePanel.engine).getLapTime() + DELAY;
    ((GamePlayEngine) this.gamePanel.engine).setLapTime(this.lapTime);
  }

  private void resetTime() {
    this.gameTime = 0;
  }

  public int getGameTime() {
    return this.gameTime;
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.pauseGameButton.setText(this.languageBundle.getString("PAUSE"));
  }
}
