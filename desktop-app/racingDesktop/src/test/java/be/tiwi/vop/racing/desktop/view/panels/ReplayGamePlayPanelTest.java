package be.tiwi.vop.racing.desktop.view.panels;

import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.TypeMatcher;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.timing.Condition;
import org.fest.swing.timing.Pause;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.view.MainWindow;
import be.tiwi.vop.racing.desktop.view.panels.GameMenuDialog;

public class ReplayGamePlayPanelTest {
  private static final Logger logger = LoggerFactory.getLogger(ReplayGamePlayPanelTest.class);

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
  public void shouldVerifyIfReplayWorks() throws InterruptedException {
    logger.info("Starting replay test");
    window.panel("startMenuPanel").button("watchReplayButton").click();
    window.panel("replayMenuPanel").list("circuitList").selectItem(0);

    window.panel("replayMenuPanel").list("ghostList").selectItem(0);
    window.panel("replayMenuPanel").button("startReplayButton").click();

    // http://stackoverflow.com/questions/8957334/making-fests-component-lookup-mechanism-wait-for-a-component-to-exist?lq=1
    final ComponentMatcher matcher = new TypeMatcher(GameMenuDialog.class);
    Pause.pause(new Condition("Waiting for my end replay dialog to show up...") {
      @Override
      public boolean test() {
        return !window.robot.finder().findAll(window.target, matcher).isEmpty();

      }
    }, 100000);
  }

  @Test
  @Ignore("FIX STRANGE TEST ERROR (Next test=shouldVerifiyIfQuitReplayWorks fails after this test has completed)")
  public void shouldVerifiyIfPauseResumeReplayWorks() {
    logger.info("Starting replay pause resume test");
    window.panel("startMenuPanel").button("watchReplayButton").click();
    window.panel("replayMenuPanel").list("circuitList").selectItem(0);
    window.panel("replayMenuPanel").list("ghostList").selectItem(0);
    window.panel("replayMenuPanel").button("startReplayButton").click();

    Pause.pause(1500);
    window.panel("replayGamePlayPanel").button("pauseReplayButton").click();
    Pause.pause(2000);
    window.dialog("gameMenuDialog").button("resumeReplayButton").click();
  }

  @Test
  public void shouldVerifiyIfRestartReplayWorks() {
    logger.info("Starting replay pause restart test");
    window.panel("startMenuPanel").button("watchReplayButton").click();
    window.panel("replayMenuPanel").list("circuitList").selectItem(0);
    window.panel("replayMenuPanel").list("ghostList").selectItem(0);
    window.panel("replayMenuPanel").button("startReplayButton").click();

    Pause.pause(1500);
    window.panel("replayGamePlayPanel").button("pauseReplayButton").click();
    Pause.pause(2000);
    window.dialog("gameMenuDialog").button("restartReplayButton").click();
  }

  @Test
  public void shouldVerifiyIfQuitReplayWorks() {
    logger.info("Starting replay pause quit test");
    window.panel("startMenuPanel").button("watchReplayButton").click();
    window.panel("replayMenuPanel").list("circuitList").selectItem(0);
    window.panel("replayMenuPanel").list("ghostList").selectItem(0);
    window.panel("replayMenuPanel").button("startReplayButton").click();

    Pause.pause(1500);
    window.panel("replayGamePlayPanel").button("pauseReplayButton").click();
    Pause.pause(2000);
    window.dialog("gameMenuDialog").button("quitReplayButton").click();
  }

  @Test
  public void shouldVerifiyIfMultipleGhostSwitchingWorks() {
    logger.info("Starting replay pause quit test");
    window.panel("startMenuPanel").button("watchReplayButton").click();
    window.panel("replayMenuPanel").list("circuitList").selectItem(0);

    // if
    // (window.panel("replayMenuPanel").list("ghostList").target.getSelectionModel().getMaxSelectionIndex()
    // >= 1) {
    // window.panel("replayMenuPanel").list("ghostList").selectItems(0, 1);
    // window.panel("replayMenuPanel").button("startReplayButton").click();
    // window.panel("replayInfoPanel").button("nextGhostBtn").click();
    // Pause.pause(1500);
    // window.panel("replayInfoPanel").button("previousGhostBtn").click();

    // } else {
    // window.panel("replayMenuPanel").list("ghostList").selectItems(0);
    // window.panel("replayMenuPanel").button("startReplayButton").click();
    // }

    window.panel("replayMenuPanel").list("ghostList").selectItems(0, 1);
    window.panel("replayMenuPanel").button("startReplayButton").click();

    window.panel("replayInfoPanel").button("nextGhostBtn").click();
    Pause.pause(1500);
    window.panel("replayInfoPanel").button("previousGhostBtn").click();

    final ComponentMatcher matcher = new TypeMatcher(GameMenuDialog.class);
    Pause.pause(new Condition("Waiting for my end replay dialog to show up...") {
      @Override
      public boolean test() {
        return !window.robot.finder().findAll(window.target, matcher).isEmpty();
      }
    }, 100000);
  }
}
