package be.tiwi.vop.racing.desktop.util;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utility {
  private static final Logger logger = LoggerFactory.getLogger(Utility.class);

  public static String timeFormat(final int time) {
    int minutes = time / 60000 % 60;
    int seconds = time / 1000 % 60;
    int milliseconds = time % 1000;

    return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
  }

  @SuppressWarnings("rawtypes")
  public static void assignComponentNames(Object obj) {
    try {
      Method getComponentsMethod = obj.getClass().getMethod("getComponents", new Class[] {});

      if (null != getComponentsMethod) {
        try {
          for (Component component : (Component[]) (getComponentsMethod.invoke(obj))) {
            assignComponentNames(component);
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      } else {
        logger.info(obj.toString());
      }
    } catch (NoSuchMethodException e) {
      logger.error("No such method: {}", e.getMessage());
    }

    for (Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      String fieldName = field.getName(); // this is a different name, the reflection level name
      Object fieldValue = null;

      try {
        fieldValue = field.get(obj);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      if (null == fieldValue) {
        continue;
      }

      if (fieldValue instanceof JComponent) {

        String currentComponentNameForFieldValue = ((JComponent) fieldValue).getName();

        if (null == currentComponentNameForFieldValue) {
          ((JComponent) fieldValue).setName(fieldName); // this sets the name specially for
                                                        // JComponent
        }
      } else if (fieldValue instanceof Collection) {

        for (Object subObject : ((Collection) fieldValue).toArray()) {

          assignComponentNames(subObject);
        }
      }

    }
  }

}
