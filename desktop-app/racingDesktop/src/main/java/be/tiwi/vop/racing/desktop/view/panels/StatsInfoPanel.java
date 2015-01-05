package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;

import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;

public class StatsInfoPanel extends JPanel implements Internationalization {
  private ResourceBundle languageBundle;

  private JLabel gameTimeLabel;
  private JLabel lapTimeLabel;
  private JLabel playerNameLabel;
  private JLabel lapsDoneLabel;

  public StatsInfoPanel() {
    super(new GridBagLayout());
    this.languageBundle = ResourceBundle.getBundle("languages/language");
    setBackground(Color.BLACK);
    initLayoutPanel();
  }

  private void initLayoutPanel() {
    playerNameLabel = new JLabel();
    playerNameLabel.setForeground(Color.WHITE);
    playerNameLabel.setText(this.languageBundle.getString("PLAYERNAME")
        + AuthenticatedUser.getInstance().getUsername());
    gameTimeLabel = new JLabel(this.languageBundle.getString("GAMETIME"));
    gameTimeLabel.setForeground(Color.WHITE);
    lapTimeLabel = new JLabel(this.languageBundle.getString("LAPTIME"));
    lapTimeLabel.setForeground(Color.WHITE);
    lapsDoneLabel = new JLabel();
    lapsDoneLabel.setForeground(Color.WHITE);
    addLabel(playerNameLabel, 0);
    addLabel(gameTimeLabel, 1);
    addLabel(lapTimeLabel, 2);
    addLabel(lapsDoneLabel, 3);
  }

  public void updateGameTime(int gameTime, int lapTime) {
    this.gameTimeLabel.setText(this.languageBundle.getString("GAMETIME")
        + Utility.timeFormat(gameTime));
    this.lapTimeLabel.setText(this.languageBundle.getString("LAPTIME")
        + Utility.timeFormat(lapTime));
  }

  public void updateLapsDone(int lapsDone, int lapCount) {
    this.lapsDoneLabel.setText("Laps: " + lapsDone + "/" + lapCount);
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    playerNameLabel.setText(this.languageBundle.getString("PLAYERNAME"));
    this.gameTimeLabel.setText(this.languageBundle.getString("GAMETIME"));
    this.lapTimeLabel.setText(this.languageBundle.getString("LAPTIME"));
  }

  private void addLabel(JLabel label, int row) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 15);
    gbc.weightx = 0;
    this.add(label, gbc);
  }

}
