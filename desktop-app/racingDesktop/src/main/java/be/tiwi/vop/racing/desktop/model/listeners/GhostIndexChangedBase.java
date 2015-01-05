package be.tiwi.vop.racing.desktop.model.listeners;

public interface GhostIndexChangedBase {
  void hasGhostIndexChanged();

  void addGhostIndexChangedListener(GhostIndexChangedListener listener);

  void removeGhostIndexChangedListener(GhostIndexChangedListener listener);
}
