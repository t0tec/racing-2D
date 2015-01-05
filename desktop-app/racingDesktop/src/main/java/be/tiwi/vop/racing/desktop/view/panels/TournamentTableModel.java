package be.tiwi.vop.racing.desktop.view.panels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import be.tiwi.vop.racing.desktop.model.tournament.Tournament;

public class TournamentTableModel extends AbstractTableModel {
  public static final int NAME_COLUMN = 0;
  public static final int ORGANISER_COLUMN = 1;
  public static final int FORMULE_COLUMN = 2;
  public static final int MAX_PLAYERS_COLUMN = 3;
  public static final int DATE_COLUMN = 4;

  public static final int COLUMN_COUNT = 5;

  private final List<Tournament> tournaments = new ArrayList<Tournament>();

  public void add(List<Tournament> tournaments) {
    this.tournaments.addAll(tournaments);
  }

  public void add(Tournament tournament) {
    int index = tournaments.size();
    tournaments.add(tournament);
    fireTableRowsInserted(index, index);
  }

  public Tournament getTournament(int row) {
    return tournaments.get(row);
  }

  @Override
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public int getRowCount() {
    return this.tournaments.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Tournament tournament = tournaments.get(rowIndex);

    switch (columnIndex) {
      case NAME_COLUMN:
        return tournament.getName();
      case ORGANISER_COLUMN:
        return tournament.getUserId();
      case FORMULE_COLUMN:
        return tournament.getFormule();
      case MAX_PLAYERS_COLUMN:
        return tournament.getMaxPlayers();
      case DATE_COLUMN:
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        return dateFormat.format(tournament.getDate());
    }

    return null;
  }

}
