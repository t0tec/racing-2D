package be.tiwi.vop.racing.desktop.view.panels;

import java.util.Locale;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.timing.Pause;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.view.MainWindow;

public class SettingsPanelTest {
  private static final Logger logger = LoggerFactory.getLogger(SettingsPanelTest.class);

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
  public void shouldVerifyLanguangeChangeTest() {
    logger.info("Should verify if language can be changed");
    window.panel("startMenuPanel").button("settingsButton").click();
    window.panel("settingsPanel").comboBox("comboBox").selectItem(3);
    window.panel("settingsPanel").button("saveButton").click();

    Assert.assertEquals(Locale.FRENCH, Locale.getDefault());

    Pause.pause(3000);
    window.panel("settingsPanel").comboBox("comboBox").selectItem(2);
    window.panel("settingsPanel").button("saveButton").click();

    Assert.assertEquals(new Locale("nl"), Locale.getDefault());

    Pause.pause(3000);
    window.panel("settingsPanel").comboBox("comboBox").selectItem(1);
    window.panel("settingsPanel").button("saveButton").click();

    Assert.assertEquals(Locale.ENGLISH, Locale.getDefault());

    window.panel("settingsPanel").comboBox("comboBox").selectItem(0);
    window.panel("settingsPanel").button("saveButton").click();
  }
}
