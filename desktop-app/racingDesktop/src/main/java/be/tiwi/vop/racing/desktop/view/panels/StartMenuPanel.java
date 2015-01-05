package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.restcontroller.TournamentRestController;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;
import be.tiwi.vop.racing.desktop.view.panels.CarMenuPanel.CarMenuState;

public class StartMenuPanel extends JPanel implements ActionListener, Internationalization {
  private MainWindow window;

  private ResourceBundle languageBundle;

  private JPanel buttonPanel;
  private JButton startNewGameButton;
  private JButton tournamentButton;
  private JButton watchReplayButton;
  private JButton settingsButton;
  private JButton logoutButton;
  private JButton exitButton;

  public StartMenuPanel(MainWindow window) {
    super(new BorderLayout(10, 10));
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");
    setFocusable(true);
    setVisible(true);
    initLayoutPanel();
    requestFocus();

    Utility.assignComponentNames(this);
    this.setName("startMenuPanel");
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.startNewGameButton) {
      this.window.showCarMenu(CarMenuState.RACE);
    } else if (ae.getSource() == this.tournamentButton) {
      this.window.showCarMenu(CarMenuState.TOURNAMENT);
    } else if (ae.getSource() == this.watchReplayButton) {
      this.window.showReplayMenu();
    } else if (ae.getSource() == this.settingsButton) {
      this.window.showSettingsMenu();
    } else if (ae.getSource() == this.logoutButton) {
      AuthenticatedUser.resetInstance();
      this.window.showLoginPanel();
    } else {
      this.window.exitApplication();
    }
  }

  private void initLayoutPanel() {
    buttonPanel = new JPanel(new GridBagLayout());
    startNewGameButton = new JButton(this.languageBundle.getString("NEWGAME"));
    tournamentButton = new JButton(this.languageBundle.getString("TOURNAMENT"));
    watchReplayButton = new JButton(this.languageBundle.getString("WATCHREPLAY"));
    settingsButton = new JButton(this.languageBundle.getString("SETTINGS"));
    logoutButton = new JButton(this.languageBundle.getString("LOGOUT"));
    exitButton = new JButton(this.languageBundle.getString("EXITGAME"));

    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(startNewGameButton);
    buttonList.add(tournamentButton);
    buttonList.add(watchReplayButton);
    buttonList.add(settingsButton);
    buttonList.add(logoutButton);
    buttonList.add(exitButton);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    int countEnrolledTournaments = new TournamentRestController().loadEnrolledTournaments().size();

    if (countEnrolledTournaments == 0) {
      this.tournamentButton.setEnabled(false);
      JLabel notEnrolledLabel = new JLabel(this.languageBundle.getString("NOTENROLLED"));
      add(notEnrolledLabel, BorderLayout.SOUTH);
    }

    add(buttonPanel, BorderLayout.CENTER);
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
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.startNewGameButton.setText(this.languageBundle.getString("NEWGAME"));
    this.watchReplayButton.setText(this.languageBundle.getString("WATCHREPLAY"));
    this.settingsButton.setText(this.languageBundle.getString("SETTINGS"));
    this.logoutButton.setText(this.languageBundle.getString("LOGOUT"));
    this.exitButton.setText(this.languageBundle.getString("EXITGAME"));
  }

}
