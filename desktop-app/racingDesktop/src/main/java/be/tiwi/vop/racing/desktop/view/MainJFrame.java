package be.tiwi.vop.racing.desktop.view;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MainJFrame extends JFrame {
  private static final Dimension preferedDimension = new Dimension(1024, 768);

  public MainJFrame() {
    super("racing-2D");

    try {
      URL url = this.getClass().getClassLoader().getResource("icon/icon.png");
      Image icon = ImageIO.read(url);
      setIconImage(icon);
    } catch (IOException ioExc) {
      ioExc.printStackTrace();
    }

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(preferedDimension);
    pack();
    setVisible(true);
  }
}
