package by.dudko.carsales.mapper;

import by.dudko.carsales.model.entity.BaseEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RowMapper<T extends BaseEntity<?>> {
    Optional<T> mapRow(ResultSet resultSet);

    default List<T> mapRows(ResultSet resultSet) {
        List<T> entities = new ArrayList<>();
        Optional<T> entity = mapRow(resultSet);
        while (entity.isPresent()) {
            entities.add(entity.get());
            entity = mapRow(resultSet);
        }
        return entities;
    }
}
