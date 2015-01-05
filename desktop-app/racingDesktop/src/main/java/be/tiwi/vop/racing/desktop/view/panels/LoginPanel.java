package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.exceptions.UserNotFoundException;
import be.tiwi.vop.racing.desktop.restcontroller.LoginRestController;
import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class LoginPanel extends JPanel implements ActionListener, Internationalization {
  private static final Logger logger = LoggerFactory.getLogger(LoginPanel.class);

  private MainWindow window;

  private ResourceBundle languageBundle;

  private JPanel formPanel;
  private JTextField usernameTextField;
  private JLabel usernameErrorLabel;
  private JPasswordField passwordTextField;
  private JLabel passwordErrorLabel;
  private JButton loginButton;

  public LoginPanel(MainWindow window) {
    super(new BorderLayout());
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");

    setFocusable(true);
    setVisible(true);
    initLayoutPanel();
    requestFocus();
    this.window.getRootPane().setDefaultButton(loginButton);

    Utility.assignComponentNames(this);
    this.setName("loginPanel");
  }

  private void initLayoutPanel() {
    usernameTextField = new JTextField("", 10);
    passwordTextField = new JPasswordField("", 10);

    loginButton = new JButton(this.languageBundle.getString("LOGIN"));
    loginButton.addActionListener(this);

    usernameErrorLabel = new JLabel();
    passwordErrorLabel = new JLabel();

    usernameErrorLabel.setForeground(new Color(255, 51, 51));
    usernameErrorLabel.setText(this.languageBundle.getString("ERRORNOUSERNAME"));

    passwordErrorLabel.setForeground(new Color(255, 51, 51));
    passwordErrorLabel.setText(this.languageBundle.getString("ERRORNOPASSWORD"));

    Map<String, JTextField> fieldMap = new HashMap<String, JTextField>();
    fieldMap.put(this.languageBundle.getString("PASSWORD"), passwordTextField);
    fieldMap.put(this.languageBundle.getString("USERNAME"), usernameTextField);

    Map<String, JLabel> errorLabelMap = new HashMap<String, JLabel>();
    errorLabelMap.put(this.languageBundle.getString("PASSWORD"), passwordErrorLabel);
    errorLabelMap.put(this.languageBundle.getString("USERNAME"), usernameErrorLabel);

    formPanel = new JPanel();
    formPanel.setLayout(new GridBagLayout());

    for (int i = 0; i < fieldMap.size(); i++) {
      addLabel(((String) (fieldMap.keySet().toArray()[i])), i);
      addTextField((JTextField) fieldMap.values().toArray()[i], i);
      addErrorTextLabel((JLabel) (errorLabelMap.values().toArray()[i]), i);
    }

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 5, 5, 5);
    formPanel.add(loginButton, gbc);

    add(formPanel);
  }

  private void addLabel(String text, int row) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 15);
    gbc.weightx = 0;
    formPanel.add(new JLabel(Character.toUpperCase(text.charAt(0)) + text.substring(1)), gbc);
  }

  private void addTextField(JTextField textField, int row) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 1;
    gbc.gridy = row;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    formPanel.add(textField, gbc);
  }

  private void addErrorTextLabel(JLabel label, int row) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = 1;
    gbc.gridx = 2;
    gbc.gridy = row;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.weightx = 0;
    label.setVisible(false);
    formPanel.add(label, gbc);
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.loginButton) {
      authenticateUser();
    }
  }

  private void authenticateUser() {
    String username = usernameTextField.getText();
    String password = new String(passwordTextField.getPassword());
    try {
      if (!username.isEmpty() && !password.isEmpty()) {
        usernameErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        new LoginRestController().loginUser(username, password);
        this.window.showStartMenu();
      } else {
        if (username.isEmpty()) {
          usernameErrorLabel.setVisible(true);
        } else {
          usernameErrorLabel.setVisible(false);
        }

        if (password.isEmpty()) {
          passwordErrorLabel.setVisible(true);
        } else {
          passwordErrorLabel.setVisible(false);
        }
      }
    } catch (UserNotFoundException unfe) {
      JOptionPane.showMessageDialog(this,
          this.languageBundle.getString("ERRORAUTHENTICATIONMESSAGE"),
          this.languageBundle.getString("ERRORAUTHENTICATIONTITLE"), JOptionPane.WARNING_MESSAGE);
      logger.error("User " + username + " not found: " + unfe.getMessage());
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "Something went horribly wrong! Possible causes: " + ex.getCause(), "Unknown exception",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.loginButton.setText(this.languageBundle.getString("LOGIN"));
    this.usernameTextField.setText(this.languageBundle.getString("USERNAME"));
    this.passwordTextField.setText(this.languageBundle.getString("PASSWORD"));
    this.usernameErrorLabel.setText(this.languageBundle.getString("ERRORNOUSERNAME"));
    this.passwordErrorLabel.setText(this.languageBundle.getString("ERRORNOPASSWORD"));
  }

}
