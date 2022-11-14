package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.RowMapper;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.CarState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarAdMapper implements RowMapper<CarAd> {
    private static final CarAdMapper instance = new CarAdMapper();
    private final PhoneNumberMapper phoneNumberMapper = PhoneNumberMapper.getInstance();

    public static CarAdMapper getInstance() {
        return instance;
    }

    @Override
    public Optional<CarAd> mapRow(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(CarAd.builder()
                        .id(resultSet.getLong("id"))
                        .userId(resultSet.getLong("user_id"))
                        .year(resultSet.getInt("year"))
                        .brand(resultSet.getString("brand"))
                        .model(resultSet.getString("model"))
                        .engineVolume(resultSet.getInt("engine_volume"))
                        .carState(CarState.valueOf(resultSet.getString("car_state")))
                        .mileage(resultSet.getInt("mileage"))
                        .power(resultSet.getInt("power"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            log.error("Failed to map carAd", e);
        }
        return Optional.empty();
    }
}
