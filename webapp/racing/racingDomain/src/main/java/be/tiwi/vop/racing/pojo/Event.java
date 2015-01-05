package be.tiwi.vop.racing.pojo;

import java.util.Date;

public class Event {

  private Integer userId, id;
  private String action;
  private Date timestamp;

  public Event() {
    // Explicit public default constructor
  }

  public Event(int userid, String act) {
    this.action = act;
    this.userId = userid;
    this.timestamp = new Date();
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date date) {
    this.timestamp = date;
  }
}
