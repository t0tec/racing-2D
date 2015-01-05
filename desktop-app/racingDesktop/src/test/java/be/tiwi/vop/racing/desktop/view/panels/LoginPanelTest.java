package be.tiwi.vop.racing.desktop.view.panels;

import org.junit.Assert;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class LoginPanelTest {
  private static final Logger logger = LoggerFactory.getLogger(LoginPanelTest.class);

  private FrameFixture window;

  @BeforeClass
  public static void setUpOnce() {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp() {
    MainWindow frame = GuiActionRunner.execute(new GuiQuery<MainWindow>() {
      @Override
      protected MainWindow executeInEDT() {
        MainWindow window = new MainWindow();
        window.showLoginPanel();
        return window;
      }
    });
    window = new FrameFixture(frame);
    window.show();
  }

  @After
  public void tearDown() {
    window.cleanUp();
  }

  @Test
  public void shouldVerifyLoginIfLoginButtonIsPressed() {
    logger.info("Starting login successfully authenticated test");
    window.panel("loginPanel").textBox("usernameTextField").deleteText();
    window.panel("loginPanel").textBox("passwordTextField").deleteText();
    window.panel("loginPanel").textBox("usernameTextField").enterText("t0tec");
    window.panel("loginPanel").textBox("passwordTextField").enterText("t0tec");
    window.panel("loginPanel").button("loginButton").click();

    Assert.assertNotNull(AuthenticatedUser.getInstance());
  }

  @Test
  public void shouldShowErrorIfUsernameAndPasswordIsMissing() {
    logger.info("Starting login username & password missing test");
    window.panel("loginPanel").textBox("usernameTextField").deleteText();
    window.panel("loginPanel").textBox("passwordTextField").deleteText();
    window.panel("loginPanel").button("loginButton").click();
    window.panel("loginPanel").panel("formPanel").label("usernameErrorLabel")
        .requireText("Please fill in your username!");
    window.panel("loginPanel").label("passwordErrorLabel")
        .requireText("Please fill in your password!");
  }

  @Test
  public void shouldShowErrorDialogIfCredentialsAreWrong() {
    logger.info("Starting login wrong credentials test");
    window.panel("loginPanel").textBox("usernameTextField").deleteText();
    window.panel("loginPanel").textBox("passwordTextField").deleteText();
    window.panel("loginPanel").textBox("usernameTextField").enterText("idontknow");
    window.panel("loginPanel").textBox("passwordTextField").enterText("idontknow");
    window.panel("loginPanel").button("loginButton").click();
    window.optionPane().requireMessage("Your user credentials are wrong. Please try again.")
        .requireTitle("Login failed");
  }
}
