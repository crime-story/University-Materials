package service;

import model.Car;

public class Application {

    public static void main(String[] args) {

        // afisare toate masinile
        System.out.println(CarService.getAllCars());

        // afisare toate persoanele
        System.out.println(PersonService.getAllPersons());

        // afisare toate inregistrarile
        System.out.println(PersonService.getAllRegistrations());

        // adauga masina
        CarService.addCar(new Car("Mercedes"), 1);

        System.out.println(CarService.getAllCars());
    }
}
