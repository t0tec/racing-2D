package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.util.Utility;
import be.tiwi.vop.racing.desktop.view.Internationalization;
import be.tiwi.vop.racing.desktop.view.MainWindow;

public class SettingsPanel extends JPanel implements ActionListener, Internationalization {
  private static final Logger logger = LoggerFactory.getLogger(SettingsPanel.class);

  private static final ServiceLoader<javax.swing.LookAndFeel> LOOK_AND_FEEL_LOADER = ServiceLoader
      .load(javax.swing.LookAndFeel.class);

  private MainWindow window;

  private ResourceBundle languageBundle;

  private JPanel centerPanel;
  private JLabel titleLabel;
  private JLabel languageLabel;
  private JLabel lookAndFeelLabel;

  private JButton saveButton;
  private JButton backButton;
  private JComboBox<Language> languageCombobox;
  private JComboBox<LookAndFeel> lookAndFeelCombobox;
  private String lookAndFeel;

  public SettingsPanel(MainWindow window) {
    super(new BorderLayout());
    this.window = window;
    this.languageBundle = ResourceBundle.getBundle("languages/language");
    setFocusable(true);
    setVisible(true);
    initLayoutPanel();
    requestFocus();

    Utility.assignComponentNames(this);
    this.setName("settingsPanel");
  }

  private void initLayoutPanel() {
    titleLabel = new JLabel(this.languageBundle.getString("SETTINGSTITLE"));
    languageLabel = new JLabel(this.languageBundle.getString("SETTINGSLANGUAGE"));
    lookAndFeelLabel = new JLabel("Look and feel: ");

    saveButton = new JButton(languageBundle.getString("SAVESETTINGS"));
    saveButton.addActionListener(this);
    backButton = new JButton(languageBundle.getString("BACK"));
    backButton.addActionListener(this);

    languageCombobox = new JComboBox<Language>();
    languageCombobox.addItem(new Language("default"));
    loadSupportedLanguages();

    lookAndFeelCombobox = new JComboBox<LookAndFeel>();
    loadSupportedLookAndFeels();

    centerPanel = new JPanel(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    centerPanel.add(titleLabel, gbc);
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridy++;
    centerPanel.add(languageLabel, gbc);
    gbc.gridx++;
    centerPanel.add(languageCombobox, gbc);
    gbc.gridy++;
    gbc.gridx--;
    centerPanel.add(lookAndFeelLabel, gbc);
    gbc.gridx++;
    centerPanel.add(lookAndFeelCombobox, gbc);
    gbc.gridy++;
    gbc.gridx--;
    gbc.anchor = GridBagConstraints.EAST;
    centerPanel.add(backButton, gbc);
    gbc.gridx++;
    centerPanel.add(saveButton, gbc);

    add(centerPanel);
  }

  private void loadSupportedLanguages() {
    try {
      InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
      Properties props = new Properties();
      props.load(is);
      String languages = props.getProperty("languages.supported");

      String[] supportedLanguages = languages.split(",");

      for (int i = 0; i < supportedLanguages.length; i++) {
        languageCombobox.addItem(new Language(supportedLanguages[i]));
      }

    } catch (IOException ioEx) {
      logger.error("IO eror {}", ioEx.getMessage());
    }
  }

  private void loadSupportedLookAndFeels() {
    UIManager.LookAndFeelInfo lookAndFeelInfos[] = UIManager.getInstalledLookAndFeels();

    lookAndFeel = UIManager.getLookAndFeel().getClass().getName();
    for (UIManager.LookAndFeelInfo lafInfo : lookAndFeelInfos) {
      lookAndFeelCombobox.addItem(new LookAndFeel(lafInfo.getName(), lafInfo.getClassName()));
    }

    // Now load any look and feels defined externally as service via java.util.ServiceLoader
    LOOK_AND_FEEL_LOADER.iterator();
    for (javax.swing.LookAndFeel laf : LOOK_AND_FEEL_LOADER) {
      lookAndFeelCombobox.addItem(new LookAndFeel(laf.getName(), laf.getClass().getName()));
    }

  }

  private void updateLookAndFeel() {
    Window windows[] = Frame.getWindows();

    for (Window window : windows) {
      SwingUtilities.updateComponentTreeUI(window);
    }
  }

  public void setLookAndFeel(String lookAndFeel) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

    String oldLookAndFeel = this.lookAndFeel;

    if (!oldLookAndFeel.equals(lookAndFeel)) {
      UIManager.setLookAndFeel(lookAndFeel);
      this.lookAndFeel = lookAndFeel;
      updateLookAndFeel();
      firePropertyChange("lookAndFeel", oldLookAndFeel, lookAndFeel);
    }
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.saveButton) {
      Language language = ((Language) languageCombobox.getSelectedItem());

      Properties properties = new Properties();
      properties.setProperty("language", language.code);

      Locale.setDefault(new Locale(properties.getProperty("language")));
      this.window.translateAllComponents();

      LookAndFeel laf = (LookAndFeel) lookAndFeelCombobox.getSelectedItem();
      try {
        this.setLookAndFeel(laf.getClassName());
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      logger.info("Changed language to {}", Locale.getDefault().getDisplayLanguage());
      logger.info("Set look and feel to {}", laf.getName());
    } else if (ae.getSource() == this.backButton) {
      this.window.showStartMenu();
    }
  }

  @Override
  public void translateComponent(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
    this.saveButton.setText(this.languageBundle.getString("SAVESETTINGS"));
    this.backButton.setText(this.languageBundle.getString("BACK"));
    this.titleLabel.setText(this.languageBundle.getString("SETTINGSTITLE"));
    this.languageLabel.setText(this.languageBundle.getString("SETTINGSLANGUAGE"));
  }

  protected class Language {
    private String code;

    public Language(String code) {
      this.code = code;
    }

    public String getCode() {
      return this.code;
    }

    @Override
    public String toString() {
      if (!this.code.equals("default")) {
        return new Locale(code).getDisplayLanguage();
      } else {
        return code;
      }
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((code == null) ? 0 : code.hashCode());
      return result;
    }

    private SettingsPanel getOuterType() {
      return SettingsPanel.this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null) {
        return false;
      }

      return this.code.equals(((Language) o).code);
    }
  }

  protected class LookAndFeel {
    private String name;
    private String className;

    public LookAndFeel(String name, String className) {
      this.name = name;
      this.className = className;
    }

    public String getName() {
      return name;
    }

    public String getClassName() {
      return className;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((className == null) ? 0 : className.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof LookAndFeel)) {
        return false;
      }

      LookAndFeel that = (LookAndFeel) o;

      if (!className.equals(that.className)) {
        return false;
      }
      if (!name.equals(that.name)) {
        return false;
      }

      return true;
    }

    private SettingsPanel getOuterType() {
      return SettingsPanel.this;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
