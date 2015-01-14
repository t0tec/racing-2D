package be.tiwi.vop.racing.model;

public class User {

  private Integer id;
  private String username;
  private String password;
  private String email;
  private String fullName;
  private boolean isPublic;

  public User() {}

  public User(String username, String email, String fullName, boolean isPublic) {
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.isPublic = isPublic;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }
}
