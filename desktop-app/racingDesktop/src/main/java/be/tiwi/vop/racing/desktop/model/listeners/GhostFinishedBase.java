package be.tiwi.vop.racing.desktop.model.listeners;

public interface GhostFinishedBase {
  void haveGhostsFinished();

  void addGhostFinishedListener(GhostFinishedListener listener);

  void removeGhostFinishedListener(GhostFinishedListener listener);
}
