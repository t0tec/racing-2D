package be.tiwi.vop.racing.desktop.aspectj;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.circuit.ObstacleType;
import be.tiwi.vop.racing.desktop.util.ResourceManager;

@Aspect
public class SoundAspect {
  private static final Logger logger = LoggerFactory.getLogger(SoundAspect.class);

  @Pointcut("execution(void be.tiwi.vop.racing.desktop.view.panels.*.startTimer(..))")
  public void startGame() {}

  @Before("startGame()")
  public void playBackgroundSoundLoop() {
    logger.info("starting background sound");
    ResourceManager.getInstance().getSoundClip("BACKGROUND").loop();
  }

  @Pointcut("execution(void be.tiwi.vop.racing.desktop.view.panels.*.stopTimer(..))")
  public void stopGame() {}

  @Before("stopGame()")
  public void stopPlayingBackgroundSoundLoop() {
    logger.info("stopping background sound");
    ResourceManager.getInstance().getSoundClip("BACKGROUND").stop();
  }

  @Pointcut("execution(* be.tiwi.vop.racing.desktop.model.car.Car.brake(..)) && args(isBraking)")
  public void brakeBefore(boolean isBraking) {}

  @Before("brakeBefore(isBraking)")
  public void playBrakeSound(boolean isBraking) {
    if (isBraking) {
      ResourceManager.getInstance().getSoundClip("BRAKE").play();
    } else {
      ResourceManager.getInstance().getSoundClip("BRAKE").stop();
    }
  }

  @Pointcut("execution(* be.tiwi.vop.racing.desktop.model.car.Car.isColliding(..)) && args(obstacleType) && this(car)")
  public void obstacleHit(ObstacleType obstacleType, Car car) {}

  @After("obstacleHit(obstacleType, car)")
  public void playCrashOrBonusSound(ObstacleType obstacleType, Car car) {
    if (car.isColliding()
        && (obstacleType.equals(ObstacleType.EGGPLANT) || obstacleType.equals(ObstacleType.PADDO))) {
      ResourceManager.getInstance().getSoundClip("POWER_DOWN").play();
    } else if (car.isColliding()) {
      ResourceManager.getInstance().getSoundClip("POWER_UP").play();
    }
  }

  @Pointcut("execution(* *.actionPerformed(*)) && args(actionEvent)")
  public void buttonPointcut(ActionEvent actionEvent) {}

  @Before("buttonPointcut(actionEvent)")
  public void beforeButtonPointcut(ActionEvent actionEvent) {
    if (actionEvent.getSource() instanceof JButton) {
      ResourceManager.getInstance().getSoundClip("BUTTON_CLICK").play();
    }
  }

}
