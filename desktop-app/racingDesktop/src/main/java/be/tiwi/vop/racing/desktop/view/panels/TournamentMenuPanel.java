package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.tournament.Race;
import be.tiwi.vop.racing.desktop.model.tournament.Result;
import be.tiwi.vop.racing.desktop.restcontroller.CircuitRestController;
import be.tiwi.vop.racing.desktop.restcontroller.TournamentRestController;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class TournamentMenuPanel extends JPanel implements ActionListener, Internationalization {
  private static final Logger logger = LoggerFactory.getLogger(TournamentMenuPanel.class);

  private ResourceBundle languageBundle;

  private MainWindow window;
  private JPanel mainPanel;
  private JPanel buttonPanel;

  private JTable tournamentTable;
  private TournamentTableModel tournamentTableModel;
  private JList<Race> raceList;
  private DefaultListModel<Race> raceModel;

  private JTable userResultsTable;
  private ResultsTableModel userResultsTableModel;

  private JTable allRaceResultsTable;
  private ResultsTableModel allRaceResultsTableModel;

  private JButton startTournamentButton;
  private JButton backButton;

  public TournamentMenuPanel(MainWindow window) {
    super(new BorderLayout());
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    this.window = window;

    setFocusable(true);
    setVisible(true);
    initLayoutPanel();
    requestFocus();

    this.setName("tournamentMenuPanel");
  }

  private void initLayoutPanel() {
    this.mainPanel = new JPanel(new GridBagLayout());
    this.buttonPanel = new JPanel();

    tournamentTableModel = new TournamentTableModel();
    this.tournamentTable = new JTable(tournamentTableModel);
    this.tournamentTable.setColumnModel(createTournamentColumnModel());
    this.tournamentTable.setAutoCreateRowSorter(true);
    this.tournamentTable.setRowHeight(26);
    this.tournamentTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    this.tournamentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fillTournamentTable();

    this.tournamentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
          return;
        }

        if (tournamentTableModel.getTournament(tournamentTable.getSelectedRow()) != null) {
          userResultsTableModel.clearResults();
          userResultsTable.setColumnModel(createUserResultsColumnModel());
          allRaceResultsTableModel.clearResults();
          allRaceResultsTable.setColumnModel(createUserResultsColumnModel());
          int tournamentId =
              tournamentTableModel.getTournament(tournamentTable.getSelectedRow()).getId();
          fillRaceList(tournamentId);
        }
      }
    });

    Dimension viewSize = new Dimension(800, 5 * this.tournamentTable.getRowHeight());
    this.tournamentTable.setPreferredScrollableViewportSize(viewSize);

    JScrollPane scrollPane = new JScrollPane(this.tournamentTable);
    addToMainPanel(new JLabel(this.languageBundle.getString("TOURNAMENTS")), 0, 0);
    addToMainPanel(scrollPane, 1, 0);

    raceList = new JList<Race>();
    raceList.setName("raceList");
    raceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    raceList.setVisibleRowCount(5);

    raceList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent lse) {
        if (!lse.getValueIsAdjusting() && !raceList.isSelectionEmpty()) {
          return;
        }
        if (raceList.getSelectedValuesList().size() > 0) {
          startTournamentButton.setEnabled(true);

          List<Result> userResults =
              new TournamentRestController().getRaceResultsFromCurrentUser(tournamentTableModel
                  .getTournament(tournamentTable.getSelectedRow()).getId(), raceList
                  .getSelectedValue().getId());

          fillUserResultsTable(userResults);

          List<Result> raceResults =
              new TournamentRestController().getRaceResults(
                  tournamentTableModel.getTournament(tournamentTable.getSelectedRow()).getId(),
                  raceList.getSelectedValue().getId());

          fillAllRaceResultsTable(raceResults);

          if (userResults.size() > 0) {
            startTournamentButton.setEnabled(false);
          }

        } else {
          startTournamentButton.setEnabled(false);
        }
      }
    });

    scrollPane = new JScrollPane(raceList);

    addToMainPanel(new JLabel(this.languageBundle.getString("RACES")), 0, 1);
    addToMainPanel(scrollPane, 1, 1);

    userResultsTableModel = new ResultsTableModel();
    this.userResultsTable = new JTable(userResultsTableModel);
    this.userResultsTable.setColumnModel(createUserResultsColumnModel());
    this.userResultsTable.setRowHeight(26);
    this.userResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    this.userResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    viewSize = new Dimension(400, 5 * this.userResultsTable.getRowHeight());
    this.userResultsTable.setPreferredScrollableViewportSize(viewSize);

    scrollPane = new JScrollPane(this.userResultsTable);
    addToMainPanel(new JLabel(this.languageBundle.getString("YOURRESULTS")), 2, 0);
    addToMainPanel(scrollPane, 3, 0);

    allRaceResultsTableModel = new ResultsTableModel();
    this.allRaceResultsTable = new JTable(allRaceResultsTableModel);
    this.allRaceResultsTable.setColumnModel(createUserResultsColumnModel());
    this.allRaceResultsTable.setRowHeight(26);
    this.allRaceResultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    this.allRaceResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    viewSize = new Dimension(400, 5 * this.allRaceResultsTable.getRowHeight());
    this.allRaceResultsTable.setPreferredScrollableViewportSize(viewSize);

    scrollPane = new JScrollPane(this.allRaceResultsTable);
    addToMainPanel(new JLabel(this.languageBundle.getString("RACERESULTS")), 2, 1);
    addToMainPanel(scrollPane, 3, 1);

    buttonPanel = new JPanel(new GridBagLayout());
    startTournamentButton = new JButton(this.languageBundle.getString("SELECTRACE"));
    startTournamentButton.setName("startGameButton");
    startTournamentButton.setEnabled(false);
    backButton = new JButton(this.languageBundle.getString("BACK"));
    backButton.setName("backButton");

    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(startTournamentButton);
    buttonList.add(backButton);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    add(this.mainPanel, BorderLayout.CENTER);
    add(this.buttonPanel, BorderLayout.SOUTH);
  }

  private void fillTournamentTable() {
    tournamentTableModel.add(new TournamentRestController().loadEnrolledTournaments());
  }

  private void fillUserResultsTable(List<Result> results) {
    userResultsTableModel.clearResults();;
    userResultsTable.setColumnModel(createUserResultsColumnModel());
    userResultsTableModel.add(results);
  }

  private void fillAllRaceResultsTable(List<Result> results) {
    allRaceResultsTableModel.clearResults();
    allRaceResultsTable.setColumnModel(createUserResultsColumnModel());
    allRaceResultsTableModel.add(results);
  }

  private TableColumnModel createTournamentColumnModel() {
    DefaultTableColumnModel columnModel = new DefaultTableColumnModel();

    TableColumn column = new TableColumn();
    column.setModelIndex(TournamentTableModel.NAME_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("TOURNMANETHEADERNAME"));
    column.setPreferredWidth(75);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(TournamentTableModel.ORGANISER_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("TOURNMANETHEADERORGANISER"));
    column.setPreferredWidth(50);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(TournamentTableModel.FORMULE_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("TOURNMANETHEADERFORMULA"));
    column.setPreferredWidth(50);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(TournamentTableModel.MAX_PLAYERS_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("TOURNAMENTHEADERMAXPLAYERS"));
    column.setPreferredWidth(25);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(TournamentTableModel.DATE_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("TOURNMANETHEADERDATE"));
    column.setPreferredWidth(95);
    columnModel.addColumn(column);

    return columnModel;
  }

  private TableColumnModel createUserResultsColumnModel() {
    DefaultTableColumnModel columnModel = new DefaultTableColumnModel();

    TableColumn column = new TableColumn();
    column.setModelIndex(ResultsTableModel.LAP_NR_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("RESULTSHEADERLAP"));
    column.setPreferredWidth(50);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(ResultsTableModel.LAP_TIME_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("RESULTSHEADERLAPTIME"));
    column.setPreferredWidth(50);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(ResultsTableModel.RACE_ID_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("RESULTSHEADERRACE"));
    column.setPreferredWidth(50);
    columnModel.addColumn(column);

    column = new TableColumn();
    column.setModelIndex(ResultsTableModel.USER_ID_COLUMN);
    column.setHeaderValue(this.languageBundle.getString("RESULTSHEADERPLAYERID"));
    column.setPreferredWidth(50);
    columnModel.addColumn(column);

    return columnModel;
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

  private void addToMainPanel(JScrollPane scrollPane, int row, int column) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = column;
    gbc.gridy = row;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 1;
    gbc.weighty = 1;
    mainPanel.add(scrollPane, gbc);
  }

  private void addToMainPanel(JLabel label, int row, int column) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 0;
    gbc.gridx = column;
    gbc.gridy = row;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    gbc.weighty = 0;
    mainPanel.add(label, gbc);
  }

  private void fillRaceList(int tournamentId) {
    raceModel = new DefaultListModel<Race>();
    for (Race circuit : new TournamentRestController().loadTournamentRaces(tournamentId)) {
      raceModel.addElement(circuit);
    }
    raceList.setModel(raceModel);
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.startTournamentButton) {
      logger.info("Selected tournament: {}",
          tournamentTableModel.getTournament(tournamentTable.getSelectedRow()).getName());

      Race race = ((Race) this.raceList.getSelectedValue());
      logger.info("Selected circuit id: {}", race.getCircuitId());

      Circuit circuit = new CircuitRestController().loadCircuit(race.getCircuitId());
      circuit.setLapCount(race.getLaps());

      this.window.showTournamentRaceMenu(circuit, race);

    } else if (ae.getSource() == this.backButton) {
      this.window.showStartMenu();
    }
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.startTournamentButton.setText(this.languageBundle.getString("SELECT"));
    this.backButton.setText(this.languageBundle.getString("BACK"));
  }
}
