package be.tiwi.vop.racing.desktop.view;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MainJFrame extends JFrame {
  private Dimension prefferedDimension = new Dimension(1024, 768);

  public MainJFrame() {
    super("racing-2D");
    Image icon = null;
    URL url = this.getClass().getClassLoader().getResource("icon/icon.png");
    try {
      icon = ImageIO.read(url);
    } catch (IOException ioExc) {
      ioExc.printStackTrace();
    }
    setIconImage(icon);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(prefferedDimension);
    pack();
    setVisible(true);
  }
}
