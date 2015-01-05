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

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuDialog.State;

public class ReplayGamePlayPanel extends JPanel implements ActionListener, Internationalization {
  private MainWindow window;

  private ResourceBundle languageBundle;

  private ReplayGamePanel replayGamePanel;
  private ReplayInfoPanel replayInfoPanel;
  private JButton pauseReplayButton;

  private Timer gameTimer;
  private final int DELAY = 20;
  private int gameTime;

  public ReplayGamePlayPanel(MainWindow window, Circuit circuit) {
    super(new BorderLayout());
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    setFocusable(true);
    setVisible(true);
    this.replayGamePanel = new ReplayGamePanel(window, circuit);
    this.replayInfoPanel = new ReplayInfoPanel();
    initLayoutPanel();

    this.gameTimer = new Timer(DELAY, this);
    resetTime();

    Utility.assignComponentNames(this);
    this.setName("replayGamePlayPanel");
  }

  public void startReplay(List<Ghost> ghosts) {
    this.replayGamePanel.requestFocus();
    this.replayGamePanel.loadGhosts(ghosts);
    this.replayGamePanel.addGhostFinishedListener();
    this.replayGamePanel.requestFocus();
    this.replayInfoPanel.setCountGhosts(ghosts.size());
    this.replayInfoPanel.addGhostIndexChangedListener(this.replayGamePanel);
    startTimer();
  }

  public void stopReplay() {
    stopTimer();
    this.pauseReplayButton.setEnabled(false);
  }

  public void resumeReplay() {
    startTimer();
    this.pauseReplayButton.setEnabled(true);
    this.replayGamePanel.requestFocus();
  }

  private void initLayoutPanel() {
    this.replayGamePanel.createCircuitBoard();
    add(this.replayGamePanel, BorderLayout.CENTER);
    add(this.replayInfoPanel, BorderLayout.NORTH);

    JPanel pausePanel = new JPanel();
    pausePanel.setBackground(Color.BLACK);

    this.pauseReplayButton = new JButton(this.languageBundle.getString("PAUSE"));
    this.pauseReplayButton.addActionListener(this);
    pausePanel.add(this.pauseReplayButton);
    add(pausePanel, BorderLayout.SOUTH);

    this.replayGamePanel.requestFocus();
  }

  private void startTimer() {
    this.gameTimer.start();
  }

  private void stopTimer() {
    this.gameTimer.stop();
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.pauseReplayButton) {
      this.window.showGameMenuDialog(State.PAUSEREPLAY);
      this.stopTimer();
      this.pauseReplayButton.setEnabled(false);
    }

    this.replayGamePanel.updateCircuit(this.gameTime);
    this.replayInfoPanel.updateGameTime(this.gameTime);
    updateTime();
  }

  private void updateTime() {
    this.gameTime += DELAY;
  }

  private void resetTime() {
    this.gameTime = 0;
  }

  public void restartReplay() {
    resetTime();
    startTimer();
    this.pauseReplayButton.setEnabled(true);
    this.replayGamePanel.requestFocus();
  }

  public int getGameTime() {
    return this.gameTime;
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.pauseReplayButton.setText(this.languageBundle.getString("PAUSE"));
  }
}
