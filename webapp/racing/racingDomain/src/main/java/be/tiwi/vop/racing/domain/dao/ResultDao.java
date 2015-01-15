package be.tiwi.vop.racing.domain.dao;

import java.util.List;

import be.tiwi.vop.racing.core.model.Result;

public interface ResultDao {
  List<Result> getResultsByRaceIdAndUserId(int raceId, int userId);

  List<Result> getResultsByRaceId(int raceId);

  void insertResult(Result result);

  void insertResults(List<Result> results);
}
