package repository;

import entity.PersonEntity;
import mapper.PersonEntityMapper;
import service.DatabaseQueryExecutorService;

import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public static List<PersonEntity> findAll(){

        List<Object> objects = DatabaseQueryExecutorService.executeReadQuery("select * from person", new PersonEntityMapper());
        List<PersonEntity> result = new ArrayList<>();

        for (Object object : objects) {
            if(object instanceof PersonEntity){
                result.add((PersonEntity) object);
            } else {
                throw new RuntimeException("Expected PersonEntity instance");
            }
        }

        return result;
    }
}
