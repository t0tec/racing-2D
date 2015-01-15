package be.tiwi.vop.racing.domain;

public interface DaoCommand {
  public Object execute(DaoManager daoManager);
}
