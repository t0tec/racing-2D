package be.tiwi.vop.racing.desktop.view.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.circuit.Circuit;
import be.tiwi.vop.racing.desktop.model.circuit.Circuit.Direction;
import be.tiwi.vop.racing.desktop.model.circuit.Tile;
import be.tiwi.vop.racing.desktop.model.circuit.TileType;
import be.tiwi.vop.racing.desktop.restcontroller.CircuitRestController;
import be.tiwi.vop.racing.desktop.util.ResourceManager;

public class PreviewPanel extends JPanel {
  private static final Logger logger = LoggerFactory.getLogger(PreviewPanel.class);

  private BufferedImage image;
  private Image scaledImage;
  private Circuit circuit;

  public PreviewPanel() {
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent event) {
        formComponentResized(event);
      }
    });
  }

  public void loadPreview(int circuitId) {
    createCircuitBoard(circuitId);
    setScaledImage();
  }

  private void createCircuitBoard(int circuitId) {
    logger.info("creating preview circuit board");
    this.circuit = new CircuitRestController().loadCircuit(circuitId);

    this.image =
        new BufferedImage(circuit.getColumns() * circuit.getTILESIZE(), circuit.getRows()
            * circuit.getTILESIZE(), BufferedImage.TYPE_INT_RGB);

    Graphics2D g2d = (Graphics2D) this.image.getGraphics();

    for (int y = 0; y < circuit.getColumns(); y++) {
      for (int x = 0; x < circuit.getRows(); x++) {
        Tile tile = circuit.getTileByXandY(y, x);

        if (tile != null) {
          drawTile(g2d, tile.getX() * circuit.getTILESIZE(), tile.getY() * circuit.getTILESIZE(),
              loadTileImage(tile));
        } else {
          Tile earthTile = new Tile();
          earthTile.setType(TileType.EARTH);
          earthTile.setX(y);
          earthTile.setY(x);
          earthTile.setCircuitid(circuit.getId());
          earthTile.setCheckpoint(0);
          circuit.getTiles().add(earthTile);

          drawTile(g2d, earthTile.getX() * circuit.getTILESIZE(),
              earthTile.getY() * circuit.getTILESIZE(), loadTileImage(earthTile));
        }
      }
    }

  }

  private Image getScaledImage(Image image, int w, int h) {
    BufferedImage scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = scaledImage.createGraphics();
    g2.scale(1, -1);
    g2.translate(0, -h);
    g2.drawImage(image, 0, 0, w, h, null);
    g2.dispose();
    return scaledImage;
  }

  public Image getScaledImage() {
    return this.getScaledImage(this.image, 300, 300);
  }

  private void drawTile(Graphics2D g, int x, int y, Image image) {
    AffineTransform at = new AffineTransform();
    at.translate(x, y);
    g.drawImage(image, at, null);
  }

  private BufferedImage loadTileImage(Tile tile) {
    BufferedImage bufferedImage;

    if (tile.getCheckpoint() != 0) {
      bufferedImage =
          (BufferedImage) ResourceManager.getInstance().getImage(
              tile.getType().toString() + "_CHECKPOINT");
    } else {
      bufferedImage =
          (BufferedImage) ResourceManager.getInstance().getImage(tile.getType().toString());
    }

    if (tile.getType() == TileType.START) {
      bufferedImage = setStartTileDirection(bufferedImage);
    }

    return bufferedImage;
  }

  private BufferedImage setStartTileDirection(BufferedImage bufferedImage) {
    if (this.circuit.getDirection() == Direction.LEFT) {
      return horizontalflip(bufferedImage);
    } else if (this.circuit.getDirection() == Direction.UP) {
      return verticalflip(bufferedImage);
    } else {
      return bufferedImage;
    }
  }

  private BufferedImage horizontalflip(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(w, h, img.getType());
    Graphics2D g = dimg.createGraphics();
    g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
    g.dispose();
    return dimg;
  }

  private BufferedImage verticalflip(BufferedImage img) {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
    Graphics2D g = dimg.createGraphics();
    g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
    g.dispose();
    return dimg;
  }

  private void formComponentResized(ComponentEvent event) {
    setScaledImage();
  }

  private void setScaledImage() {
    if (image != null) {

      float imageWidth = image.getWidth(this);
      float imageHeight = image.getHeight(this);
      float panelWidth = this.getWidth();
      float panelHeight = this.getHeight();

      if (panelWidth < imageWidth || panelHeight < imageHeight) {
        if ((panelWidth / panelHeight) > (imageWidth / imageHeight)) {
          imageWidth = -1;
          imageHeight = panelHeight;
        } else {
          imageWidth = panelWidth;
          imageHeight = -1;
        }

        // prevent errors if panel is 0 wide or high
        if (imageWidth == 0) {
          imageWidth = -1;
        }

        if (imageHeight == 0) {
          imageHeight = -1;
        }

        scaledImage =
            image.getScaledInstance((int) imageWidth, (int) imageHeight, Image.SCALE_DEFAULT);

      } else {
        scaledImage = image;
      }
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (scaledImage != null) {
      g.drawImage(scaledImage, 0, 0, this);
    }
  }

}
