package be.tiwi.vop.racing.desktop.util;

import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JApplet;

/**
 * This is a simple Resource Manager for loading images and wav files. It also tries to allow a
 * means of releasing the resource to the control of the VM.
 * 
 * Note: that files will only load from stand-a-lone applications. Applets won't be able to use this
 * class unless all files are changed to urls. Also images won't be able to be loaded from within
 * jar files.
 */
public class ResourceManager {

  // the resource container for Images referenced by a string
  private HashMap<String, Image> images = new HashMap<String, Image>();

  // the resource container for AudioClips referenced by a string
  private HashMap<String, AudioClip> audio = new HashMap<String, AudioClip>();

  // the one and only instance of this class
  private static ResourceManager singleton = new ResourceManager();

  protected ResourceManager() {
    loadImages();
    loadSounds();
  }

  /**
   * Get a reference to this class globally.
   * 
   * @return The one and only instantiated object of this class.
   */
  public static ResourceManager getInstance() {
    if (singleton == null) {
      singleton = new ResourceManager();
    }
    return singleton;
  }

  private void loadImages() {
    // load tiles textures
    this.loadImage("tiles/EARTH.png", "EARTH");
    this.loadImage("tiles/START.png", "START");
    this.loadImage("tiles/STRAIGHT.png", "STRAIGHT");
    this.loadImage("tiles/STRAIGHT_UP.png", "STRAIGHT_UP");
    this.loadImage("tiles/L_TURN.png", "L_TURN");
    this.loadImage("tiles/L_TURN_90.png", "L_TURN_90");
    this.loadImage("tiles/L_TURN_180.png", "L_TURN_180");
    this.loadImage("tiles/L_TURN_270.png", "L_TURN_270");
    this.loadImage("tiles/CROSS.png", "CROSS");

    // load tiles checkpoint textures
    this.loadImage("tiles/STRAIGHT_CHECKPOINT.png", "STRAIGHT_CHECKPOINT");
    this.loadImage("tiles/STRAIGHT_UP_CHECKPOINT.png", "STRAIGHT_UP_CHECKPOINT");
    this.loadImage("tiles/L_TURN_CHECKPOINT.png", "L_TURN_CHECKPOINT");
    this.loadImage("tiles/L_TURN_90_CHECKPOINT.png", "L_TURN_90_CHECKPOINT");
    this.loadImage("tiles/L_TURN_180_CHECKPOINT.png", "L_TURN_180_CHECKPOINT");
    this.loadImage("tiles/L_TURN_270_CHECKPOINT.png", "L_TURN_270_CHECKPOINT");

    // load obstacles textures
    this.loadImage("obstacles/EGGPLANT.png", "EGGPLANT");
    this.loadImage("obstacles/MELON.png", "MELON");
    this.loadImage("obstacles/STRAWBERRY.png", "STRAWBERRY");
    this.loadImage("obstacles/PADDO.png", "PADDO");
  }

  private void loadSounds() {
    this.loadSoundClip("sounds/button_click.wav", "BUTTON_CLICK");
    this.loadSoundClip("sounds/background.wav", "BACKGROUND");
    this.loadSoundClip("sounds/brake.wav", "BRAKE");
    this.loadSoundClip("sounds/power_up.wav", "POWER_UP");
    this.loadSoundClip("sounds/power_down.wav", "POWER_DOWN");
  }

  /**
   * The main method that should be used when rendering Images in your game. Here is an example:
   * Image img = ResourceManager.getInstance().getImage("logo"); graphics.drawImage(img, 0, 0,
   * null);
   * 
   * @param refname The key object in the images map
   * @return The image object or null if there was none.
   */
  public Image getImage(String refname) {
    return images.get(refname);
  }

  /**
   * This method should be manually called when you are done with a resource.
   * 
   * @param refname The key of the object you want to remove from the map
   */
  public void removeImage(String refname) {
    images.remove(refname);
  }

  /**
   * One of the methods of loading an Image into the ResourceManager.
   * 
   * @param path The relative or absolute path of the file to load
   * @param refname The key that you want to reference this image by.
   * @return Returns the loaded image in case you want to keep a reference to it after loading it
   *         instead of calling getImage().
   */
  private Image loadImage(String path, String refname) {
    // get the image.
    Image img = fetchImage(path, refname);

    // might replace the existing image with what was already there.
    // but hopefully that won't occur too often.
    images.put(refname, img);

    return img;
  }

  /**
   * Returns the current audio mapped to the reference name string.
   * 
   * @param refname The key in the audio map
   * @return The audio clip associated with the name key.
   */
  public AudioClip getSoundClip(String refname) {
    return audio.get(refname);
  }

  /**
   * Attempts to load an AudioClip. Returns null if unsuccessful.
   * 
   * @param path The relative or absolute file path of the resource.
   * @param refname The key in the map where to place the object.
   * @return The audio clip loaded.
   */
  public AudioClip loadSoundClip(String path, String refname) {
    // check to see if there already is an AudioClip with the refname.
    if (!audio.containsKey(refname)) {
      URL url = this.getClass().getClassLoader().getResource(path);
      AudioClip ac = JApplet.newAudioClip(url);
      audio.put(refname, ac);
      return ac;
    } else {
      return audio.get(refname);
    }
  }

  /**
   * Removes an audio clip from the audio map. To try and release the resource allocation to the VM.
   * 
   * @name The key of the audio clip to remove.
   */
  public void removeAudio(String refname) {
    audio.remove(refname);
  }

  /**
   * Attempts to load an Image from the specified path.
   * 
   * Will return null if this method failed to load an image. This method will be used by other
   * methods in this class but should not be available to any other class
   * 
   * @param path A valid file path string either relative or absolute.
   * @param refname
   * @return A valid Image instance or null.
   */
  private Image fetchImage(String path, String refname) {
    // this method can be used to load resource differently for
    // applets and from jar files.

    // check to see if the reference already exists else load it.
    if (images.containsKey(refname)) {
      return images.get(refname);
    } else {
      try {
        URL url = this.getClass().getClassLoader().getResource(path);
        return ImageIO.read(url);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
