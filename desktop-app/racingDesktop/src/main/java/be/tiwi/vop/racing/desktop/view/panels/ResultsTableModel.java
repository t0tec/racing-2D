package be.tiwi.vop.racing.desktop.view.panels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import be.tiwi.vop.racing.desktop.model.tournament.Result;
import be.tiwi.vop.racing.desktop.util.Utility;

public class ResultsTableModel extends AbstractTableModel {

  public static final int LAP_NR_COLUMN = 0;
  public static final int LAP_TIME_COLUMN = 1;
  public static final int RACE_ID_COLUMN = 2;
  public static final int USER_ID_COLUMN = 3;

  public static final int COLUMN_COUNT = 4;

  private final List<Result> results = new ArrayList<Result>();

  public void add(List<Result> results) {
    for (Result r : results) {
      if (results.contains(r)) {
        add(r);
      }
    }
  }

  public void add(Result result) {
    int index = results.size();
    results.add(result);
    fireTableRowsInserted(index, index);
  }

  public Result getResult(int row) {
    return results.get(row);
  }

  @Override
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Override
  public int getRowCount() {
    return this.results.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Result tournament = results.get(rowIndex);

    switch (columnIndex) {
      case LAP_NR_COLUMN:
        return tournament.getLapNumber();
      case LAP_TIME_COLUMN:
        return Utility.timeFormat(tournament.getTime());
      case RACE_ID_COLUMN:
        return tournament.getRaceId();
      case USER_ID_COLUMN:
        return tournament.getUserId();
    }

    return null;
  }

  public void clearResults() {
    this.results.clear();
  }

}
