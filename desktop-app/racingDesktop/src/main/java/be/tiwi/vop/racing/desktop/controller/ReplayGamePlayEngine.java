package be.tiwi.vop.racing.desktop.controller;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;

public class ReplayGamePlayEngine extends GameEngine {

  public ReplayGamePlayEngine(Circuit circuit) {
    super(circuit);
  }

  @Override
  public void processGameEngineTick(int time) {
    this.time = time;
    haveGhostsFinishedFlag();
  }

}
