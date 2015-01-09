package be.tiwi.vop.racing.desktop;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.restcontroller.RestServerHostAvailableTest;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class Start {
  private static final Logger logger = LoggerFactory.getLogger(Start.class);

  public static void main(String[] args) {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new Start();
        logger.info("Started the application!");
        if (!RestServerHostAvailableTest.hostAvailabilityCheck()) {
          JOptionPane.showMessageDialog(null, "REST server at " + RestServerHostAvailableTest.HOST
              + " is offline or there is an internal server error!", "REST server offline",
              JOptionPane.WARNING_MESSAGE);
        }

        new MainWindow().showLoginPanel();
      }
    });
  }

}
