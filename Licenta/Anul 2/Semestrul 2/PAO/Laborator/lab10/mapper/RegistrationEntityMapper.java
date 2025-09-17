package mapper;

import entity.RegistrationEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationEntityMapper implements RowMapper<RegistrationEntity> {

    public RegistrationEntity mapRow(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        int personId = resultSet.getInt("person_id");
        int courseId = resultSet.getInt("course_id");
        String personName = resultSet.getString("person_name");
        String courseName = resultSet.getString("course_name");


        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setCourseId(courseId);
        registrationEntity.setPersonId(personId);
        registrationEntity.setPersonName(personName);
        registrationEntity.setCourseName(courseName);
        registrationEntity.setId(id);

        return registrationEntity;
    }
}
