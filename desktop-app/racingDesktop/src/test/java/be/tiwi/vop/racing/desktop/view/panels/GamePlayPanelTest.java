package be.tiwi.vop.racing.desktop.view.panels;

import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

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

import be.tiwi.vop.racing.desktop.view.MainWindow;

public class GamePlayPanelTest {
  private static final Logger logger = LoggerFactory.getLogger(GamePlayPanelTest.class);

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
  public void shouldVerifyIfGamePlayWorks() throws InterruptedException {
    logger.info("Starting game test");
    window.panel("startMenuPanel").button("startNewGameButton").click();
    window.panel("carMenuPanel").button("selectNewRaceCarButton").click();
    window.panel("gameMenuPanel").list("circuitList").selectItem(0);
    window.panel("gameMenuPanel").button("startGameButton").click();

    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").releaseKey(VK_RIGHT);

    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").releaseKey(VK_RIGHT);

    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_RIGHT);
    window.panel("gamePlayPanel").panel("gamePanel").releaseKey(VK_RIGHT);

    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").pressKey(VK_UP);
    window.panel("gamePlayPanel").panel("gamePanel").releaseKey(VK_UP);

  }
}
