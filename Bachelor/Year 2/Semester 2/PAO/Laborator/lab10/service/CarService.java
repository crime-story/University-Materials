package service;

import entity.CarEntity;
import model.Car;
import repository.CarRepository;

import java.util.ArrayList;
import java.util.List;

public class CarService {

    public static List<Car> getAllCars() {

        List<CarEntity> carEntities = CarRepository.findAll();
        List<Car> result = new ArrayList<>();

        for (CarEntity carEntity : carEntities) {
            result.add(new Car(carEntity.getId(), carEntity.getName()));
        }
        return result;
    }

    public static List<Car> getAllCarsForOwner(int ownerId){

        List<CarEntity> carEntities = CarRepository.findAllForOwner(ownerId);
        List<Car> result = new ArrayList<>();

        for (CarEntity carEntity : carEntities) {
            result.add(new Car(carEntity.getId(), carEntity.getName()));
        }
        return result;
    }

    public static void addCar(Car car, int ownerId) {
        CarEntity carEntity = new CarEntity();
        carEntity.setName(car.getName());
        carEntity.setOwnerId(ownerId);

        CarRepository.addCar(carEntity);
    }
}
