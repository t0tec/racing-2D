package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.listeners.GhostIndexChangedBase;
import be.tiwi.vop.racing.desktop.model.listeners.GhostIndexChangedListener;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;

public class ReplayInfoPanel extends JPanel implements ActionListener, GhostIndexChangedBase,
    Internationalization {
  private static final Logger logger = LoggerFactory.getLogger(ReplayInfoPanel.class);

  private ResourceBundle languageBundle;

  private JButton nextGhostBtn;
  private JButton previousGhostBtn;

  private JPanel infoPanel;
  private JPanel buttonPanel;
  private JLabel gameTimeLabel;

  private List<GhostIndexChangedListener> ghostIndexChangedListeners;
  private int ghostIndex = 0;
  private int ghostListIndexMax;

  public ReplayInfoPanel() {
    super(new GridBagLayout());
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    setBackground(Color.BLACK);
    initLayoutPanel();

    this.ghostIndexChangedListeners = new ArrayList<GhostIndexChangedListener>();

    Utility.assignComponentNames(this);
    this.setName("replayInfoPanel");
  }

  private void initLayoutPanel() {
    infoPanel = new JPanel();
    infoPanel.setBackground(Color.BLACK);
    gameTimeLabel = new JLabel(this.languageBundle.getString("GAMETIME"));
    gameTimeLabel.setForeground(Color.WHITE);
    infoPanel.add(gameTimeLabel);

    buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.BLACK);
    nextGhostBtn = new JButton(this.languageBundle.getString("NEXT"));
    nextGhostBtn.setEnabled(false);
    previousGhostBtn = new JButton(this.languageBundle.getString("PREVIOUS"));
    previousGhostBtn.setEnabled(false);

    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(nextGhostBtn);
    buttonList.add(previousGhostBtn);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    addJPanel(infoPanel, 0);
    addJPanel(buttonPanel, 1);
  }

  private void addButton(JButton button, int column) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = column;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    buttonPanel.add(button, gbc);
  }

  private void addJPanel(JPanel panel, int row) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    this.add(panel, gbc);
  }

  public void updateGameTime(int gameTime) {
    this.gameTimeLabel.setText(this.languageBundle.getString("GAMETIME")
        + Utility.timeFormat(gameTime));
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.nextGhostBtn) {
      this.ghostIndex++;

      if (this.ghostIndex >= ghostListIndexMax) {
        this.nextGhostBtn.setEnabled(false);
      }

      if (this.ghostIndex > 0) {
        this.previousGhostBtn.setEnabled(true);
      }

      hasGhostIndexChanged();
      logger.info("Next ghost selected");
    } else if (ae.getSource() == this.previousGhostBtn) {
      this.ghostIndex--;

      if (this.ghostIndex < this.ghostListIndexMax) {
        this.nextGhostBtn.setEnabled(true);
      }

      if (this.ghostIndex <= 0) {
        this.previousGhostBtn.setEnabled(false);
      }

      hasGhostIndexChanged();
      logger.info("Previous ghost selected");
    } else {

    }
  }

  public void setCountGhosts(int countGhosts) {
    this.ghostListIndexMax = countGhosts - 1;

    if (this.ghostIndex < this.ghostListIndexMax) {
      this.nextGhostBtn.setEnabled(true);
    }

    if (this.ghostIndex > 0) {
      this.previousGhostBtn.setEnabled(true);
    }
  }

  @Override
  public void hasGhostIndexChanged() {
    for (GhostIndexChangedListener listener : this.ghostIndexChangedListeners) {
      listener.ghostIndexChangedListener(this.ghostIndex);
    }
  }

  @Override
  public void addGhostIndexChangedListener(GhostIndexChangedListener listener) {
    this.ghostIndexChangedListeners.add(listener);
  }

  @Override
  public void removeGhostIndexChangedListener(GhostIndexChangedListener listener) {
    this.ghostIndexChangedListeners.remove(listener);
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    gameTimeLabel.setText(this.languageBundle.getString("GAMETIME"));
    nextGhostBtn.setText(this.languageBundle.getString("NEXT"));
    previousGhostBtn.setText(this.languageBundle.getString("PREVIOUS"));
  }

}
