package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import be.tiwi.vop.racing.desktop.view.Internationalization;

public class CountDownTimerDialog extends JDialog implements Internationalization {
  private ResourceBundle languageBundle;

  private int count;

  private JLabel message;

  public CountDownTimerDialog(JFrame parent, boolean modal, int seconds) {
    super(parent, modal);
    setUndecorated(true);
    this.languageBundle = ResourceBundle.getBundle("languages/language");
    this.count = seconds;

    final JLabel countLabel = new JLabel(String.valueOf(seconds), JLabel.CENTER);
    countLabel.setFont(new Font("verdana", Font.PLAIN, 36));
    this.message = new JLabel(this.languageBundle.getString("COUNTDOWNMESSAGE"));
    this.message.setFont(new Font("verdana", Font.BOLD, 20));

    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
    wrapper.add(this.message, BorderLayout.NORTH);
    wrapper.add(countLabel);
    add(wrapper);

    Timer timer = new Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (count == 0) {
          dispose();
        } else {
          countLabel.setText(String.valueOf(count));
          count--;
        }
      }
    });
    timer.setInitialDelay(0);
    timer.start();

    pack();
    setLocation(parent.getWidth() / 2 - this.getWidth() / 2, this.getHeight() / 2);
    // setLocationByPlatform(true);
    // setLocationRelativeTo(parent);
    setVisible(true);
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.message.setText(this.languageBundle.getString("COUNTDOWNMESSAGE"));
  }
}
