package be.tiwi.vop.racing.desktop.model.tournament;

import java.util.Date;

public class Tournament {
  private int id, userId, maxPlayers;
  private Formule formule;
  private String name;
  private Date date;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public Formule getFormule() {
    return formule;
  }

  public void setFormule(Formule formule) {
    this.formule = formule;
  }

}
