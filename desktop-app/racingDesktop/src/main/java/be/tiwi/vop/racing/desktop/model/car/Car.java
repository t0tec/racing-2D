package be.tiwi.vop.racing.desktop.model.car;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.car.handling.ICarHandling;
import be.tiwi.vop.racing.desktop.model.circuit.Obstacle;
import be.tiwi.vop.racing.desktop.model.circuit.ObstacleType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Car {
  private static final Logger logger = LoggerFactory.getLogger(Car.class);

  private static final int SPEEDCOEFFICIENT = 100;
  private static final double WHEELBASE = 3000;

  private double carVelocity = 0;
  private double carOrientation = 0;
  private double steeringInput = 0;
  private boolean isAccelerating = false;
  private boolean isBraking = false;
  private boolean isSteeringLeft = false;
  private boolean isSteeringRight = false;

  private double roadResistance = -0.01;
  private boolean isColliding = false;

  private int amount = 150;

  private int batteryERSLevel = 100;
  private boolean isBatteryERSActive = false;
  private static final int maximumBatteryERSLevel = 100;

  private ICarHandling carHandling;
  private Point rearTyres;
  private Point frontTyres;

  private String carImageLocation;
  private static final int IMAGE_WIDTH = 40;
  private static final int IMAGE_HEIGHT = 20;

  public Car(ICarHandling carHandling, String carImageLocation) {
    this.carHandling = carHandling;
    this.carImageLocation = carImageLocation;
  }

  private void changeVelocity() {
    this.carVelocity =
        this.carHandling.calculateVelocity(this.carVelocity, this.roadResistance,
            this.isBatteryERSActive, this.isAccelerating, this.isBraking);
  }

  public void updatePosition() {
    if (this.batteryERSLevel < 0) {
      isBatteryERSActive = false;
    }

    if (isBatteryERSActive && this.batteryERSLevel > 0) {
      this.batteryERSLevel -= 1;
    }

    calculateSteeringInput();

    if (this.isColliding) {
      if (!this.isPenalty) {
        this.trigger(3000, amount);
      } else {
        this.trigger(3000, -amount);
      }
    }

    changeVelocity();

    Point[] newPosition =
        this.carHandling.calculateNewCarPosition(this.rearTyres, this.frontTyres, WHEELBASE,
            this.carVelocity, this.carOrientation, this.steeringInput);

    this.rearTyres = newPosition[0];
    this.frontTyres = newPosition[1];

    this.carOrientation = this.carHandling.calculateNewOrientation(this.rearTyres, this.frontTyres);
  }

  public void accelerate(boolean isAccelerating) {
    this.isAccelerating = isAccelerating;
  }

  public void brake(boolean isBraking) {
    this.isBraking = isBraking;
  }

  public void steerLeft(boolean isSteeringLeft) {
    this.isSteeringLeft = isSteeringLeft;
  }

  public void steerRight(boolean isSteeringRight) {
    this.isSteeringRight = isSteeringRight;
  }

  private void calculateSteeringInput() {
    this.steeringInput =
        this.carHandling.calculateSteeringInput(this.isSteeringLeft, this.isSteeringRight);
  }

  public void handleKeyInput(int key, boolean isKeyDown) {
    // 32 == SPACE, 38 = UP, 40 = RIGHT, 39 = LEFT, 37 = DOWN
    if (key == KeyEvent.VK_UP) {
      accelerate(isKeyDown);
    } else if (key == KeyEvent.VK_DOWN) {
      brake(isKeyDown);
    } else if (key == KeyEvent.VK_RIGHT) {
      steerRight(isKeyDown);
    } else if (key == KeyEvent.VK_LEFT) {
      steerLeft(isKeyDown);
    } else if (key == KeyEvent.VK_SPACE) {
      if (this.batteryERSLevel > 0) {
        isBatteryERSActive = isKeyDown;
      } else {
        isBatteryERSActive = false;
      }
    }
  }

  public ICarHandling getCarHandling() {
    return this.carHandling;
  }

  public void setCarHandling(ICarHandling carHandling) {
    this.carHandling = carHandling;
  }

  public double getOrientation() {
    return this.carOrientation;
  }

  public void setOrientation(double carOrientation) {
    this.carOrientation = carOrientation;

    Point position = new Point(getCenterPosition());

    this.frontTyres =
        new Point((int) (position.x * SPEEDCOEFFICIENT + WHEELBASE / 2
            * Math.cos(this.carOrientation)), (int) (position.y * SPEEDCOEFFICIENT + WHEELBASE / 2
            * Math.sin(this.carOrientation)));
    this.rearTyres =
        new Point((int) (position.x * SPEEDCOEFFICIENT - WHEELBASE / 2
            * Math.cos(this.carOrientation)), (int) (position.y * SPEEDCOEFFICIENT - WHEELBASE / 2
            * Math.sin(this.carOrientation)));
  }

  public Point getCenterPosition() {
    return new Point((this.frontTyres.x + this.rearTyres.x) / 100 / 2,
        (this.frontTyres.y + this.rearTyres.y) / 100 / 2);
  }

  public void setCenterPosition(Point position) {
    this.frontTyres =
        new Point((int) (position.x * SPEEDCOEFFICIENT + WHEELBASE / 2
            * Math.cos(this.carOrientation)), (int) (position.y * SPEEDCOEFFICIENT + WHEELBASE / 2
            * Math.sin(this.carOrientation)));
    this.rearTyres =
        new Point((int) (position.x * SPEEDCOEFFICIENT - WHEELBASE / 2
            * Math.cos(this.carOrientation)), (int) (position.y * SPEEDCOEFFICIENT - WHEELBASE / 2
            * Math.sin(this.carOrientation)));
  }

  public void resetCar() {
    logger.info("Resetting car for default position");
    this.isBraking = false;
    this.isAccelerating = false;
    this.isSteeringLeft = false;
    this.isSteeringRight = false;
    this.isColliding = false;
    this.isBatteryERSActive = false;

    this.carVelocity = 0;
    this.steeringInput = 0;
  }

  public void doCollision(Point obstaclePosition) {
    if (getCenterPosition().x > obstaclePosition.x - Obstacle.getObstaclesize() / 2
        && getCenterPosition().x < obstaclePosition.x + Obstacle.getObstaclesize() / 2
        && -getCenterPosition().y > obstaclePosition.y - Obstacle.getObstaclesize() / 2
        && -getCenterPosition().y < obstaclePosition.y + Obstacle.getObstaclesize() / 2) {
      this.isColliding = true;
    } else {
      this.isColliding = false;
    }
  }

  public boolean hasCrossedFinishline(Point finishLinePosition) {
    if (getCenterPosition().x > finishLinePosition.x - 10
        && getCenterPosition().x < finishLinePosition.x
        && -getCenterPosition().y > finishLinePosition.y - 50
        && -getCenterPosition().y < finishLinePosition.y + 50) {
      return true;
    } else {
      return false;
    }
  }

  public void trigger(int duration, int amount) {
    this.carHandling.doAccelerationAdjustment(duration, amount);
    this.isColliding = false;
  }

  private boolean isPenalty;

  public boolean isColliding(ObstacleType obstacleType) {
    if (obstacleType.equals(ObstacleType.EGGPLANT) || obstacleType.equals(ObstacleType.PADDO)) {
      this.isPenalty = true;
    } else {
      this.isPenalty = false;
    }

    if (obstacleType.equals(ObstacleType.EGGPLANT) || obstacleType.equals(ObstacleType.STRAWBERRY)) {
      this.amount = 150;
    } else {
      this.amount = 300;
    }

    return this.isColliding;
  }

  public void changeRoadResistance() {
    this.roadResistance = -0.1;

  }

  public void resetRoadResitance() {
    this.roadResistance = -0.01;
  }

  public boolean isColliding() {
    return isColliding;
  }

  public int getBatteryERSLevel() {
    return batteryERSLevel;
  }

  public void setBatteryERSLevel(int batteryERSLevel) {
    this.batteryERSLevel = batteryERSLevel;
  }

  public static int getMaximumBatteryERSLevel() {
    return maximumBatteryERSLevel;
  }

  public String getCarImageLocation() {
    return carImageLocation;
  }

  public static int getImageWidth() {
    return IMAGE_WIDTH;
  }

  public static int getImageHeight() {
    return IMAGE_HEIGHT;
  }

  public static class CarInterfaceAdapter implements JsonSerializer<Car>, JsonDeserializer<Car> {

    @Override
    public JsonElement serialize(final Car car, final Type type,
        final JsonSerializationContext context) {
      final JsonObject wrapper = new JsonObject();
      wrapper.addProperty("type", car.getCarHandling().getClass().getName());
      wrapper.add("data", context.serialize(car.getCarHandling()));
      wrapper.add("carImageLocation", new JsonPrimitive(car.getCarImageLocation()));
      return wrapper;
    }

    @Override
    public Car deserialize(JsonElement jsonElem, Type type, JsonDeserializationContext context)
        throws JsonParseException {
      final JsonObject wrapper = (JsonObject) jsonElem;
      final JsonElement typeName = get(wrapper, "type");
      final JsonElement data = get(wrapper, "data");
      final Type actualType = typeForName(typeName);

      JsonElement element = wrapper.get("carImageLocation");

      Car car =
          new Car((ICarHandling) context.deserialize(data, actualType), element.getAsString());

      return car;
    }

    private Type typeForName(final JsonElement typeElem) {
      try {
        return Class.forName(typeElem.getAsString());
      } catch (ClassNotFoundException e) {
        throw new JsonParseException(e);
      }
    }

    private JsonElement get(final JsonObject wrapper, String memberName) {
      final JsonElement elem = wrapper.get(memberName);
      if (elem == null)
        throw new JsonParseException("no '" + memberName
            + "' member found in what was expected to be an interface wrapper");
      return elem;
    }

  }
}
