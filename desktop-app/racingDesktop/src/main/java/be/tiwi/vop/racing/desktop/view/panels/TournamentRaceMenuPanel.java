package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.model.user.User;
import be.tiwi.vop.racing.desktop.restcontroller.CircuitRestController;
import be.tiwi.vop.racing.desktop.restcontroller.GhostRestController;
import be.tiwi.vop.racing.desktop.restcontroller.UserRestController;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class TournamentRaceMenuPanel extends JPanel implements ActionListener, Internationalization {
  private ResourceBundle languageBundle;

  private MainWindow window;
  private Circuit circuit;
  private Race race;

  private JPanel informationPanel;
  private JPanel buttonPanel;

  private JButton startGameButton;
  private JButton backButton;
  private JLabel circuitNameLabel;
  private JLabel designerNameLabel;
  private JLabel personalLapTimeLabel;
  private JLabel fastestLapTimeLabel;
  private JLabel circuitLapCountLabel;

  private JLabel circuitImageLabel;

  public TournamentRaceMenuPanel(MainWindow window, Circuit circuit, Race race) {
    super(new BorderLayout());
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    this.window = window;
    this.circuit = circuit;
    this.race = race;

    setFocusable(true);
    setVisible(true);
    initLayoutPanel();
    requestFocus();
  }

  private void initLayoutPanel() {
    this.circuitImageLabel = new JLabel("", null, SwingConstants.CENTER);
    this.circuitNameLabel =
        new JLabel(this.languageBundle.getString("CIRCUITNAME") + this.circuit.getName());
    this.designerNameLabel = new JLabel();
    this.personalLapTimeLabel = new JLabel();
    this.fastestLapTimeLabel = new JLabel();
    this.circuitLapCountLabel =
        new JLabel(this.languageBundle.getString("CIRCUITLAPS") + this.circuit.getLapCount());

    User designer = new UserRestController().getUser(this.circuit.getDesigner());
    if (designer != null) {
      designerNameLabel.setText(languageBundle.getString("CIRCUITDESIGNERNAME")
          + designer.getUsername());
    } else {
      designerNameLabel.setText(languageBundle.getString("CIRCUITDESIGNERNAME")
          + this.circuit.getDesigner());
    }

    Ghost userGhost = new GhostRestController().loadUserGhost(this.circuit.getId());
    if (userGhost != null) {
      personalLapTimeLabel.setText(languageBundle.getString("CIRCUITPERSONALLAPTIME")
          + Utility.timeFormat(userGhost.getTime()));
    } else {
      personalLapTimeLabel.setText(languageBundle.getString("CIRCUITPERSONALLAPTIME")
          + "[00:00.000] -- No time found");
    }

    Ghost fastestGhost = new GhostRestController().loadFastestGhost(this.circuit.getId());
    if (fastestGhost != null) {
      fastestLapTimeLabel.setText(languageBundle.getString("CIRCUITFASTESTLAPTIME")
          + Utility.timeFormat(fastestGhost.getTime()));
    } else {
      fastestLapTimeLabel.setText(languageBundle.getString("CIRCUITFASTESTLAPTIME")
          + "[00:00.000] -- No time found");
    }

    this.informationPanel = new JPanel(new GridBagLayout());

    addLabel(this.circuitImageLabel, 0);
    addLabel(this.circuitNameLabel, 1);
    addLabel(this.designerNameLabel, 2);
    addLabel(this.fastestLapTimeLabel, 3);
    addLabel(this.personalLapTimeLabel, 4);
    addLabel(this.circuitLapCountLabel, 5);

    add(this.informationPanel, BorderLayout.NORTH);

    PreviewPanel previewPanel = new PreviewPanel();
    previewPanel.loadPreview(this.circuit.getId());

    add(new JPanel().add(new JLabel(new ImageIcon(previewPanel.getScaledImage()))),
        BorderLayout.CENTER);

    buttonPanel = new JPanel(new GridBagLayout());
    this.startGameButton = new JButton(this.languageBundle.getString("STARTGAME"));
    this.backButton = new JButton(this.languageBundle.getString("BACK"));

    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(startGameButton);
    buttonList.add(backButton);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    add(buttonPanel, BorderLayout.SOUTH);
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
    this.informationPanel.add(label, gbc);
  }

  protected void addButton(JButton button, int column) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = column;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    buttonPanel.add(button, gbc);
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.startGameButton.setText(this.languageBundle.getString("STARTGAME"));
    this.backButton.setText(this.languageBundle.getString("BACK"));
    this.circuitNameLabel.setText(this.languageBundle.getString("CIRCUITNAME"));
    this.designerNameLabel.setText(this.languageBundle.getString("CIRCUITDESIGNERNAME"));
    this.personalLapTimeLabel.setText(this.languageBundle.getString("CIRCUITPERSONALLAPTIME"));
    this.fastestLapTimeLabel.setText(this.languageBundle.getString("CIRCUITFASTESTLAPTIME"));
    this.circuitLapCountLabel.setText(this.languageBundle.getString("CIRCUITLAPS"));
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.startGameButton) {
      this.window.showTournamentRaceGamePanel(this.race, this.circuit);
    } else if (ae.getSource() == this.backButton) {
      this.window.showTournamentMenu();
    }
  }

}
