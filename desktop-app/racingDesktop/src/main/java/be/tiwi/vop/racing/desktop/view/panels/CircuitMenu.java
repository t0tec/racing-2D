package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.ghost.Ghost;
import be.tiwi.vop.racing.desktop.restcontroller.CircuitRestController;
import be.tiwi.vop.racing.desktop.restcontroller.GhostRestController;
import be.tiwi.vop.racing.desktop.view.Internationalization;

public abstract class CircuitMenu extends JPanel implements ActionListener, Internationalization {
  protected ResourceBundle languageBundle;

  protected JPanel mainPanel;
  protected JPanel circuitSelectionPanel;
  protected JLabel circuitPanelTitleLabel;
  protected JPanel informationPanel;
  protected JLabel infoTitleLabel;
  protected PreviewPanel previewPanel;
  protected JLabel circuitNameLabel;
  protected JLabel designerNameLabel;
  protected JLabel personalLapTimeLabel;
  protected JLabel fastestLapTimeLabel;
  protected JPanel ghostSelectionPanel;
  protected JLabel ghostPanelTitleLabel;
  protected JPanel buttonPanel;

  protected JList<Circuit> circuitList;
  protected JList<Ghost> ghostList;
  protected DefaultListModel<Circuit> circuitModel;
  protected DefaultListModel<Ghost> ghostModel;
  protected JButton startGameButton;
  protected JButton startReplayButton;
  protected JButton backButton;

  public CircuitMenu() {
    super(new BorderLayout());
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    setFocusable(true);
    setVisible(true);
    initLayoutPanel();
    requestFocus();

    this.setName("abstractCircuitMenu");
  }

  private void initLayoutPanel() {
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
    mainPanel.add(Box.createRigidArea(new Dimension(10, 1)));

    circuitSelectionPanel = new JPanel();
    circuitSelectionPanel.setLayout(new BoxLayout(circuitSelectionPanel, BoxLayout.Y_AXIS));
    circuitSelectionPanel.add(Box.createRigidArea(new Dimension(1, 10)));
    circuitPanelTitleLabel = new JLabel(this.languageBundle.getString("CIRCUITCHOOSETITLE"));
    circuitPanelTitleLabel.setFont(new Font("verdana", Font.BOLD, 20));
    circuitSelectionPanel.add(circuitPanelTitleLabel);
    circuitSelectionPanel.add(Box.createRigidArea(new Dimension(1, 10)));

    previewPanel = new PreviewPanel();
    circuitNameLabel = new JLabel(this.languageBundle.getString("CIRCUITNAME"));
    designerNameLabel = new JLabel(this.languageBundle.getString("CIRCUITDESIGNERNAME"));
    personalLapTimeLabel = new JLabel(this.languageBundle.getString("CIRCUITPERSONALLAPTIME"));
    fastestLapTimeLabel = new JLabel(this.languageBundle.getString("CIRCUITFASTESTLAPTIME"));

    circuitList = new JList<Circuit>();
    circuitList.setName("circuitList");
    fillCircuitList();
    circuitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    circuitList.setVisibleRowCount(25);

    JScrollPane scrollPane = new JScrollPane(circuitList);
    circuitSelectionPanel.add(scrollPane);
    circuitSelectionPanel.add(Box.createRigidArea(new Dimension(1, 10)));


    informationPanel = new JPanel();
    informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));
    informationPanel.add(Box.createRigidArea(new Dimension(1, 10)));
    infoTitleLabel = new JLabel(this.languageBundle.getString("CIRCUITINFOTITLE"));
    infoTitleLabel.setFont(new Font("verdana", Font.BOLD, 20));
    informationPanel.add(infoTitleLabel);
    informationPanel.add(Box.createRigidArea(new Dimension(1, 10)));

    informationPanel.add(circuitNameLabel);
    informationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    informationPanel.add(designerNameLabel);
    informationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    informationPanel.add(personalLapTimeLabel);
    informationPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    informationPanel.add(fastestLapTimeLabel);
    informationPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    informationPanel.add(previewPanel);


    ghostSelectionPanel = new JPanel();
    ghostSelectionPanel.setLayout(new BoxLayout(ghostSelectionPanel, BoxLayout.Y_AXIS));
    ghostSelectionPanel.add(Box.createRigidArea(new Dimension(1, 10)));
    ghostPanelTitleLabel = new JLabel(this.languageBundle.getString("CIRCUITCHOOSEGHOSTTITLE"));
    ghostPanelTitleLabel.setFont(new Font("verdana", Font.BOLD, 20));
    ghostSelectionPanel.add(ghostPanelTitleLabel);
    ghostSelectionPanel.add(Box.createRigidArea(new Dimension(1, 10)));
    ghostList = new JList<Ghost>();
    ghostList.setName("ghostList");
    scrollPane = new JScrollPane(ghostList);
    ghostSelectionPanel.add(scrollPane);
    ghostSelectionPanel.add(Box.createRigidArea(new Dimension(1, 10)));

    buttonPanel = new JPanel(new GridBagLayout());
    startGameButton = new JButton(this.languageBundle.getString("STARTGAME"));
    startGameButton.setName("startGameButton");
    startGameButton.setEnabled(false);
    startReplayButton = new JButton(this.languageBundle.getString("STARTREPLAY"));
    startReplayButton.setName("startReplayButton");
    startReplayButton.setEnabled(false);
    backButton = new JButton(this.languageBundle.getString("BACK"));
    backButton.setName("backButton");

    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(startGameButton);
    buttonList.add(startReplayButton);
    buttonList.add(backButton);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    mainPanel.add(circuitSelectionPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(15, 1)));
    mainPanel.add(informationPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(15, 1)));
    mainPanel.add(ghostSelectionPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(15, 1)));

    add(mainPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  protected void fillCircuitList() {
    circuitModel = new DefaultListModel<Circuit>();
    for (Circuit circuit : new CircuitRestController().loadCircuits()) {
      circuitModel.addElement(circuit);
    }
    circuitList.setModel(circuitModel);
  }

  protected void fillGhostList(int circuitId) {
    ghostModel = new DefaultListModel<Ghost>();
    for (Ghost ghost : new GhostRestController().loadGhosts(circuitId)) {
      ghostModel.addElement(ghost);
    }
    ghostList.setModel(ghostModel);
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
  public abstract void actionPerformed(ActionEvent ae);

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.startGameButton.setText(this.languageBundle.getString("STARTGAME"));
    this.startReplayButton.setText(this.languageBundle.getString("STARTREPLAY"));
    this.backButton.setText(this.languageBundle.getString("BACK"));
    this.circuitPanelTitleLabel.setText(this.languageBundle.getString("CIRCUITCHOOSETITLE"));
    this.infoTitleLabel.setText(this.languageBundle.getString("CIRCUITINFOTITLE"));
    this.circuitNameLabel.setText(this.languageBundle.getString("CIRCUITNAME"));
    this.designerNameLabel.setText(this.languageBundle.getString("CIRCUITDESIGNERNAME"));
    this.personalLapTimeLabel.setText(this.languageBundle.getString("CIRCUITPERSONALLAPTIME"));
    this.fastestLapTimeLabel.setText(this.languageBundle.getString("CIRCUITFASTESTLAPTIME"));
    this.ghostPanelTitleLabel.setText(this.languageBundle.getString("CIRCUITCHOOSEGHOSTTITLE"));
  }
}
