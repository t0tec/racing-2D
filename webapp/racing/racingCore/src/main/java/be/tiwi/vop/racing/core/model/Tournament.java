package be.tiwi.vop.racing.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Tournament {

  private int id, userId, maxPlayers;
  private Formule formule;
  private String name;
  private Date date;
  private User organiser;

  public Tournament() {
    this.name = "default tournament";
  }

  public Tournament(int uId, Formule formule, String name, Date date) {
    this.formule = formule;
    this.name = name;
    this.userId = uId;
    this.date = date;
  }

  /**
   * Copy constructor
   *
   * @param t
   */
  public Tournament(Tournament t) {
    this.formule = t.formule;
    this.userId = t.userId;
    this.id = t.id;
    this.maxPlayers = t.maxPlayers;
  }

  public Formule getFormule() {
    return formule;
  }

  public void setFormule(Formule formule) {
    this.formule = formule;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public Date getDate() {
    return date;
  }

  public User getOrganiser() {
    return organiser;
  }

  public void setOrganiser(User organiser) {
    this.organiser = organiser;
  }

  public enum Formule {
    TOTAL, FASTEST, LONGEST
  }

  public int hashCode() {
    return new HashCodeBuilder(43, 67).append(id).toHashCode();
  }

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Tournament)) {
      return false;
    }

    Tournament rhs = (Tournament) obj;
    return new EqualsBuilder().append(id, rhs.id).isEquals();
  }

}
