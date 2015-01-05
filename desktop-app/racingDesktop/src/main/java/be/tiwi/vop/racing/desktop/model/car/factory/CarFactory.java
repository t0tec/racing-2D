package be.tiwi.vop.racing.desktop.model.car.factory;

import java.util.List;
import java.util.ResourceBundle;

import be.tiwi.vop.racing.desktop.model.car.Car;
import be.tiwi.vop.racing.desktop.model.car.Car.CarInterfaceAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public final class CarFactory {
  private static final String BASE_NAME = "cars/cars";

  private static List<Car> cars;

  private static int selectedCarIndex;

  public static List<Car> getCars() {
    ResourceBundle carPropertiesBundle = ResourceBundle.getBundle(BASE_NAME);
    String jsonStringCars = carPropertiesBundle.getString("cars");

    Gson gson =
        new GsonBuilder().registerTypeAdapter(Car.class, new CarInterfaceAdapter()).create();
    cars = gson.fromJson(jsonStringCars, new TypeToken<List<Car>>() {}.getType());

    return cars;
  }

  public static Car getCar() {
    return cars.get(selectedCarIndex);
  }

  public static void setSelectedCarIndex(int carIndex) {
    selectedCarIndex = carIndex;
  }
}
