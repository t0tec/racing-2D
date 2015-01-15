package be.tiwi.vop.racing.dao;

import be.tiwi.vop.racing.core.model.Event;
import java.util.List;

public interface EventDao {
  List<Event> getEventsByUserId(int userId, int limit1, int limit2);

  void insertEvent(Event e);
}
