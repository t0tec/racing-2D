package be.tiwi.vop.racing.desktop.model.user;

import be.tiwi.vop.racing.desktop.restclient.BasicAuthFeature;

public class AuthenticatedUser extends User {
  private BasicAuthFeature basicAuth;

  private volatile static AuthenticatedUser instance;

  public static AuthenticatedUser getInstance() {
    if (instance == null) {
      synchronized (AuthenticatedUser.class) {
        instance = new AuthenticatedUser();
      }
    }
    return instance;
  }

  private AuthenticatedUser() {
    super();
  }

  public static void resetInstance() {
    instance = null;
  }

  public void setBasicAuth(BasicAuthFeature basicAuth) {
    this.basicAuth = basicAuth;
  }

  public BasicAuthFeature getBasicAuth() {
    return this.basicAuth;
  }

}
