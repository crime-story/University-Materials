package repository;

import entity.RegistrationEntity;
import mapper.RegistrationEntityMapper;
import service.DatabaseQueryExecutorService;

import java.util.ArrayList;
import java.util.List;

public class RegistrationRepository {

    public static List<RegistrationEntity> findAll(){

        List<Object> objects = DatabaseQueryExecutorService.executeReadQuery("Select registration.id,person.name as person_name, course.name as course_name, person_id, course_id from course join person join registration\n" +
                "where registration.person_id = person.id\n" +
                "and registration.course_id = course.id", new RegistrationEntityMapper());
        List<RegistrationEntity> result = new ArrayList<>();

        for (Object object : objects) {
            if(object instanceof RegistrationEntity){
                result.add((RegistrationEntity) object);
            } else {
                throw new RuntimeException("Expected RegistrationEntity instance");
            }
        }

        return result;
    }
}
