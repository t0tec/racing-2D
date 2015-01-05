package be.tiwi.vop.racing.desktop.view;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainJFrame extends JFrame {
  private Dimension prefferedDimension = new Dimension(1024, 768);

  public MainJFrame() {
    super("racing-2D");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(prefferedDimension);
    pack();
    setVisible(true);
  }
}
