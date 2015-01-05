package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import be.tiwi.vop.racing.desktop.controller.TournamentRaceEngine;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuDialog.State;

public class TournamentRaceGamePlayPanel extends JPanel implements ActionListener,
    Internationalization {
  private MainWindow window;

  private ResourceBundle languageBundle;

  private TournamentRaceGamePanel tournamentRaceGamePanel;
  private StatsInfoPanel statsInfoPanel;
  private JButton pauseGameButton;

  private Timer gameTimer;
  private final int DELAY = 20; // 20ms =~ 60fps
  private int gameTime;
  private int lapTime;

  public TournamentRaceGamePlayPanel(MainWindow window, Race race, Circuit circuit) {
    super(new BorderLayout());
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");
    setFocusable(true);
    setVisible(true);
    this.tournamentRaceGamePanel = new TournamentRaceGamePanel(window, race, circuit);
    this.statsInfoPanel = new StatsInfoPanel();
    initLayoutPanel();
    this.gameTimer = new Timer(DELAY, this);
    resetTime();

    Utility.assignComponentNames(this);
    this.setName("tournamentRaceGamePlayPanel");
  }

  public void startTournamentRaceGame() {
    this.tournamentRaceGamePanel.requestFocus();
    this.tournamentRaceGamePanel.setInitialCarPosition();
    this.tournamentRaceGamePanel.resetGameEngine();
    this.tournamentRaceGamePanel.addLapCompletedListener();
    new CountDownTimerDialog(window, true, 3);
    startTimer();
    this.tournamentRaceGamePanel.requestFocus();
  }

  public void stopGame() {
    stopTimer();
  }

  public void resumeGame() {
    startTimer();
    this.pauseGameButton.setEnabled(true);
    this.tournamentRaceGamePanel.requestFocus();
  }

  private void initLayoutPanel() {
    this.tournamentRaceGamePanel.createCircuitBoard();
    add(this.tournamentRaceGamePanel, BorderLayout.CENTER);
    add(this.statsInfoPanel, BorderLayout.NORTH);

    JPanel pausePanel = new JPanel();
    pausePanel.setBackground(Color.BLACK);

    this.pauseGameButton = new JButton(this.languageBundle.getString("PAUSE"));
    this.pauseGameButton.addActionListener(this);
    pausePanel.add(this.pauseGameButton);
    add(pausePanel, BorderLayout.SOUTH);

    this.tournamentRaceGamePanel.requestFocus();
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
      this.window.showGameMenuDialog(State.PAUSETOURNAMENTRACE);
      this.stopTimer();
      this.pauseGameButton.setEnabled(false);
    }

    this.tournamentRaceGamePanel.updateCircuit(this.gameTime);
    this.statsInfoPanel.updateGameTime(this.gameTime,
        ((TournamentRaceEngine) this.tournamentRaceGamePanel.engine).getLastLapTime());
    this.statsInfoPanel.updateLapsDone(
        ((TournamentRaceEngine) this.tournamentRaceGamePanel.engine).getLapsDone(),
        ((TournamentRaceEngine) this.tournamentRaceGamePanel.engine).getCircuit().getLapCount());
    updateTime();
  }

  private void updateTime() {
    this.gameTime += DELAY;
    this.lapTime =
        ((TournamentRaceEngine) this.tournamentRaceGamePanel.engine).getLapTime() + DELAY;
    ((TournamentRaceEngine) this.tournamentRaceGamePanel.engine).setLapTime(this.lapTime);
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
