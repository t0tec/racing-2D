package be.tiwi.vop.racing.desktop.view.panels;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.timing.Pause;
// import org.fest.swing.fixture.JComboBoxFixture;
// import org.fest.swing.security.NoExitSecurityManagerInstaller;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.user.AuthenticatedUser;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class StartMenuPanelTest {
  private static final Logger logger = LoggerFactory.getLogger(StartMenuPanelTest.class);

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
    window.panel("loginPanel").textBox("usernameTextField").setText("t0tec");
    window.panel("loginPanel").textBox("passwordTextField").setText("t0tec");
    window.panel("loginPanel").button("loginButton").click();
    window.show();
  }

  @After
  public void tearDown() {
    window.cleanUp();
  }

  @Test
  public void shouldVerifyIfCarMenuWorks() {
    logger.info("Starting game menu test");
    window.panel("startMenuPanel").button("startNewGameButton").click();

    window.panel("carMenuPanel").button("nextButton").click();

    Pause.pause(1500);
    window.panel("carMenuPanel").button("previousButton").click();

    window.panel("carMenuPanel").button("selectNewRaceCarButton").click();
  }

  @Test
  public void shouldVerifyIfGameMenuWorks() {
    logger.info("Starting game menu test");
    window.panel("startMenuPanel").button("startNewGameButton").click();
  }

  @Test
  public void shouldVerifyIfReplayMenuWorks() {
    logger.info("Starting replay menu test");
    window.panel("startMenuPanel").button("watchReplayButton").click();
  }


  @Test
  public void shouldVerifyIfLogoutWorks() {
    logger.info("Starting logout test");

    Assert.assertNotNull(AuthenticatedUser.getInstance());

    window.panel("startMenuPanel").button("logoutButton").click();

    Assert.assertNull(AuthenticatedUser.getInstance().getId());
  }

  @Test
  public void shouldVerifyIfSettingsMenuWorks() {
    logger.info("Starting settings menu test");
    window.panel("startMenuPanel").button("settingsButton").click();
  }

  @Test
  @Ignore("We don't want to end the tests do we?")
  public void shouldVerifyIfExitApplicationWorks() {
    logger.info("Starting exit application test");
    window.panel("startMenuPanel").button("exitButton").click();
  }

}
