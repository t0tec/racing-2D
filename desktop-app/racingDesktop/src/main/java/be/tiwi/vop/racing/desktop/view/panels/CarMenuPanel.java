package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.car.factory.CarFactory;
import be.tiwi.vop.racing.desktop.model.car.handling.CarHandling;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class CarMenuPanel extends JPanel implements ActionListener, Internationalization {
  private static final Logger logger = LoggerFactory.getLogger(CarMenuPanel.class);

  private ResourceBundle languageBundle;

  public enum CarMenuState {
    RACE, TOURNAMENT
  }

  private final MainWindow window;

  private JPanel selectableCarImagePanel;
  private JPanel mainPanel;
  private JPanel carInfoPanel;
  private JPanel buttonPanel;

  private JButton previousButton;
  private JButton nextButton;
  private JLabel selectableCarImageLabel;
  private JLabel accelerationLabel;
  private JLabel brakingLabel;
  private JLabel steeringInputLabel;
  private JSlider accelerationSlider;
  private JSlider brakingSlider;
  private JSlider steeringInputSlider;

  private JButton selectNewRaceCarButton;
  private JButton selectTournamentRaceCarButton;
  private JButton backToStartMenuButton;

  private int selectedCarIndex;

  public CarMenuPanel(MainWindow window) {
    setLayout(new BorderLayout(10, 10));

    this.languageBundle = ResourceBundle.getBundle("languages/language");
    this.window = window;
    setFocusable(true);
    setVisible(true);

    this.selectedCarIndex = 0;

    configureLayout();
    requestFocus();

    Utility.assignComponentNames(this);
    this.setName("carMenuPanel");
  }

  private void configureLayout() {
    this.mainPanel = new JPanel();

    this.selectableCarImagePanel = new JPanel();
    this.selectableCarImagePanel
        .setLayout(new BoxLayout(selectableCarImagePanel, BoxLayout.X_AXIS));

    this.previousButton = new JButton(this.languageBundle.getString("PREVIOUS"));
    this.previousButton.addActionListener(this);
    this.nextButton = new JButton(this.languageBundle.getString("NEXT"));
    this.nextButton.addActionListener(this);
    this.selectableCarImageLabel = new JLabel();
    this.selectableCarImagePanel.add(this.previousButton);
    this.selectableCarImagePanel.add(Box.createRigidArea(new Dimension(300, 300)));
    this.selectableCarImagePanel.add(this.selectableCarImageLabel);
    this.selectableCarImagePanel.add(Box.createRigidArea(new Dimension(300, 300)));
    this.selectableCarImagePanel.add(this.nextButton);

    mainPanel.add(this.selectableCarImagePanel, BorderLayout.CENTER);

    this.carInfoPanel = new JPanel(new GridBagLayout());
    this.accelerationSlider = new JSlider(0, 400);
    this.brakingSlider = new JSlider(0, 1100);
    this.steeringInputSlider = new JSlider(20, 60);

    this.accelerationSlider.setEnabled(false);
    this.brakingSlider.setEnabled(false);
    this.steeringInputSlider.setEnabled(false);

    this.accelerationLabel = new JLabel(this.languageBundle.getString("CARACCELERATION"));
    this.brakingLabel = new JLabel(this.languageBundle.getString("CARBRAKING"));
    this.steeringInputLabel = new JLabel(this.languageBundle.getString("CARSTEERINGINPUT"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridy = 1;
    this.carInfoPanel.add(this.accelerationLabel, gbc);
    gbc.gridy = 2;
    this.carInfoPanel.add(this.brakingLabel, gbc);
    gbc.gridy = 3;
    this.carInfoPanel.add(this.steeringInputLabel, gbc);
    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.gridy = 1;
    this.carInfoPanel.add(this.accelerationSlider, gbc);
    gbc.gridy = 2;
    this.carInfoPanel.add(this.brakingSlider, gbc);
    gbc.gridy = 3;
    this.carInfoPanel.add(this.steeringInputSlider, gbc);

    mainPanel.add(this.carInfoPanel, BorderLayout.SOUTH);

    changeToSelectedCar(this.selectedCarIndex);

    this.backToStartMenuButton = new JButton(this.languageBundle.getString("BACK"));
    this.selectNewRaceCarButton = new JButton(this.languageBundle.getString("SELECT"));
    this.selectTournamentRaceCarButton = new JButton(this.languageBundle.getString("SELECT"));

    this.buttonPanel = new JPanel(new GridBagLayout());
    ArrayList<JButton> buttonList = new ArrayList<JButton>();
    buttonList.add(selectNewRaceCarButton);
    buttonList.add(selectTournamentRaceCarButton);
    buttonList.add(backToStartMenuButton);

    for (int i = 0; i < buttonList.size(); i++) {
      addButton(buttonList.get(i), i);
      buttonList.get(i).addActionListener(this);
    }

    add(this.mainPanel, BorderLayout.CENTER);

    add(this.buttonPanel, BorderLayout.SOUTH);
  }

  protected void addButton(JButton button, int column) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = column;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    buttonPanel.add(button, gbc);
  }

  private void changeToSelectedCar(int indexCar) {
    if ((indexCar >= 0) && (indexCar < CarFactory.getCars().size())) {
      try {
        BufferedImage img = null;
        URL url =
            this.getClass().getClassLoader()
                .getResource(((Car) CarFactory.getCars().get(indexCar)).getCarImageLocation());
        img = ImageIO.read(url);
        this.selectableCarImageLabel.setIcon(new ImageIcon(img));
      } catch (IOException ex) {
        logger.error(ex.getMessage());
      }

      this.accelerationSlider.setValue((int) ((CarHandling) (CarFactory.getCars().get(indexCar))
          .getCarHandling()).getAcceleration());
      this.brakingSlider.setValue((int) -((CarHandling) (CarFactory.getCars().get(indexCar))
          .getCarHandling()).getBraking());
      this.steeringInputSlider.setValue((int) Math.ceil(((CarHandling) (CarFactory.getCars()
          .get(indexCar)).getCarHandling()).getSteeringInput() * 100));
    }

    if (this.selectedCarIndex >= CarFactory.getCars().size() - 1) {
      this.nextButton.setEnabled(false);
    } else if (this.selectedCarIndex <= 0) {
      this.previousButton.setEnabled(false);
    } else {
      this.previousButton.setEnabled(true);
      this.nextButton.setEnabled(true);
    }
  }

  public void setCarMenuState(CarMenuState state) {
    if (state.equals(CarMenuState.RACE)) {
      this.selectNewRaceCarButton.setVisible(true);
      this.selectTournamentRaceCarButton.setVisible(false);
    } else if (state.equals(CarMenuState.TOURNAMENT)) {
      this.selectNewRaceCarButton.setVisible(false);
      this.selectTournamentRaceCarButton.setVisible(true);
    }
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.selectNewRaceCarButton) {
      CarFactory.setSelectedCarIndex(selectedCarIndex);
      logger.info("Selected car image: {}", CarFactory.getCar().getCarImageLocation());
      this.window.showStartNewGameMenu();
    } else if (ae.getSource() == this.selectTournamentRaceCarButton) {
      CarFactory.setSelectedCarIndex(selectedCarIndex);
      this.window.showTournamentMenu();
    } else if (ae.getSource() == this.backToStartMenuButton) {
      this.window.showStartMenu();
    } else if ((ae.getSource() == this.previousButton) && (this.selectedCarIndex > 0)) {
      this.selectedCarIndex -= 1;
      changeToSelectedCar(this.selectedCarIndex);
    } else if ((ae.getSource() == this.nextButton)
        && (this.selectedCarIndex < CarFactory.getCars().size() - 1)) {
      this.selectedCarIndex += 1;
      changeToSelectedCar(this.selectedCarIndex);
    }
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.previousButton.setText(this.languageBundle.getString("PREVIOUS"));
    this.nextButton.setText(this.languageBundle.getString("NEXT"));
    this.accelerationLabel.setText(this.languageBundle.getString("CARACCELERATION"));
    this.brakingLabel.setText(this.languageBundle.getString("CARBRAKING"));
    this.steeringInputLabel.setText(this.languageBundle.getString("CARSTEERINGINPUT"));
    this.backToStartMenuButton.setText(this.languageBundle.getString("BACK"));
    this.selectNewRaceCarButton.setText(this.languageBundle.getString("SELECT"));
    this.selectTournamentRaceCarButton.setText(this.languageBundle.getString("SELECT"));
  }

}
