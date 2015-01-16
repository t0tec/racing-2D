package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.model.user.User;
import be.tiwi.vop.racing.desktop.restcontroller.CircuitRestController;
import be.tiwi.vop.racing.desktop.restcontroller.GhostRestController;
import be.tiwi.vop.racing.desktop.restcontroller.UserRestController;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class ReplayMenuPanel extends CircuitMenu {
  private static final Logger logger = LoggerFactory.getLogger(ReplayMenuPanel.class);

  private MainWindow window;

  public ReplayMenuPanel(MainWindow window) {
    super();
    this.window = window;
    initLayoutPanel();

    this.setName("replayMenuPanel");
  }

  private void initLayoutPanel() {
    startGameButton.setVisible(false);

    circuitList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
          return;
        }

        logger.info("Selected: " + circuitList.getSelectedValue().toString());
        Circuit currentCircuit = (Circuit) circuitList.getSelectedValue();

        circuitNameLabel.setText(languageBundle.getString("CIRCUITNAME") + currentCircuit.getName());

        User designer = new UserRestController().getUser(currentCircuit.getDesigner());
        if (designer != null) {
          designerNameLabel.setText(languageBundle.getString("CIRCUITDESIGNERNAME")
              + designer.getUsername());
        } else {
          designerNameLabel.setText(languageBundle.getString("CIRCUITDESIGNERNAME")
              + currentCircuit.getDesigner());
        }

        Ghost userGhost = new GhostRestController().loadUserGhost(currentCircuit.getId());
        if (userGhost != null) {
          personalLapTimeLabel.setText(languageBundle.getString("CIRCUITPERSONALLAPTIME")
              + Utility.timeFormat(userGhost.getTime()));
        } else {
          personalLapTimeLabel.setText(languageBundle.getString("CIRCUITPERSONALLAPTIME")
              + "[00:00.000] -- No time found");
        }

        Ghost fastestGhost = new GhostRestController().loadFastestGhost(currentCircuit.getId());
        if (fastestGhost != null) {
          fastestLapTimeLabel.setText(languageBundle.getString("CIRCUITFASTESTLAPTIME")
              + Utility.timeFormat(fastestGhost.getTime()));
        } else {
          fastestLapTimeLabel.setText(languageBundle.getString("CIRCUITFASTESTLAPTIME")
              + "[00:00.000] -- No time found");
        }

        previewPanel.loadPreview(currentCircuit.getId());
        previewPanel.repaint();

        fillGhostList(currentCircuit.getId());
      }
    });

    ghostList.addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent ae) {
        if (ghostList.getSelectedValuesList().size() > 0) {
          startReplayButton.setEnabled(true);
        } else {
          startReplayButton.setEnabled(false);
        }
      }
    });
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.startReplayButton) {
      Circuit currentCircuit = (Circuit) circuitList.getSelectedValue();
      logger.info("Selected ghosts size: " + ghostList.getSelectedValuesList().size());
      List<Ghost> ghosts = ghostList.getSelectedValuesList();
      Circuit circuit = new CircuitRestController().loadCircuit(currentCircuit.getId());
      this.window.showReplayGamePanel(circuit, ghosts);
    } else if (ae.getSource() == this.backButton) {
      this.window.showStartMenu();
    } else {

    }

  }

}
