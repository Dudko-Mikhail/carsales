package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.RowMapper;
import by.dudko.carsales.model.entity.PhoneNumber;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumberMapper implements RowMapper<PhoneNumber> {
    private static final PhoneNumberMapper instance = new PhoneNumberMapper();

    public static PhoneNumberMapper getInstance() {
        return instance;
    }

    @Override
    public Optional<PhoneNumber> mapRow(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(PhoneNumber.builder()
                        .id(resultSet.getLong("id"))
                        .adId(resultSet.getLong("ad_id"))
                        .number(resultSet.getString("number"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Failed to map phoneNumber", e);
        }
        return Optional.empty();
    }
}
