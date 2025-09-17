package service;

import entity.PersonEntity;
import entity.RegistrationEntity;
import model.Car;
import model.Course;
import model.Person;
import model.Registration;
import repository.PersonRepository;
import repository.RegistrationRepository;

import java.util.ArrayList;
import java.util.List;

public class PersonService {

    public static List<Person> getAllPersons() {

        List<PersonEntity> personEntities = PersonRepository.findAll();
        List<Person> result = new ArrayList<>();

        for (PersonEntity personEntity : personEntities) {

            Person person = new Person(personEntity.getId(), personEntity.getName());
            List<Car> cars = CarService.getAllCarsForOwner(personEntity.getId());
            person.setCars(cars);
            result.add(person);
        }
        return result;
    }

    public static List<Registration> getAllRegistrations() {

        List<RegistrationEntity> registrationEntities = RegistrationRepository.findAll();
        List<Registration> result = new ArrayList<>();

        for (RegistrationEntity entity : registrationEntities) {

            Registration registration = new Registration(new Person(entity.getPersonId(), entity.getPersonName()), new Course(entity.getCourseId(), entity.getCourseName()));
            result.add(registration);
        }

        return result;

    }
}
