package mapper;

import entity.CarEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarEntityMapper implements RowMapper<CarEntity> {

    public CarEntity mapRow(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int ownerId = resultSet.getInt("ownerId");


        return new CarEntity(id, name, ownerId);
    }
}
