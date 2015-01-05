package be.tiwi.vop.racing.desktop.model.listeners;

public interface LapCompletedBase {
  void hasLapsCompleted();

  void addLapCompletedListener(LapCompletedListener listener);

  void removeLapCompletedListener(LapCompletedListener listener);
}
