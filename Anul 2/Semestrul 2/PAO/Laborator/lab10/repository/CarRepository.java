package repository;

import entity.CarEntity;
import mapper.CarEntityMapper;
import service.DatabaseQueryExecutorService;

import java.util.ArrayList;
import java.util.List;

public class CarRepository {

    public static List<CarEntity> findAll() {

        List<Object> objects = DatabaseQueryExecutorService.executeReadQuery("select * from car", new CarEntityMapper());
        List<CarEntity> result = new ArrayList<>();

        for (Object object : objects) {
            if (object instanceof CarEntity) {
                result.add((CarEntity) object);
            } else {
                throw new RuntimeException("Expected CarEntity instance");
            }
        }

        return result;
    }

    public static List<CarEntity> findAllForOwner(int ownerId) {
        List<Object> objects = DatabaseQueryExecutorService.executeReadQuery("select * from car where car.ownerId = " + ownerId, new CarEntityMapper());
        List<CarEntity> result = new ArrayList<>();

        for (Object object : objects) {
            if (object instanceof CarEntity) {
                result.add((CarEntity) object);
            } else {
                throw new RuntimeException("Expected CarEntity instance");
            }
        }

        return result;
    }

    public static void addCar(CarEntity carEntity) {

        DatabaseQueryExecutorService.executeUpdateQuery("INSERT into car(name) values('" + carEntity.getName() + "')");
    }
}
