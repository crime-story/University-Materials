package mapper;

import entity.PersonEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonEntityMapper implements RowMapper<PersonEntity> {

    public PersonEntity mapRow(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");

        return new PersonEntity(id, name);
    }
}
