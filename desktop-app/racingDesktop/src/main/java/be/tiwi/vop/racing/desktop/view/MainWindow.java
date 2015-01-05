package be.tiwi.vop.racing.desktop.view;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.panels.CarMenuPanel;
import be.tiwi.vop.racing.desktop.view.panels.CarMenuPanel.CarMenuState;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuDialog;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuDialog.State;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuPanel;
import be.tiwi.vop.racing.desktop.view.panels.GamePlayPanel;
import be.tiwi.vop.racing.desktop.view.panels.LoginPanel;
import be.tiwi.vop.racing.desktop.view.panels.ReplayGamePlayPanel;
import be.tiwi.vop.racing.desktop.view.panels.ReplayMenuPanel;
import be.tiwi.vop.racing.desktop.view.panels.SettingsPanel;
import be.tiwi.vop.racing.desktop.view.panels.StartMenuPanel;
import be.tiwi.vop.racing.desktop.view.panels.TournamentMenuPanel;
import be.tiwi.vop.racing.desktop.view.panels.TournamentRaceGamePlayPanel;
import be.tiwi.vop.racing.desktop.view.panels.TournamentRaceMenuPanel;

public class MainWindow extends MainJFrame {
  private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

  private LoginPanel loginPanel;
  private StartMenuPanel startMenuPanel;
  private GameMenuPanel newGameMenuPanel;
  private CarMenuPanel carMenuPanel;
  private GamePlayPanel gamePlayPanel;
  private TournamentMenuPanel tournamentMenuPanel;
  private TournamentRaceMenuPanel tournamenRaceMenuPanel;
  private TournamentRaceGamePlayPanel tournamentRaceGamePlayPanel;
  private ReplayMenuPanel replayMenuPanel;
  private ReplayGamePlayPanel replayGamePlayPanel;
  private GameMenuDialog gameMenuDialog;
  private SettingsPanel settingsPanel;

  public MainWindow() {
    this.settingsPanel = new SettingsPanel(this);
  }

  public GamePlayPanel getGamePlayPanel() {
    return this.gamePlayPanel;
  }

  public ReplayGamePlayPanel getReplayGamePlayPanel() {
    return this.replayGamePlayPanel;
  }

  public TournamentRaceGamePlayPanel getTournamentRaceGamePlayPanel() {
    return this.tournamentRaceGamePlayPanel;
  }

  public void showLoginPanel() {
    logger.info("Setting up login window");
    this.loginPanel = new LoginPanel(this);
    this.setContentPane(loginPanel);
    this.revalidate();
    this.repaint();
  }

  public void showStartMenu() {
    logger.info("Setting up start menu window");
    this.startMenuPanel = new StartMenuPanel(this);
    this.setContentPane(startMenuPanel);
    this.revalidate();
    this.repaint();
  }

  public void showCarMenu(CarMenuState state) {
    logger.info("Setting up menu window to choose car");
    this.carMenuPanel = new CarMenuPanel(this);
    this.carMenuPanel.translateComponent(ResourceBundle.getBundle("languages/language"));
    this.setContentPane(carMenuPanel);
    this.carMenuPanel.setCarMenuState(state);
    this.revalidate();
    this.repaint();
  }

  public void showStartNewGameMenu() {
    logger.info("Setting up menu window to choose circuit game");
    this.newGameMenuPanel = new GameMenuPanel(this);
    this.setContentPane(newGameMenuPanel);
    this.revalidate();
    this.repaint();
  }

  public void showTournamentMenu() {
    logger.info("Setting up menu window to choose tournament");
    this.tournamentMenuPanel = new TournamentMenuPanel(this);
    this.setContentPane(tournamentMenuPanel);
    this.revalidate();
    this.repaint();
  }

  public void showTournamentRaceMenu(Circuit circuit, Race race) {
    logger.info("Setting up tournament race menu to select ghosts");
    this.tournamenRaceMenuPanel = new TournamentRaceMenuPanel(this, circuit, race);
    this.setContentPane(tournamenRaceMenuPanel);
    this.revalidate();
    this.repaint();
  }

  public void showReplayMenu() {
    logger.info("Setting up menu to watch replays");
    this.replayMenuPanel = new ReplayMenuPanel(this);
    this.setContentPane(replayMenuPanel);
    this.revalidate();
    this.repaint();
  }

  public void showGamePanel(Circuit circuit, List<Ghost> ghosts) {
    logger.info("Setting up window to start the game");
    this.gamePlayPanel = new GamePlayPanel(this, circuit);
    this.setContentPane(gamePlayPanel);
    this.revalidate();
    this.repaint();
    this.gamePlayPanel.startGame(ghosts);
    logger.info("Game has started!");
  }

  public void hasCompletedGame() {
    logger.info("Setting up panel to show end game/replay menu when game/replay is done");
    handleGameEnding();
    logger.info("Completed game in {}", Utility.timeFormat(this.gamePlayPanel.getGameTime()));

    this.showGameMenuDialog(State.ENDGAME);
  }

  public void handleGameEnding() {
    this.gamePlayPanel.stopGame();
  }

  public void showTournamentRaceGamePanel(Race race, Circuit circuit) {
    logger.info("Setting up window to start tournament race");

    this.tournamentRaceGamePlayPanel = new TournamentRaceGamePlayPanel(this, race, circuit);
    this.setContentPane(tournamentRaceGamePlayPanel);
    this.revalidate();
    this.repaint();
    this.tournamentRaceGamePlayPanel.startTournamentRaceGame();
    logger.info("Tournament race game has started!");
  }

  public void hasCompletedTournamentRaceGame() {
    logger.info("Setting up panel to show end tournament game menu when tournament race is done");
    handleTournamentRaceGameEnding();
    logger.info("Completed in {}",
        Utility.timeFormat(this.tournamentRaceGamePlayPanel.getGameTime()));

    this.showGameMenuDialog(State.ENDTOURNAMENTRACE);
  }

  public void handleTournamentRaceGameEnding() {
    this.tournamentRaceGamePlayPanel.stopGame();
  }

  public void showReplayGamePanel(Circuit circuit, List<Ghost> ghosts) {
    logger.info("Setting up window to start replay");
    this.replayGamePlayPanel = new ReplayGamePlayPanel(this, circuit);
    this.setContentPane(replayGamePlayPanel);
    this.revalidate();
    this.repaint();
    this.replayGamePlayPanel.startReplay(ghosts);
    logger.info("Replay has started!");
  }

  public void hasCompletedReplay() {
    this.replayGamePlayPanel.stopReplay();

    this.showGameMenuDialog(State.ENDREPLAY);

    logger.info("Replay completed");
  }

  public void showGameMenuDialog(State state) {
    logger.info("Setting up panel to show dialog menu when in game");
    gameMenuDialog = new GameMenuDialog(this);
    gameMenuDialog.showCorrectGameMenuDialogState(state);
    gameMenuDialog.pack();
    gameMenuDialog.setLocationRelativeTo(this.getContentPane());
    gameMenuDialog.setVisible(true);
    this.revalidate();
    this.repaint();
  }

  public void showSettingsMenu() {
    logger.info("Setting up window to show the application settings");
    this.setContentPane(settingsPanel);
    this.revalidate();
    this.repaint();
  }

  public void exitApplication() {
    logger.info("Exiting application");
    System.exit(0);
  }

  private List<Component> getAllComponents(final Container c) {
    Component[] comps = c.getComponents();
    List<Component> compList = new ArrayList<Component>();
    for (Component comp : comps) {
      compList.add(comp);
      if (comp instanceof Container) {
        compList.addAll(getAllComponents((Container) comp));
      }
    }
    return compList;
  }

  public void translateAllComponents() {
    logger.info("Translating all components");
    ResourceBundle bundle = ResourceBundle.getBundle("languages/language");
    List<Component> list = new ArrayList<Component>();
    list.addAll(Arrays.asList(getWindows()));
    list.addAll(getAllComponents(this));
    for (Component component : list) {
      if (component instanceof Internationalization) {
        ((Internationalization) component).translateComponent(bundle);
      }
    }
  }

}
