package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class GameMenuDialog extends JDialog implements ActionListener, Internationalization {

  private static final Logger logger = LoggerFactory.getLogger(GameMenuDialog.class);

  public enum State {
    PAUSEGAME, ENDGAME, PAUSEREPLAY, ENDREPLAY, PAUSETOURNAMENTRACE, ENDTOURNAMENTRACE
  }

  private MainWindow window;

  private ResourceBundle languageBundle;

  private JPanel buttonPanel;
  private JButton resumeGameButton;
  private JButton restartGameButton;
  private JButton quitGameButton;
  private JButton resumeReplayButton;
  private JButton restartReplayButton;
  private JButton quitReplayButton;
  private JButton resumeTournamentRaceButton;
  private JButton returnToTournamentMenuButton;

  public GameMenuDialog(MainWindow window) {
    super(window, false);
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    this.setUndecorated(true);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    initLayoutPanel();

    Utility.assignComponentNames(this);
    this.setName("gameMenuDialog");
  }

  private void initLayoutPanel() {
    setLayout(new BorderLayout());
    setMinimumSize(new Dimension(300, 300));

    buttonPanel = new JPanel(new GridBagLayout());

    resumeGameButton = new JButton(this.languageBundle.getString("RESUMEGAME"));
    restartGameButton = new JButton(this.languageBundle.getString("RESTARTGAME"));
    quitGameButton = new JButton(this.languageBundle.getString("QUITGAME"));
    resumeReplayButton = new JButton(this.languageBundle.getString("RESUMEREPLAY"));
    restartReplayButton = new JButton(this.languageBundle.getString("RESTARTREPLAY"));
    quitReplayButton = new JButton(this.languageBundle.getString("QUITREPLAY"));
    resumeTournamentRaceButton = new JButton(this.languageBundle.getString("RESUMEGAME"));
    returnToTournamentMenuButton = new JButton("Return to tournament menu");

    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(resumeGameButton);
    buttonList.add(restartGameButton);
    buttonList.add(quitGameButton);
    buttonList.add(resumeReplayButton);
    buttonList.add(restartReplayButton);
    buttonList.add(quitReplayButton);
    buttonList.add(resumeTournamentRaceButton);
    buttonList.add(returnToTournamentMenuButton);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    this.setContentPane(buttonPanel);
  }

  private void addButton(JButton button, int row) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    buttonPanel.add(button, gbc);
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.resumeGameButton) {
      logger.info("Resuming game!");
      this.setVisible(false);
      this.window.getGamePlayPanel().resumeGame();
    } else if (ae.getSource() == this.restartGameButton) {
      logger.info("Restarting game!");
      this.setVisible(false);
      this.window.getGamePlayPanel().restartGame();
    } else if (ae.getSource() == this.quitGameButton) {
      logger.info("Quitting current game!");
      this.setVisible(false);
      this.window.showStartNewGameMenu();
    } else if (ae.getSource() == this.resumeReplayButton) {
      logger.info("Resuming replay!");
      this.setVisible(false);
      this.window.getReplayGamePlayPanel().resumeReplay();
    } else if (ae.getSource() == this.restartReplayButton) {
      logger.info("Restarting replay!");
      this.setVisible(false);
      this.window.getReplayGamePlayPanel().restartReplay();
    } else if (ae.getSource() == this.quitReplayButton) {
      logger.info("Quitting current replay!");
      this.setVisible(false);
      this.window.showReplayMenu();
    } else if (ae.getSource() == this.resumeTournamentRaceButton) {
      logger.info("Resuming current tournament race!");
      this.setVisible(false);
      this.window.getTournamentRaceGamePlayPanel().resumeGame();
    } else if (ae.getSource() == this.returnToTournamentMenuButton) {
      this.setVisible(false);
      this.window.showTournamentMenu();
    }
  }

  public void showCorrectGameMenuDialogState(State state) {
    if (state.equals(State.ENDGAME)) {
      this.setTitle(this.languageBundle.getString("ENDGAMETITLE"));
      setVisibilityButtons(false, true, true, false, false, false, false, false);
    } else if (state.equals(State.PAUSEGAME)) {
      this.setTitle(this.languageBundle.getString("PAUSEGAMETITLE"));
      setVisibilityButtons(true, true, true, false, false, false, false, false);
    } else if (state.equals(State.ENDREPLAY)) {
      this.setTitle(this.languageBundle.getString("ENDREPLAYTITLE"));
      setVisibilityButtons(false, false, false, false, true, true, false, false);
    } else if (state.equals(State.PAUSEREPLAY)) {
      this.setTitle(this.languageBundle.getString("PAUSEREPLAYTITLE"));
      setVisibilityButtons(false, false, false, true, true, true, false, false);
    } else if (state.equals(State.PAUSETOURNAMENTRACE)) {
      setVisibilityButtons(false, false, false, false, false, false, true, false);
    } else if (state.equals(State.ENDTOURNAMENTRACE)) {
      setVisibilityButtons(false, false, false, false, false, false, false, true);
    }
  }

  private void setVisibilityButtons(boolean resumeGame, boolean restartGame, boolean quitGame,
                                    boolean resumeReplay, boolean restartReplay, boolean quitReplay,
                                    boolean resumeTournament, boolean returnToTournament) {
    resumeGameButton.setVisible(resumeGame);
    restartGameButton.setVisible(restartGame);
    quitGameButton.setVisible(quitGame);
    resumeReplayButton.setVisible(resumeReplay);
    restartReplayButton.setVisible(restartReplay);
    quitReplayButton.setVisible(quitReplay);
    resumeTournamentRaceButton.setVisible(resumeTournament);
    returnToTournamentMenuButton.setVisible(returnToTournament);
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.resumeGameButton.setText(this.languageBundle.getString("RESUMEGAME"));
    this.restartGameButton.setText(this.languageBundle.getString("RESTARTGAME"));
    this.quitGameButton.setText(this.languageBundle.getString("QUITGAME"));
    this.resumeReplayButton.setText(this.languageBundle.getString("RESUMEREPLAY"));
    this.restartReplayButton.setText(this.languageBundle.getString("RESTARTREPLAY"));
    this.quitReplayButton.setText(this.languageBundle.getString("QUITREPLAY"));
    this.resumeTournamentRaceButton = new JButton(this.languageBundle.getString("RESUMEGAME"));
  }

}
